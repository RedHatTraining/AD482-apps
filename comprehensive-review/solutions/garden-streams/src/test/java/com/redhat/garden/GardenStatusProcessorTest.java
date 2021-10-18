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

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.WindowStore;
import org.apache.kafka.streams.test.TestRecord;


public class GardenStatusProcessorTest {

    TopologyTestDriver testDriver;

    TestInputTopic<Integer, SensorMeasurementEnriched> enrichedMeasurementsTopic;
    ObjectMapperSerde<SensorMeasurementEnriched> enrichedMeasurementSerde;

    TestOutputTopic<String, GardenStatus> gardenStatusEventsTopic;
    ObjectMapperSerde<GardenStatus> gardenStatusEventSerde;


    @BeforeEach
    public void setup() {
        GardenStatusProcessor topologyBuilder = new GardenStatusProcessor();
        testDriver = new TopologyTestDriver(topologyBuilder.getTopology());

        enrichedMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurementEnriched.class);
        enrichedMeasurementsTopic = testDriver.createInputTopic(
                    RulesProcessor.ENRICHED_SENSOR_MEASUREMENTS_TOPIC,
                    new IntegerSerializer(),
                    enrichedMeasurementSerde.serializer());

        gardenStatusEventSerde = new ObjectMapperSerde<>(GardenStatus.class);
        gardenStatusEventsTopic = testDriver.createOutputTopic(
            GardenStatusProcessor.GARDEN_STATUS_EVENTS_TOPIC,
            new StringDeserializer(),
            gardenStatusEventSerde.deserializer());
    }

    @AfterEach
    public void teardown() {
        testDriver.close();
        enrichedMeasurementSerde.close();
        gardenStatusEventSerde.close();
    }

    @Test
    public void testAggregatesMeasurementsByGardenName() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurementEnriched measurement1 = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L),
            sensor);
        SensorMeasurementEnriched measurement2 = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.0, 10L),
            sensor);

        // When
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement1);
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement2);

        // Then
        TestRecord<String, GardenStatus> record = gardenStatusEventsTopic.readRecord();
        String gardenName = record.getKey();
        assertEquals("Garden 1", gardenName);
    }

    @Test
    public void testGardenStatusKeepsLatestValue() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurementEnriched measurement1 = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L),
            sensor);
        SensorMeasurementEnriched measurement2 = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.0, 10L),
            sensor);
        SensorMeasurementEnriched measurement3 = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 6.0, 10L),
            sensor);

        WindowStore<String, GardenStatus> windowStore = testDriver.getWindowStore("garden-status-store");

        // When
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement1, 10L);
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement2, 11L);
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement3, 12L);

        // Then
        KeyValueIterator<Windowed<String>, GardenStatus> events = windowStore.fetchAll(0, 20L);

        GardenStatus event = events.next().value;
        assertEquals(6.0, event.temperature);
    }

    @Test
    public void testGardenStatusUpdatesTrend() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurementEnriched measurement1 = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L),
            sensor);
        SensorMeasurementEnriched measurement2 = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.0, 10L),
            sensor);
        SensorMeasurementEnriched measurement3 = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 6.0, 10L),
            sensor);

        WindowStore<String, GardenStatus> windowStore = testDriver.getWindowStore("garden-status-store");

        // When
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement1, 10L);
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement2, 11L);
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement3, 12L);

        // Then
        KeyValueIterator<Windowed<String>, GardenStatus> events = windowStore.fetchAll(0, 20L);

        GardenStatus event = events.next().value;
        assertEquals(GardenMeasurementTrend.UP, event.temperatureTrend);
    }

    @Test
    public void testWritesToGardenStatusTopic() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurementEnriched measurement = new SensorMeasurementEnriched(
            new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L),
            sensor);

        // When
        enrichedMeasurementsTopic.pipeInput(sensor.id, measurement, 10L);

        // Then
        assertFalse(gardenStatusEventsTopic.isEmpty());
    }

}
