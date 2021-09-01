package com.redhat.training.bank.stream;

import com.redhat.training.bank.event.AmountWasDeposited;
import com.redhat.training.bank.event.HighValueDepositWasDetected;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AmountWasDepositedPipeline extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(AmountWasDepositedPipeline.class);

    // Reading topic
    static final String AMOUNT_WAS_DEPOSITED_TOPIC = "bank-account-deposit";

    // Writing topic
    static final String HIGH_VALUE_DEPOSIT_TOPIC = "high-value-deposit-alert";

    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<AmountWasDeposited> depositEventSerde
                = new ObjectMapperSerde<>(AmountWasDeposited.class);

        ObjectMapperSerde<HighValueDepositWasDetected> highValueEventSerde
                = new ObjectMapperSerde<>(HighValueDepositWasDetected.class);

        // TODO: Build the stream topology

        // TODO: Create a Kafka streams and start it
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        // TODO: Close the stream on shutdown
    }
}
