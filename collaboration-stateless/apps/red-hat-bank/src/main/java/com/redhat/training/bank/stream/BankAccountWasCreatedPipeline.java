package com.redhat.training.bank.stream;

import com.redhat.training.bank.event.BankAccountWasCreated;
import com.redhat.training.bank.model.BankAccount;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

@ApplicationScoped
public class BankAccountWasCreatedPipeline extends  StreamProcessor {
    private static final Logger LOGGER = Logger.getLogger(BankAccountWasCreatedPipeline.class);

    // Reading topic
    static final String BANK_ACCOUNT_WAS_CREATED_TOPIC = "bank-account-creation";

    private KafkaStreams streams;

    void onStart(@Observes StartupEvent startupEvent) {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<BankAccountWasCreated> eventSerde
                = new ObjectMapperSerde<>(BankAccountWasCreated.class);

        // TODO: Update the account type on each event

        // TODO: Create a Kafka streams and start it
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        // TODO: Close the stream on shutdown
    }

    @Transactional
    public void updateAccountTypeFromEvent(BankAccountWasCreated event) {

        BankAccount entity = BankAccount.findById(event.id);

        if (entity != null) {
            entity.profile = event.balance < 100000 ? "regular" : "premium";
            LOGGER.info(
                    "Updated Bank Account - ID: "
                    + event.id + " - Type: " + entity.profile
            );
        } else {
            LOGGER.info("Bank Account with id " + event.id + " not found!");
        }
    }
}
