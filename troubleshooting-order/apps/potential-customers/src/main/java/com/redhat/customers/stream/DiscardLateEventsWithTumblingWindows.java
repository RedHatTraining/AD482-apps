package com.redhat.customers.stream;

import com.redhat.customers.event.PotentialCustomersWereDetected;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import java.time.Duration;

@ApplicationScoped
public class DiscardLateEventsWithTumblingWindows extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(DiscardLateEventsWithTumblingWindows.class);

    // Reading topic
    static final String POTENTIAL_CUSTOMERS_TOPIC = "potential-customers-detected";
    static final int WINDOW_SIZE = 10;

    @Produces
    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<PotentialCustomersWereDetected> customersEventSerde
                = new ObjectMapperSerde<>(PotentialCustomersWereDetected.class);

        // TODO: Build the stream topology

        streams = new KafkaStreams(
                builder.build(),
                generateStreamConfig()
        );

        streams.start();
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        streams.close();
    }
}
