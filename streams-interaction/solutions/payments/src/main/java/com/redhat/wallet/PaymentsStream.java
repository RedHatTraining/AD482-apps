package com.redhat.wallet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;


@ApplicationScoped
public class PaymentsStream {

    // Deserializer for message keys.
    private final Serde<String> keySerde = Serdes.String();

    // Serializer for message values
    private final Serde<Integer> valueSerde = Serdes.Integer();

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Integer> stream = builder.stream("payments", Consumed.with(keySerde, valueSerde));

        stream.foreach((key, amount) -> System.out.println("Received payment: " + amount));

        stream
            .filter((key, amount) -> amount > 1000)
            .to("large-payments", Produced.with(keySerde, valueSerde));

        return builder.build();
    }

}
