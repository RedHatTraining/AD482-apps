package com.redhat.monitor.stream;

import com.redhat.monitor.event.TemperatureWasMeasuredInCelsius;
import com.redhat.monitor.event.TemperatureWasTransformed;
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
public class TransformTemperature extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(TransformTemperature.class);

    private static final int CALCULATION_DELAY = 5000;

    // Reading topic
    static final String TEMPERATURES_TOPIC = "temperatures-in-celsius";

    // Writing topic
    static final String MEASURED_TEMPERATURES_TOPIC = "measured-temperatures";

    @Produces
    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<TemperatureWasMeasuredInCelsius> temperaturesEventSerde
                = new ObjectMapperSerde<>(TemperatureWasMeasuredInCelsius.class);

        ObjectMapperSerde<TemperatureWasTransformed> temperaturesTransformedEventSerde
                = new ObjectMapperSerde<>(TemperatureWasTransformed.class);

        KStream<String, TemperatureWasMeasuredInCelsius> stream = builder.stream(
                TEMPERATURES_TOPIC, Consumed.with(Serdes.String(), temperaturesEventSerde)
        );

        stream.map((key, measure) -> {
            LOGGER.infov("Transforming {0}ºC to ºF...", measure.measure);

            // Simulate a slow calculation
            try {
                Thread.sleep(CALCULATION_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Double fahrenheit = ((double) measure.measure * 9/5) + 32;

            LOGGER.infov(
                    "Temp. transformed {0}ºC -> {1}ºF (ID: {2})",
                    measure.measure,
                    fahrenheit,
                    measure.locationId
            );

            return new KeyValue<>(
                    measure.locationId,
                    new TemperatureWasTransformed(measure.locationId, measure.measure, fahrenheit)
            );
        }).to(
                MEASURED_TEMPERATURES_TOPIC,
                Produced.with(Serdes.Integer(), temperaturesTransformedEventSerde)
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
