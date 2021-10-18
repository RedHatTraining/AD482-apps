package com.redhat.garden;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.redhat.garden.entities.GardenMeasurementTrend;
import com.redhat.garden.entities.GardenStatus;
import com.redhat.garden.entities.Sensor;
import com.redhat.garden.entities.SensorMeasurement;
import com.redhat.garden.entities.SensorMeasurementEnriched;
import com.redhat.garden.entities.SensorMeasurementType;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.WindowStore;
import org.apache.kafka.streams.test.TestRecord;


public class MeasurementStreamEnricherTest {

    TopologyTestDriver testDriver;

    TestInputTopic<Integer, Sensor> sensorsTopic;
    ObjectMapperSerde<Sensor> sensorSerde;

    TestInputTopic<Integer, SensorMeasurement> sensorMeasurementsTopic;
    ObjectMapperSerde<SensorMeasurement> sensorMeasurementSerde;

    TestOutputTopic<Integer, SensorMeasurementEnriched> enrichedMeasurementsTopic;
    ObjectMapperSerde<SensorMeasurementEnriched> enrichedMeasurementSerde;

    TestOutputTopic<String, GardenStatus> gardenStatusEventsTopic;
    ObjectMapperSerde<GardenStatus> gardenStatusEventSerde;


    @BeforeEach
    public void setup() {
        MeasurementStreamEnricher topologyBuilder = new MeasurementStreamEnricher();
        testDriver = new TopologyTestDriver(topologyBuilder.build());

        sensorSerde = new ObjectMapperSerde<>(Sensor.class);
        sensorsTopic = testDriver.createInputTopic(
                    MeasurementStreamEnricher.SENSORS_TOPIC,
                    new IntegerSerializer(),
                    sensorSerde.serializer());

        sensorMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurement.class);
        sensorMeasurementsTopic = testDriver.createInputTopic(
                    MeasurementStreamEnricher.SENSOR_MEASUREMENTS_TOPIC,
                    new IntegerSerializer(),
                    sensorMeasurementSerde.serializer());

        enrichedMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurementEnriched.class);
        enrichedMeasurementsTopic = testDriver.createOutputTopic(
                    RulesProcessor.ENRICHED_SENSOR_MEASUREMENTS_TOPIC,
                    new IntegerDeserializer(),
                    enrichedMeasurementSerde.deserializer());


        gardenStatusEventSerde = new ObjectMapperSerde<>(GardenStatus.class);
        gardenStatusEventsTopic = testDriver.createOutputTopic(
            MeasurementStreamEnricher.GARDEN_STATUS_EVENTS_TOPIC,
            new StringDeserializer(),
            gardenStatusEventSerde.deserializer());
    }

    @AfterEach
    public void teardown() {
        testDriver.close();
        sensorMeasurementSerde.close();
        enrichedMeasurementSerde.close();
        gardenStatusEventSerde.close();
    }

    @Test
    public void testWritesEnrichedStreamToTopic() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.5, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, SensorMeasurementEnriched> record = enrichedMeasurementsTopic.readRecord();
        SensorMeasurementEnriched event = record.getValue();
        assertEquals("Garden 1", event.gardenName);
        assertEquals(SensorMeasurementType.TEMPERATURE, event.type);
        assertEquals(4.5, event.value);
    }

    @Test
    public void testAggregatesMeasurementsByGardenName() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement1 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L);
        SensorMeasurement measurement2 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.0, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement1);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement2);

        // Then
        TestRecord<String, GardenStatus> record = gardenStatusEventsTopic.readRecord();
        String gardenName = record.getKey();
        assertEquals("Garden 1", gardenName);
    }

    @Test
    public void testGardenStatusKeepsLatestValue() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement1 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L);
        SensorMeasurement measurement2 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.0, 10L);
        SensorMeasurement measurement3 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 6.0, 10L);

        WindowStore<String, GardenStatus> windowStore = testDriver.getWindowStore("garden-status-store");

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement1, 10L);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement2, 11L);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement3, 12L);

        // Then
        KeyValueIterator<Windowed<String>, GardenStatus> events = windowStore.fetchAll(0, 20L);

        GardenStatus event = events.next().value;
        assertEquals(6.0, event.temperature);
    }

    @Test
    public void testGardenStatusUpdatesTrend() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement1 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L);
        SensorMeasurement measurement2 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.0, 10L);
        SensorMeasurement measurement3 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 6.0, 10L);

        WindowStore<String, GardenStatus> windowStore = testDriver.getWindowStore("garden-status-store");

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement1, 10L);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement2, 11L);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement3, 12L);

        // Then
        KeyValueIterator<Windowed<String>, GardenStatus> events = windowStore.fetchAll(0, 20L);

        GardenStatus event = events.next().value;
        assertEquals(GardenMeasurementTrend.UP, event.temperatureTrend);
    }

    @Test
    public void testWritesToGardenStatusTopic() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement1 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement1, 10L);

        // Then
        assertFalse(gardenStatusEventsTopic.isEmpty());
    }

}
