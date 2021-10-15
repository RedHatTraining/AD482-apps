package com.redhat.garden;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redhat.garden.entities.GardenMeasurementTrend;
import com.redhat.garden.entities.GardenStatus;
import com.redhat.garden.entities.Sensor;
import com.redhat.garden.entities.SensorMeasurement;
import com.redhat.garden.entities.SensorMeasurementType;
import com.redhat.garden.events.DryConditionsDetected;
import com.redhat.garden.events.StrongWindDetected;
import com.redhat.garden.events.LowTemperatureDetected;

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


public class GardenStreamsTopologyBuilderTest {

    TopologyTestDriver testDriver;

    TestInputTopic<Integer, Sensor> sensorsTopic;
    ObjectMapperSerde<Sensor> sensorSerde;

    TestInputTopic<Integer, SensorMeasurement> sensorMeasurementsTopic;
    ObjectMapperSerde<SensorMeasurement> sensorMeasurementSerde;

    TestOutputTopic<Integer, LowTemperatureDetected> lowTemperatureEventsTopic;
    ObjectMapperSerde<LowTemperatureDetected> lowTemperatureEventSerde;

    TestOutputTopic<Integer, DryConditionsDetected> dryConditionsEventsTopic;
    ObjectMapperSerde<DryConditionsDetected> dryConditionsEventSerde;

    TestOutputTopic<Integer, StrongWindDetected> strongWindEventsTopic;
    ObjectMapperSerde<StrongWindDetected> strongWindEventSerde;

    TestOutputTopic<String, GardenStatus> gardenStatusEventsTopic;
    ObjectMapperSerde<GardenStatus> gardenStatusEventSerde;

    @BeforeEach
    public void setup() {
        GardenStreamsTopologyBuilder topologyBuilder = new GardenStreamsTopologyBuilder();
        testDriver = new TopologyTestDriver(topologyBuilder.build());

        sensorSerde = new ObjectMapperSerde<>(Sensor.class);
        sensorsTopic = testDriver.createInputTopic(
                    GardenStreamsTopologyBuilder.SENSORS_TOPIC,
                    new IntegerSerializer(),
                    sensorSerde.serializer());

        sensorMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurement.class);
        sensorMeasurementsTopic = testDriver.createInputTopic(
                    GardenStreamsTopologyBuilder.SENSOR_MEASUREMENTS_TOPIC,
                    new IntegerSerializer(),
                    sensorMeasurementSerde.serializer());

        lowTemperatureEventSerde = new ObjectMapperSerde<>(LowTemperatureDetected.class);
        lowTemperatureEventsTopic = testDriver.createOutputTopic(
            GardenStreamsTopologyBuilder.LOW_TEMPERATURE_EVENTS_TOPIC,
            new IntegerDeserializer(),
            lowTemperatureEventSerde.deserializer());

        dryConditionsEventSerde = new ObjectMapperSerde<>(DryConditionsDetected.class);
        dryConditionsEventsTopic = testDriver.createOutputTopic(
            GardenStreamsTopologyBuilder.LOW_HUMIDITY_EVENTS_TOPIC,
            new IntegerDeserializer(),
            dryConditionsEventSerde.deserializer());

        strongWindEventSerde = new ObjectMapperSerde<>(StrongWindDetected.class);
        strongWindEventsTopic = testDriver.createOutputTopic(
            GardenStreamsTopologyBuilder.STRONG_WIND_EVENTS_TOPIC,
            new IntegerDeserializer(),
            strongWindEventSerde.deserializer());

        gardenStatusEventSerde = new ObjectMapperSerde<>(GardenStatus.class);
        gardenStatusEventsTopic = testDriver.createOutputTopic(
            GardenStreamsTopologyBuilder.GARDEN_STATUS_EVENTS_TOPIC,
            new StringDeserializer(),
            gardenStatusEventSerde.deserializer());
    }

    @AfterEach
    public void teardown() {
        testDriver.close();
        sensorMeasurementSerde.close();
        lowTemperatureEventSerde.close();
        dryConditionsEventSerde.close();
        strongWindEventSerde.close();
        gardenStatusEventSerde.close();
    }

    @Test
    public void testLowTemperatureConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.5, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowTemperatureDetected> record = lowTemperatureEventsTopic.readRecord();
        LowTemperatureDetected event = record.getValue();
        assertEquals(4.5, event.value);
    }

    @Test
    public void testGoodTemperatureConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 20.0, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(lowTemperatureEventsTopic.isEmpty());
    }

    @Test
    public void testDryConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.HUMIDITY, 0.1, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, DryConditionsDetected> record = dryConditionsEventsTopic.readRecord();
        DryConditionsDetected event = record.getValue();
        assertEquals(0.1, event.value);
    }

    @Test
    public void testGoodDryConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.HUMIDITY, 0.8, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(dryConditionsEventsTopic.isEmpty());
    }

    @Test
    public void testStrongWindConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.WIND, 15.0, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, StrongWindDetected> record = strongWindEventsTopic.readRecord();
        StrongWindDetected event = record.getValue();
        assertEquals(15.0, event.value);
    }

    @Test
    public void testCalmWindConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.WIND, 3.0, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(dryConditionsEventsTopic.isEmpty());
    }


    @Test
    public void testLowTemperatureEventIncludesSensorMetadata() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.5, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowTemperatureDetected> record = lowTemperatureEventsTopic.readRecord();
        LowTemperatureDetected event = record.getValue();
        assertEquals("Garden 1", event.gardenName);

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
