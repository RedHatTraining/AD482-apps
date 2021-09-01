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
import org.apache.kafka.streams.kstream.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class AmountWasWithdrawnPipeline extends StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(AmountWasWithdrawnPipeline.class);

    // Reading topics
    static final String AMOUNT_WAS_WITHDRAWN_TOPIC = "bank-account-withdrawn";

    // Writing topics
    static final String LOW_RISK_WITHDRAWN_TOPIC = "low-risk-withdrawn-alert";
    static final String MODERATE_RISK_WITHDRAWN_TOPIC = "moderate-risk-withdrawn-alert";
    static final String HIGH_RISK_WITHDRAWN_TOPIC = "high-risk-withdrawn-alert";

    private KafkaStreams streams;

    private ObjectMapperSerde<LowRiskWithdrawnWasDetected> lowRiskEventSerde;
    private ObjectMapperSerde<ModerateRiskWithdrawnWasDetected> moderateRiskEventSerde;
    private ObjectMapperSerde<HighRiskWithdrawnWasDetected> highRiskEventSerde;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<AmountWasWithdrawn> withdrawalEventSerde
                = new ObjectMapperSerde<>(AmountWasWithdrawn.class);

        lowRiskEventSerde = new ObjectMapperSerde<>(LowRiskWithdrawnWasDetected.class);
        moderateRiskEventSerde = new ObjectMapperSerde<>(ModerateRiskWithdrawnWasDetected.class);
        highRiskEventSerde = new ObjectMapperSerde<>(HighRiskWithdrawnWasDetected.class);

        // TODO: Add inverse filter

        // TODO: Split the stream

        // TODO: Create a Kafka streams and start it
    }

    private void processLowAmountEvents(KStream<Long, AmountWasWithdrawn> stream) {
        // TODO: process the low amount branch
    }

    private void processModerateAmountEvents(KStream<Long, AmountWasWithdrawn> stream) {
        // TODO: process the moderate amount branch
    }

    private void processHighAmountEvents(KStream<Long, AmountWasWithdrawn> stream) {
        // TODO: process the high amount branch
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        // TODO: Close the stream on shutdown
    }

    // Helper methods
    private void logLowRiskWithdrawn(Long bankAccountId, Long amount) {
        LOGGER.infov(
                "Low Risk Withdrawn - Account ID: {0} Amount: {1}",
                bankAccountId,
                amount
        );
    }

    private void logModerateRiskWithdrawn(Long bankAccountId, Long amount) {
        LOGGER.infov(
                "Moderate Risk Withdrawn - Account ID: {0} Amount: {1}",
                bankAccountId,
                amount
        );
    }

    private void logHighRiskWithdrawn(Long bankAccountId, Long amount) {
        LOGGER.infov(
                "High Risk Withdrawn - Account ID: {0} Amount: {1}",
                bankAccountId,
                amount
        );
    }
}
