package com.redhat.training.bank.consumer;

import com.redhat.training.bank.event.BankAccountWasCreated;
import com.redhat.training.bank.model.BankAccount;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class BankAccountWasCreatedConsumer {
    private static final Logger LOGGER = Logger.getLogger(BankAccountWasCreatedConsumer.class);

    // @todo: create the consumer implementation
    @Incoming("bank-account-creation-in")
    @Blocking
    @Transactional
    public void processEvent(Record<Long, BankAccountWasCreated> record) {
        LOGGER.info("Processing event: " + BankAccountWasCreated.class);

        BankAccountWasCreated event = record.value();
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
