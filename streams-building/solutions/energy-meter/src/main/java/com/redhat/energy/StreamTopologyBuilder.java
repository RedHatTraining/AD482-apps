package com.redhat.energy;

import java.io.Console;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class StreamTopologyBuilder {


    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        // TODO:
        ObjectMapperSerde<WindTurbine> turbineSerde = new ObjectMapperSerde<>(WindTurbine.class);
        ObjectMapperSerde<PowerMeasurement> powerMeasurementSerde = new ObjectMapperSerde<>(PowerMeasurement.class);

        // TODO:
        KTable<Integer, WindTurbine> turbines = builder.table(
            "turbines",
            Consumed.with(Serdes.Integer(), turbineSerde),
            Materialized.<Integer, WindTurbine, KeyValueStore<Bytes, byte[]>>as("turbinesStore1")
                .withKeySerde(Serdes.Integer())
                .withValueSerde(turbineSerde)
        );

        // // TODO:
        // GlobalKTable<String, WindTurbine> turbinesg = builder.globalTable( 
        //     "turbines",
        //     Consumed.with(stringSerde, turbineSerde),
        //     Materialized.as("turbinesStore")
        // );
 

        // TODO:
        KStream<Integer, Integer> powerValuesStream = builder.stream(
            "turbine-power-generation",
            Consumed.with(Serdes.Integer(), Serdes.Integer())
        );

        // TODO: convert to megawatts
        powerValuesStream.map((turbineId, watts) -> {
            Double megawatts = (double) watts / 10;
            PowerMeasurement measurement = new PowerMeasurement(turbineId, megawatts);
            System.out.println("capacity " + megawatts);
            return KeyValue.pair(turbineId, measurement);
        }).to(
            "turbines-generated-mwatts",
            Produced.with(Serdes.Integer(), powerMeasurementSerde)
        );



        // powerValuesStream
        //     .groupByKey()
        //     .count()
        //     .toStream()
        //     .to("turbines-reported-values");

        return builder.build();
    }

}
