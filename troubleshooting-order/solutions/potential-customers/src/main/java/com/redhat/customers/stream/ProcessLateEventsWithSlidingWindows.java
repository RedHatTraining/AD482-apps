package com.redhat.customers.stream;

import com.redhat.customers.event.PotentialCustomersAverageWasCalculated;
import com.redhat.customers.event.PotentialCustomersWereDetected;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import java.time.Duration;

@ApplicationScoped
public class ProcessLateEventsWithSlidingWindows extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(ProcessLateEventsWithSlidingWindows.class);

    // Reading topic
    static final String POTENTIAL_CUSTOMERS_TOPIC = "potential-customers-detected";

    // Writing topic
    static final String AVERAGE_CUSTOMERS_TOPIC = "potential-customers-average";

    @Produces
    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<PotentialCustomersWereDetected> customersEventSerde
                = new ObjectMapperSerde<>(PotentialCustomersWereDetected.class);

        ObjectMapperSerde<PotentialCustomersAverageWasCalculated> customersAverageSerde
                = new ObjectMapperSerde<>(PotentialCustomersAverageWasCalculated.class);

        // TODO: Build the stream topology
        builder.stream(
                POTENTIAL_CUSTOMERS_TOPIC,
                Consumed.with(Serdes.String(), customersEventSerde)
        )
        .groupByKey()
        .windowedBy(
                SlidingWindows.withTimeDifferenceAndGrace(
                        Duration.ofSeconds(10),
                        Duration.ofSeconds(15)
                )
        ).aggregate(
                PotentialCustomersAverageWasCalculated::new,
                (key, measure, avg) -> avg.update(measure),
                Materialized.with(Serdes.String(), customersAverageSerde)
        )
        .toStream()
        .map((Windowed<String> key, PotentialCustomersAverageWasCalculated average) -> {
            return new KeyValue<>(
                    key.key(),
                    Math.round(average.total / average.measurements)
            );
        }).to(
                AVERAGE_CUSTOMERS_TOPIC,
                Produced.with(Serdes.String(), Serdes.Long())
        );

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
