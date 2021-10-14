package com.redhat.garden;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import com.redhat.garden.events.DryConditionsDetected;
import com.redhat.garden.events.LowNutrientsDetected;
import com.redhat.garden.events.LowTemperatureDetected;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
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

    TestOutputTopic<Integer, LowNutrientsDetected> lowNutrientsEventsTopic;
    ObjectMapperSerde<LowNutrientsDetected> lowNutrientsEventSerde;

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
            GardenStreamsTopologyBuilder.DRY_CONDITIONS_EVENTS_TOPIC,
            new IntegerDeserializer(),
            dryConditionsEventSerde.deserializer());

        lowNutrientsEventSerde = new ObjectMapperSerde<>(LowNutrientsDetected.class);
        lowNutrientsEventsTopic = testDriver.createOutputTopic(
            GardenStreamsTopologyBuilder.LOW_NUTRIENTS_EVENTS_TOPIC,
            new IntegerDeserializer(),
            lowNutrientsEventSerde.deserializer());
    }

    @AfterEach
    public void teardown() {
        testDriver.close();
        sensorMeasurementSerde.close();
        lowTemperatureEventSerde.close();
    }

    @Test
    public void testLowTemperatureConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, "temperature", 4.5, new Date());

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowTemperatureDetected> record = lowTemperatureEventsTopic.readRecord();
        LowTemperatureDetected event = record.getValue();
        assertEquals(4.5, event.value);
    }

    @Test
    public void testDryConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, "humidity", 0.1, new Date());

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
        SensorMeasurement measurement = new SensorMeasurement(1, "humidity", 0.8, new Date());

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(dryConditionsEventsTopic.isEmpty());
    }

    @Test
    public void testLowNutrientsLevel() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, "nutrients", 0.2, new Date());

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowNutrientsDetected> record = lowNutrientsEventsTopic.readRecord();
        LowNutrientsDetected event = record.getValue();
        assertEquals(0.2, event.value);
    }

    @Test
    public void testGoodNutrientsLevel() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, "nutrients", 0.8, new Date());

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
        SensorMeasurement measurement = new SensorMeasurement(1, "temperature", 4.5, new Date());

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowTemperatureDetected> record = lowTemperatureEventsTopic.readRecord();
        LowTemperatureDetected event = record.getValue();
        assertEquals("Garden 1", event.gardenName);

    }

}
