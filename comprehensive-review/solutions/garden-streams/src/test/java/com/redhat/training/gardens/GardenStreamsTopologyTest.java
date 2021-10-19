package com.redhat.training.gardens;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redhat.training.gardens.model.Sensor;
import com.redhat.training.gardens.model.SensorMeasurement;
import com.redhat.training.gardens.model.SensorMeasurementEnriched;
import com.redhat.training.gardens.model.SensorMeasurementType;
import com.redhat.training.gardens.model.GardenMeasurementTrend;
import com.redhat.training.gardens.model.GardenStatus;
import com.redhat.training.gardens.event.LowHumidityDetected;
import com.redhat.training.gardens.event.LowTemperatureDetected;
import com.redhat.training.gardens.event.StrongWindDetected;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.WindowStore;

public class GardenStreamsTopologyTest {

    TopologyTestDriver testDriver;

    TestInputTopic<Integer, Sensor> sensorsTopic;
    ObjectMapperSerde<Sensor> sensorSerde;

    TestInputTopic<Integer, SensorMeasurement> sensorMeasurementsTopic;
    ObjectMapperSerde<SensorMeasurement> sensorMeasurementSerde;

    TestOutputTopic<Integer, SensorMeasurementEnriched> enrichedMeasurementsTopic;
    ObjectMapperSerde<SensorMeasurementEnriched> enrichedMeasurementSerde;

    TestOutputTopic<String, GardenStatus> gardenStatusEventsTopic;
    ObjectMapperSerde<GardenStatus> gardenStatusEventSerde;

    TestOutputTopic<Integer, LowTemperatureDetected> lowTemperatureEventsTopic;
    ObjectMapperSerde<LowTemperatureDetected> lowTemperatureEventSerde;

    TestOutputTopic<Integer, LowHumidityDetected> dryConditionsEventsTopic;
    ObjectMapperSerde<LowHumidityDetected> dryConditionsEventSerde;

    TestOutputTopic<Integer, StrongWindDetected> strongWindEventsTopic;
    ObjectMapperSerde<StrongWindDetected> strongWindEventSerde;

    @BeforeEach
    public void setup() {
        GardenStreamsTopology topology = new GardenStreamsTopology();
        testDriver = new TopologyTestDriver(topology.build());

        sensorSerde = new ObjectMapperSerde<>(Sensor.class);
        sensorsTopic = testDriver.createInputTopic("garden-sensors", new IntegerSerializer(), sensorSerde.serializer());

        sensorMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurement.class);
        sensorMeasurementsTopic = testDriver.createInputTopic("garden-sensor-measurements-repl",
                new IntegerSerializer(), sensorMeasurementSerde.serializer());

        enrichedMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurementEnriched.class);
        enrichedMeasurementsTopic = testDriver.createOutputTopic("garden-sensor-measurements-enriched",
                new IntegerDeserializer(), enrichedMeasurementSerde.deserializer());

        lowTemperatureEventSerde = new ObjectMapperSerde<>(LowTemperatureDetected.class);
        lowTemperatureEventsTopic = testDriver.createOutputTopic("garden-low-temperature-events",
                new IntegerDeserializer(), lowTemperatureEventSerde.deserializer());

        dryConditionsEventSerde = new ObjectMapperSerde<>(LowHumidityDetected.class);
        dryConditionsEventsTopic = testDriver.createOutputTopic("garden-low-humidity-events",
                new IntegerDeserializer(), dryConditionsEventSerde.deserializer());

        strongWindEventSerde = new ObjectMapperSerde<>(StrongWindDetected.class);
        strongWindEventsTopic = testDriver.createOutputTopic("garden-strong-wind-events",
                new IntegerDeserializer(), strongWindEventSerde.deserializer());

        gardenStatusEventSerde = new ObjectMapperSerde<>(GardenStatus.class);
        gardenStatusEventsTopic = testDriver.createOutputTopic("garden-status-events", new StringDeserializer(),
                gardenStatusEventSerde.deserializer());
    }

    @AfterEach
    public void teardown() {
        testDriver.close();
        sensorMeasurementSerde.close();
        enrichedMeasurementSerde.close();
        lowTemperatureEventSerde.close();
        dryConditionsEventSerde.close();
        strongWindEventSerde.close();
        gardenStatusEventSerde.close();
    }

    @Test
    public void testWritesEnrichedStreamToTopic() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.5, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertFalse(enrichedMeasurementsTopic.isEmpty());
    }

    @Test
    public void testEnrichedMeasurementIncludeGardenName() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.5, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, SensorMeasurementEnriched> record = enrichedMeasurementsTopic.readRecord();
        SensorMeasurementEnriched event = record.getValue();
        assertEquals("Garden 1", event.gardenName);
    }

    @Test
    public void testEnrichedMeasurementIncludeMeasurementType() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.5, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, SensorMeasurementEnriched> record = enrichedMeasurementsTopic.readRecord();
        SensorMeasurementEnriched event = record.getValue();
        assertEquals(SensorMeasurementType.TEMPERATURE, event.type);
    }

    @Test
    public void testEnrichedMeasurementIncludesValue() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.5, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, SensorMeasurementEnriched> record = enrichedMeasurementsTopic.readRecord();
        SensorMeasurementEnriched event = record.getValue();
        assertEquals(4.5, event.value);
    }

    @Test
    public void testLowTemperatureConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
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
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 20, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(lowTemperatureEventsTopic.isEmpty());
    }

    @Test
    public void testDryConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Humidity", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.HUMIDITY, 0.1, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        TestRecord<Integer, LowHumidityDetected> record = dryConditionsEventsTopic.readRecord();
        LowHumidityDetected event = record.getValue();
        assertEquals(0.1, event.value);
    }

    @Test
    public void testGoodHumidityConditions() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Humidity", "Customer 1", "Garden 1");
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
        Sensor sensor = new Sensor(1, "Sensor 1", "Wind", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.WIND, 15, 10L);

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
        Sensor sensor = new Sensor(1, "Sensor 1", "Wind", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.WIND, 3, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(measurement.sensorId, measurement);

        // Then
        assertTrue(dryConditionsEventsTopic.isEmpty());
    }

    @Test
    public void testGeneratedEventsIncludesSensorMetadata() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
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
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
        SensorMeasurement measurement1 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L);
        SensorMeasurement measurement2 = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 4.0, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement1);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement2);

        // Then
        TestRecord<String, GardenStatus> record = gardenStatusEventsTopic.readRecord();
        GardenStatus status = record.getValue();
        assertEquals("Garden 1", status.gardenName);
    }

    @Test
    public void testGardenStatusKeepsLatestValue() {
        // Given
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
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
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
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
        Sensor sensor = new Sensor(1, "Sensor 1", "Temperature", "Customer 1", "Garden 1");
        SensorMeasurement measurement = new SensorMeasurement(1, SensorMeasurementType.TEMPERATURE, 2.0, 10L);

        // When
        sensorsTopic.pipeInput(sensor.id, sensor);
        sensorMeasurementsTopic.pipeInput(sensor.id, measurement, 10L);

        // Then
        assertFalse(gardenStatusEventsTopic.isEmpty());
    }

}
