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
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<AmountWasWithdrawn> withdrawalEventSerde
                = new ObjectMapperSerde<>(AmountWasWithdrawn.class);

        ObjectMapperSerde<LowRiskWithdrawnWasDetected> lowRiskEventSerde
                = new ObjectMapperSerde<>(LowRiskWithdrawnWasDetected.class);

        ObjectMapperSerde<ModerateRiskWithdrawnWasDetected> moderateRiskEventSerde
                = new ObjectMapperSerde<>(ModerateRiskWithdrawnWasDetected.class);

        ObjectMapperSerde<HighRiskWithdrawnWasDetected> highRiskEventSerde
                = new ObjectMapperSerde<>(HighRiskWithdrawnWasDetected.class);

        // TODO: Add inverse filter
        KStream<Long, AmountWasWithdrawn> mainStream = builder.stream(
                AMOUNT_WAS_WITHDRAWN_TOPIC,
                Consumed.with(Serdes.Long(), withdrawalEventSerde)
        ).filterNot((key, withdrawal) -> {
            return withdrawal.amount <= 50;
        });

        // TODO: Split the stream
        KStream<Long, AmountWasWithdrawn>[] eventStreams = mainStream.branch(
                (key, withdrawal) -> withdrawal.amount > 50 && withdrawal.amount <= 1000,
                (key, withdrawal) -> withdrawal.amount > 1000 && withdrawal.amount <= 3000,
                (key, withdrawal) -> true
        );

        // TODO: Map the low risk branch to a new Event and send to a topic
        eventStreams[0].map((key, deposit) -> {
            LOGGER.info(
                    "Low Risk Withdrawn - Account ID:" + deposit.bankAccountId
                    + " Amount:" + deposit.amount
            );

            return new KeyValue<>(
                    deposit.bankAccountId,
                    new LowRiskWithdrawnWasDetected(deposit.bankAccountId, deposit.amount)
            );
        }).to(
                LOW_RISK_WITHDRAWN_TOPIC,
                Produced.with(Serdes.Long(), lowRiskEventSerde)
        );

        // TODO: Map the moderate risk branch to a new Event and send to a topic
        eventStreams[1].map((key, deposit) -> {
            LOGGER.info(
                    "Moderate Risk Withdrawn - Account ID:" + deposit.bankAccountId
                    + " Amount:" + deposit.amount
            );

            return new KeyValue<>(
                    deposit.bankAccountId,
                    new ModerateRiskWithdrawnWasDetected(deposit.bankAccountId, deposit.amount)
            );
        }).to(
                MODERATE_RISK_WITHDRAWN_TOPIC,
                Produced.with(Serdes.Long(), moderateRiskEventSerde)
        );

        // TODO: Map the high risk branch to a new Event and send to a topic
        eventStreams[2].map((key, deposit) -> {
            LOGGER.info(
                    "High Risk Withdrawn - Account ID:" + deposit.bankAccountId
                     + " Amount:" + deposit.amount
            );

            return new KeyValue<>(
                    deposit.bankAccountId,
                    new HighRiskWithdrawnWasDetected(deposit.bankAccountId, deposit.amount)
            );
        }).to(
                HIGH_RISK_WITHDRAWN_TOPIC,
                Produced.with(Serdes.Long(), highRiskEventSerde)
        );

        // TODO: Create a Kafka streams and start it
        streams = new KafkaStreams(
                builder.build(),
                generateStreamConfig()
        );

        streams.start();
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        // TODO: Close the stream on shutdown
        streams.close();
    }
}
