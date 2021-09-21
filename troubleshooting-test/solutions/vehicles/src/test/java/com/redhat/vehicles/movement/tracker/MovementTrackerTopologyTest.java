package com.redhat.vehicles.movement.tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redhat.vehicles.events.VehicleMoved;
import com.redhat.vehicles.events.VehicleRegistered;
import com.redhat.vehicles.inventory.Vehicle;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.test.TestRecord;



class MovementTrackerTopologyTest {

    /**
     * Unit test the Movement tracker stream topology
     */

    VehicleMovementTracker tracker;
    TopologyTestDriver testDriver;
    ObjectMapperSerde<VehicleRegistered> vehicleRegisteredSerde;
    TestInputTopic<Integer, VehicleRegistered> vehicleRegisteredTopic;
    KeyValueStore<Integer, Vehicle> vehiclesStore;
    TestInputTopic<Integer, VehicleMoved> vehicleMovedTopic;
    ObjectMapperSerde<VehicleMoved> vehicleMovedSerde;
    KeyValueStore<Integer, VehicleMetrics> vehicleMetricsStore;
    TestOutputTopic<Integer, VehicleStatus> vehicleStatusTopic;
    ObjectMapperSerde<VehicleStatus> vehicleStatusSerde;

    @BeforeEach
    public void setup() {
        // TODO: pass the VehicleMovementTracker topology to the test driver
        tracker = new VehicleMovementTracker();
        testDriver = new TopologyTestDriver(tracker.buildTopology());

        // TODO: Create test  topics
        vehicleRegisteredSerde = new ObjectMapperSerde<>(VehicleRegistered.class);
        vehicleMovedSerde = new ObjectMapperSerde<>(VehicleMoved.class);
        vehicleRegisteredTopic = testDriver.createInputTopic(
            "vehicle-registered",
            new IntegerSerializer(),
            vehicleRegisteredSerde.serializer()
        );
        vehicleMovedTopic = testDriver.createInputTopic(
            "vehicle-moved",
            new IntegerSerializer(),
            vehicleMovedSerde.serializer()
        );

        // TODO: Create test stores
        vehiclesStore = testDriver.getKeyValueStore("vehicles-store");
        vehicleMetricsStore = testDriver.getKeyValueStore("vehicle-metrics-store");

        // TODO: Create test output topic
        vehicleStatusSerde = new ObjectMapperSerde<>(VehicleStatus.class);
        vehicleStatusTopic = testDriver.createOutputTopic(
            "vehicle-status",
            new IntegerDeserializer(),
            vehicleStatusSerde.deserializer()
        );
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }

    // TODO: test VehicleRegistered events create vehicles in the store
    @Test
    public void testTopologySendsVehiclesToStore() throws JsonProcessingException {
        // Given
        VehicleRegistered event = new VehicleRegistered(12, "bike", "super bike");

        // When
        vehicleRegisteredTopic.pipeInput(event.id, event);

        // Then
        Vehicle vehicleInStore = vehiclesStore.get(12);
        assertEquals(vehicleInStore.id, 12);
    }

    // TODO: test VehicleMetrics counts VehicleMoved events for each vehicle
    @Test
    public void testVehicleMovementsCountEqualsEventsCount() throws JsonProcessingException {

        // Given
        Vehicle vehicle = new Vehicle(14, "car", "test");
        vehiclesStore.put(vehicle.id, vehicle);
        VehicleMoved event1 = new VehicleMoved(14, 0, 0, 5);
        VehicleMoved event2 = new VehicleMoved(14, 0, 0, 10);

        // When
        vehicleMovedTopic.pipeInput(event1.vehicleId, event1);
        vehicleMovedTopic.pipeInput(event2.vehicleId, event2);

        // Then
        VehicleMetrics metrics = vehicleMetricsStore.get(14);
        assertEquals(2, metrics.movementsReported);
    }

    // TODO: test VehicleMoved and VehicleRegistered events are joined and sent to output topic
    @Test
    public void testVehicleMovementsStreamIsEnriched() throws JsonProcessingException {

        // Given
        VehicleRegistered vehicleRegistered = new VehicleRegistered(111, "car", "test car");
        VehicleMoved vehicleMoved = new VehicleMoved(111, 0, 0, 5);

        // When
        vehicleRegisteredTopic.pipeInput(vehicleRegistered.id, vehicleRegistered);
        vehicleMovedTopic.pipeInput(vehicleMoved.vehicleId, vehicleMoved);

        // Then
        TestRecord<Integer, VehicleStatus> record = vehicleStatusTopic.readRecord();
        assertEquals(111, record.getKey());
        assertEquals(111, record.getValue().vehicle.id);
        assertEquals(5, record.getValue().elevation);
    }

}
