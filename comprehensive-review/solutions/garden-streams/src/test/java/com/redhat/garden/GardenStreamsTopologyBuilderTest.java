package com.redhat.garden;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

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
    ObjectMapperSerde<LowTemperatureDetected> lowTemperatureEventsSerde;

    @BeforeEach
    public void setup() {
        GardenStreamsTopologyBuilder topologyBuilder = new GardenStreamsTopologyBuilder();
        testDriver = new TopologyTestDriver(topologyBuilder.build());

        sensorMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurement.class);
        sensorMeasurementsTopic = testDriver.createInputTopic(
                    GardenStreamsTopologyBuilder.SENSOR_MEASUREMENTS_TOPIC,
                    new IntegerSerializer(),
                    sensorMeasurementSerde.serializer());

        lowTemperatureEventsSerde = new ObjectMapperSerde<>(LowTemperatureDetected.class);
        lowTemperatureEventsTopic = testDriver.createOutputTopic(
            GardenStreamsTopologyBuilder.LOW_TEMPERATURE_EVENTS_TOPIC,
            new IntegerDeserializer(),
            lowTemperatureEventsSerde.deserializer());
    }

    @AfterEach
    public void teardown() {
        testDriver.close();
        sensorMeasurementSerde.close();
        lowTemperatureEventsSerde.close();
    }

    @Test
    public void testXXX() {
        // Given
        SensorMeasurement measurement = new SensorMeasurement(1, "temperature", 4.5, new Date());

        // When
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowTemperatureDetected> record = lowTemperatureEventsTopic.readRecord();
        LowTemperatureDetected event = record.getValue();
        assertEquals(4.5, event.value);
    }

}
