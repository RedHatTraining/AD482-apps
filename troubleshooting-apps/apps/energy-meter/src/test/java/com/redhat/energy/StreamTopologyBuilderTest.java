package com.redhat.energy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.redhat.energy.records.WindTurbineStats;
import com.redhat.energy.records.MWattsMeasurement;
import com.redhat.energy.records.WindTurbine;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.IntegerDeserializer;


public class StreamTopologyBuilderTest {

    TopologyTestDriver testDriver;
    TestInputTopic<Integer, Integer> wattsStream;
    TestInputTopic<Integer, WindTurbine> turbinesStream;
    TestOutputTopic<Integer, MWattsMeasurement> measurementsStream;
    TestOutputTopic<Integer, WindTurbineStats> statsStream;

    @BeforeEach
    public void setup() {
        // TODO: create the test driver

        // TODO: Create test topics
    }

    @AfterEach
    public void tearDown() {
        testDriver.close();
    }

    // TODO: implement test
}