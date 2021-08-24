package com.redhat.monitor.stream;

import com.redhat.monitor.event.TemperatureWasMeasuredInCelsius;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class RepartitionStream extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(RepartitionStream.class);

    // Reading topic
    static final String TEMPERATURES_TOPIC = "temperatures-in-celsius";

    // Writing topic
    static final String TEMPERATURES_REPARTITIONED_TOPIC = "temperatures-in-celsius-repartitioned";

    @Produces
    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<TemperatureWasMeasuredInCelsius> temperaturesEventSerde
                = new ObjectMapperSerde<>(TemperatureWasMeasuredInCelsius.class);

        KStream<String, TemperatureWasMeasuredInCelsius> stream = builder.stream(
                TEMPERATURES_TOPIC, Consumed.with(Serdes.String(), temperaturesEventSerde)
        );

        // TODO: Implement the topology for the repartitioning
        stream.map(
            (key, measure) -> {
                LOGGER.infov(
                    "Repartitioning ID {0}, {1}ºC ...",
                    measure.locationId, measure.measure
                );

                return new KeyValue<>(measure.locationId, measure);
            }
        ).to(
            TEMPERATURES_REPARTITIONED_TOPIC,
            Produced.with(Serdes.Integer(), temperaturesEventSerde)
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
