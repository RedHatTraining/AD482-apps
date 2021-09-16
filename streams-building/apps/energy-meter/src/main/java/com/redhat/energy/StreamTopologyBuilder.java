package com.redhat.energy;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.redhat.energy.records.MWattsMeasurement;
import com.redhat.energy.records.WindTurbine;
import com.redhat.energy.records.WindTurbineStats;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class StreamTopologyBuilder {

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        // TODO: Create wind turbine serde

        // TODO: read the "turbines" topic as a KTable

        // TODO: read the "turbine-generated-watts" topic as a KStream

        // TODO: Create MWattsMeasurement serde

        // TODO: map the watts stream into a new mwatts stream

        // TODO: Create WindTurbineStats serde

        // TODO: count measurements by turbine and write results to a new stream

        return builder.build();
    }

}
