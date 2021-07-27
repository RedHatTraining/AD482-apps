package com.redhat.vehicles.movement.tracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.redhat.vehicles.Vehicle;
import com.redhat.vehicles.movement.events.VehicleMoved;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;



class MovementTrackerTopologyTest {

    /**
     * Unit test the Movement tracker stream topology
     */

    VehicleMovementTracker tracker;
    TopologyTestDriver testDriver;
    ObjectMapperSerde<Vehicle> vehicleSerde;
    TestInputTopic<Integer, Vehicle> vehiclesTopic;
    KeyValueStore<Integer, Vehicle> vehiclesStore;
    TestInputTopic<Integer, VehicleMoved> vehicleMovementsTopic;
    ObjectMapperSerde<VehicleMoved> vehicleMovedSerde;
    KeyValueStore<Integer, VehicleMetrics> vehicleMetricsStore;

    @BeforeEach
    public void setup() {
        tracker = new VehicleMovementTracker();
        testDriver = new TopologyTestDriver(tracker.buildTopology());
        vehicleSerde = new ObjectMapperSerde<>(Vehicle.class);
        vehicleMovedSerde = new ObjectMapperSerde<>(VehicleMoved.class);
        vehiclesTopic = testDriver.createInputTopic("vehicles", new IntegerSerializer(), vehicleSerde.serializer());
        vehicleMovementsTopic = testDriver.createInputTopic("vehicle-movements", new IntegerSerializer(), vehicleMovedSerde.serializer());
        vehiclesStore = testDriver.getKeyValueStore("vehicles-store");
        vehicleMetricsStore = testDriver.getKeyValueStore("vehicle-metrics-store");
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
        vehicleSerde.close();
    }

    @Test
    public void testTopologySendsVehicleToStore() throws JsonProcessingException {
        // Given
        Vehicle vehicle = new Vehicle(12, "bike", "super bike");

        // When
        vehiclesTopic.pipeInput(vehicle.id, vehicle);

        // Then
        Vehicle vehicleInStore = vehiclesStore.get(12);
        assertEquals(vehicleInStore.id, 12);
    }


    @Test
    public void testVehicleMovementMetricsAreCalculated() throws JsonProcessingException {
        // Given
        Vehicle vehicle = new Vehicle(14, "car", "test");
        vehiclesStore.put(vehicle.id, vehicle);

        // When
        VehicleMoved event = new VehicleMoved(14, 0, 0, 0);
        vehicleMovementsTopic.pipeInput(event.vehicleId, event);


        // Then
        VehicleMetrics metrics = vehicleMetricsStore.get(14);
        assertEquals(14, metrics.vehicle.id);
    }


}
