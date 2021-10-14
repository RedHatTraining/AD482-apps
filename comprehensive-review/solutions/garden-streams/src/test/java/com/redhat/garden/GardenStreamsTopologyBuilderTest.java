package com.redhat.garden;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import com.redhat.garden.events.DryConditionsDetected;
import com.redhat.garden.events.LowTemperatureDetected;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;


public class GardenStreamsTopologyBuilderTest {

    TopologyTestDriver testDriver;
    TestInputTopic<Integer, SensorMeasurement> sensorMeasurementsTopic;
    ObjectMapperSerde<SensorMeasurement> sensorMeasurementSerde;

    TestOutputTopic<Integer, LowTemperatureDetected> lowTemperatureEventsTopic;
    ObjectMapperSerde<LowTemperatureDetected> lowTemperatureEventSerde;

    TestOutputTopic<Integer, DryConditionsDetected> dryConditionsEventsTopic;
    ObjectMapperSerde<DryConditionsDetected> dryConditionsEventSerde;

    @BeforeEach
    public void setup() {
        GardenStreamsTopologyBuilder topologyBuilder = new GardenStreamsTopologyBuilder();
        testDriver = new TopologyTestDriver(topologyBuilder.build());

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
        SensorMeasurement measurement = new SensorMeasurement(1, "temperature", 4.5, new Date());

        // When
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowTemperatureDetected> record = lowTemperatureEventsTopic.readRecord();
        LowTemperatureDetected event = record.getValue();
        assertEquals(4.5, event.value);
    }

    @Test
    public void testDryConditions() {
        // Given
        SensorMeasurement measurement = new SensorMeasurement(1, "humidity", 0.1, new Date());

        // When
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, DryConditionsDetected> record = dryConditionsEventsTopic.readRecord();
        DryConditionsDetected event = record.getValue();
        assertEquals(0.1, event.value);
    }

    @Test
    public void testGoodDryConditions() {
        // Given
        SensorMeasurement measurement = new SensorMeasurement(1, "humidity", 0.8, new Date());

        // When
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(dryConditionsEventsTopic.isEmpty());
    }

}
