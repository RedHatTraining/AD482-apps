package com.redhat.wallet;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;


@ApplicationScoped
public class MoneyTransfersStream {

    private static final String TOPIC = "money-transfers";
    private final Serde<String> keySerde = Serdes.String();
    private final ObjectMapperSerde<MoneyTransferred> valueSerde = new ObjectMapperSerde<>(MoneyTransferred.class);

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, MoneyTransferred> stream = builder.stream(TOPIC, Consumed.with(keySerde, valueSerde));

        stream
            .map((key, event) -> toMoneyTransferFormatted(key, event))
            .to("formatted-transfer-amounts", Produced.with(keySerde, Serdes.String()));

        return builder.build();
    }


    private KeyValue<String, String> toMoneyTransferFormatted(String key, MoneyTransferred event) {
        String formatted = String.format(
            "Transfer from wallet [%d] to wallet [%d]: %,.2f %s",
            event.sourceWalletId, event.destinationWalletId,
            event.amount, event.currency
        );
        return new KeyValue<>(key, formatted);
    }


}
