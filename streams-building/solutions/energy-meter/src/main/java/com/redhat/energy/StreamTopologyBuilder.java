package com.redhat.energy;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class StreamTopologyBuilder {

    // Deserializer for NULL keys.
    private final Serde<String> stringSerde = Serdes.String();

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        // TODO:
        ObjectMapperSerde<WindTurbine> turbineSerde = new ObjectMapperSerde<>(WindTurbine.class);

        // TODO:
        KTable<String, WindTurbine> turbines = builder.table(
            "turbines",
            Consumed.with(stringSerde, turbineSerde),
            Materialized.as("turbinesStore")
        );

        // TODO:
        KStream<Integer, Double> powerValuesStream = builder.stream(
            "turbines-production-values",
            Consumed.with(Serdes.Integer(), Serdes.Double())
        );

        powerValuesStream.map((turbineId, watts) -> {
            Double capacity = watts / 1000000;
            return KeyValue.pair(turbineId, capacity);
        })
        .to("turbines-capacity-values");


        powerValuesStream
            .groupByKey()
            .count()
            .toStream()
            .to("turbines-reported-values");

        return builder.build();
    }

}
