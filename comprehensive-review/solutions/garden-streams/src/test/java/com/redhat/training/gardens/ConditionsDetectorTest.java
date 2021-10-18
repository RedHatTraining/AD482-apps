package com.redhat.training.gardens;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redhat.training.gardens.event.DryConditionsDetected;
import com.redhat.training.gardens.event.LowTemperatureDetected;
import com.redhat.training.gardens.event.StrongWindDetected;
import com.redhat.training.gardens.model.Sensor;
import com.redhat.training.gardens.model.SensorMeasurement;
import com.redhat.training.gardens.model.SensorMeasurementEnriched;
import com.redhat.training.gardens.model.MeasureType;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;


public class ConditionsDetectorTest {

    TopologyTestDriver testDriver;

    TestInputTopic<Integer, SensorMeasurementEnriched> enrichedMeasurementsTopic;
    ObjectMapperSerde<SensorMeasurementEnriched> sensorMeasurementSerde;

    TestOutputTopic<Integer, LowTemperatureDetected> lowTemperatureEventsTopic;
    ObjectMapperSerde<LowTemperatureDetected> lowTemperatureEventSerde;

    TestOutputTopic<Integer, DryConditionsDetected> dryConditionsEventsTopic;
    ObjectMapperSerde<DryConditionsDetected> dryConditionsEventSerde;

    TestOutputTopic<Integer, StrongWindDetected> strongWindEventsTopic;
    ObjectMapperSerde<StrongWindDetected> strongWindEventSerde;


    @BeforeEach
    public void setup() {
        ConditionsDetector processor = new ConditionsDetector();
        testDriver = new TopologyTestDriver(processor.getTopology());

        sensorMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurementEnriched.class);
        enrichedMeasurementsTopic = testDriver.createInputTopic(
                    MeasurementsEnricher.ENRICHED_SENSOR_MEASUREMENTS_TOPIC,
                    new IntegerSerializer(),
                    sensorMeasurementSerde.serializer());

        lowTemperatureEventSerde = new ObjectMapperSerde<>(LowTemperatureDetected.class);
        lowTemperatureEventsTopic = testDriver.createOutputTopic(
            ConditionsDetector.LOW_TEMPERATURE_EVENTS_TOPIC,
            new IntegerDeserializer(),
            lowTemperatureEventSerde.deserializer());

        dryConditionsEventSerde = new ObjectMapperSerde<>(DryConditionsDetected.class);
        dryConditionsEventsTopic = testDriver.createOutputTopic(
            ConditionsDetector.LOW_HUMIDITY_EVENTS_TOPIC,
            new IntegerDeserializer(),
            dryConditionsEventSerde.deserializer());

        strongWindEventSerde = new ObjectMapperSerde<>(StrongWindDetected.class);
        strongWindEventsTopic = testDriver.createOutputTopic(
            ConditionsDetector.STRONG_WIND_EVENTS_TOPIC,
            new IntegerDeserializer(),
            strongWindEventSerde.deserializer());
    }

    @AfterEach
    public void teardown() {
        testDriver.close();
        sensorMeasurementSerde.close();
        lowTemperatureEventSerde.close();
        dryConditionsEventSerde.close();
        strongWindEventSerde.close();
    }

    @Test
    public void testLowTemperatureConditions() {
        // Given
        SensorMeasurementEnriched measurement = new SensorMeasurementEnriched(
            new SensorMeasurement(1, MeasureType.TEMPERATURE, 4.5, 10L),
            new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1"));

        // When
        enrichedMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowTemperatureDetected> record = lowTemperatureEventsTopic.readRecord();
        LowTemperatureDetected event = record.getValue();
        assertEquals(4.5, event.value);
    }

    @Test
    public void testGoodTemperatureConditions() {
        // Given
        SensorMeasurementEnriched measurement = new SensorMeasurementEnriched(
            new SensorMeasurement(1, MeasureType.TEMPERATURE, 20.0, 10L),
            new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1"));

        // When
        enrichedMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(lowTemperatureEventsTopic.isEmpty());
    }

    @Test
    public void testDryConditions() {
        // Given
        SensorMeasurementEnriched measurement = new SensorMeasurementEnriched(
            new SensorMeasurement(1, MeasureType.HUMIDITY, 0.1, 10L),
            new Sensor(1, "Sensor 1", "Humidity", "Customer 1", "Garden 1"));

        // When
        enrichedMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, DryConditionsDetected> record = dryConditionsEventsTopic.readRecord();
        DryConditionsDetected event = record.getValue();
        assertEquals(0.1, event.value);
    }

    @Test
    public void testGoodHumidityConditions() {
        // Given
        SensorMeasurementEnriched measurement = new SensorMeasurementEnriched(
            new SensorMeasurement(1, MeasureType.HUMIDITY, 0.8, 10L),
            new Sensor(1, "Sensor 1", "Humidity", "Customer 1", "Garden 1"));

        // When
        enrichedMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(dryConditionsEventsTopic.isEmpty());
    }

    @Test
    public void testStrongWindConditions() {
        // Given
        SensorMeasurementEnriched measurement = new SensorMeasurementEnriched(
            new SensorMeasurement(1, MeasureType.WIND, 15.0, 10L),
            new Sensor(1, "Sensor 1", "Wind", "Customer 1", "Garden 1"));

        // When
        enrichedMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, StrongWindDetected> record = strongWindEventsTopic.readRecord();
        StrongWindDetected event = record.getValue();
        assertEquals(15.0, event.value);
    }

    @Test
    public void testCalmWindConditions() {
        // Given
        SensorMeasurementEnriched measurement = new SensorMeasurementEnriched(
            new SensorMeasurement(1, MeasureType.WIND, 3.0, 10L),
            new Sensor(1, "Sensor 1", "Wind", "Customer 1", "Garden 1"));

        // When
        enrichedMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(dryConditionsEventsTopic.isEmpty());
    }

    @Test
    public void testGeneratedEventsIncludesSensorMetadata() {
        // Given
        SensorMeasurementEnriched measurement = new SensorMeasurementEnriched(
            new SensorMeasurement(1, MeasureType.TEMPERATURE, 4.5, 10L),
            new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1"));

        // When
        enrichedMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowTemperatureDetected> record = lowTemperatureEventsTopic.readRecord();
        LowTemperatureDetected event = record.getValue();
        assertEquals("Garden 1", event.gardenName);
    }

}
