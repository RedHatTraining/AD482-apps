package com.redhat.energy.profit.stream;

import com.redhat.energy.profit.event.WindTurbineProfitMarginWasCalculated;
import com.redhat.energy.profit.event.alert.LowProfitMarginWasDetected;
import com.redhat.energy.profit.stream.common.StreamProcessor;
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
public class NotifyAboutLowProfitMarginPipeline extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(NotifyAboutLowProfitMarginPipeline.class);

    // Reading topic
    static final String WIND_TURBINE_PROFIT_MARGINS_TOPIC = "wind-turbine-profit-margins";

    // Writing topic
    static final String LOW_PROFIT_MARGIN_TOPIC = "low-profit-margin-alert";

    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<WindTurbineProfitMarginWasCalculated> profitEventSerde
                = new ObjectMapperSerde<>(WindTurbineProfitMarginWasCalculated.class);

        ObjectMapperSerde<LowProfitMarginWasDetected> alertsEventSerde
                = new ObjectMapperSerde<>(LowProfitMarginWasDetected.class);

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

    // Helper methods
    private void logLowProfitMarginAlert(Integer windTurbineId, Double profitMargin) {
        LOGGER.infov(
                "LowProfitMarginWasDetected - Turbine ID: {0} Profit Margin: {1}",
                windTurbineId,
                profitMargin
        );
    }
}
