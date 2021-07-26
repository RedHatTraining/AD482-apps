package com.redhat.training.bank.stream;

import com.redhat.training.bank.event.AmountWasWithdrawn;
import com.redhat.training.bank.event.HighRiskWithdrawnWasDetected;
import com.redhat.training.bank.event.LowRiskWithdrawnWasDetected;
import com.redhat.training.bank.event.ModerateRiskWithdrawnWasDetected;
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
public class AmountWasWithdrawnPipeline extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(AmountWasWithdrawnPipeline.class);

    // Reading topics
    static final String AMOUNT_WAS_WITHDRAWN_TOPIC = "bank-account-withdrawn";

    // Writing topics
    static final String LOW_RISK_WITHDRAWN_TOPIC = "low-risk-withdrawn-alert";
    static final String MODERATE_RISK_WITHDRAWN_TOPIC = "moderate-risk-withdrawn-alert";
    static final String HIGH_RISK_WITHDRAWN_TOPIC = "high-risk-withdrawn-alert";

    @Produces
    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        // TODO: Add a streams builder

        // TODO: Create SerDes

        // TODO: Add inverse filter

        // TODO: Split the stream

        // TODO: Map the low risk branch to a new Event and send to a topic

        // TODO: Map the moderate risk branch to a new Event and send to a topic

        // TODO: Map the high risk branch to a new Event and send to a topic

        // TODO: Create a Kafka streams and start it
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        // TODO: Close the stream on shutdown
    }
}
