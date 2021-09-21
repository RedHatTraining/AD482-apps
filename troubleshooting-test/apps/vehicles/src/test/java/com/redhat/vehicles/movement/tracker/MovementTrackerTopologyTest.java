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

        // TODO: Create test input topics

        // TODO: Create test stores

        // TODO: Create test output topic
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }

    // TODO: test VehicleRegistered events create vehicles in the store

    // TODO: test VehicleMetrics counts VehicleMoved events for each vehicle

    // TODO: test VehicleMoved and VehicleRegistered events are joined and sent to output topic

}
