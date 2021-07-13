package com.redhat.training.bank.consumer;

import com.redhat.training.bank.event.avro.BankAccountBalanceWasChanged;
import com.redhat.training.bank.model.BankAccount;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class BankAccountBalanceWasChangedConsumer {
    private static final Logger LOGGER = Logger.getLogger(BankAccountBalanceWasChangedConsumer.class);

    // @todo: create the consumer implementation
    @Incoming("bank-account-balance-in")
    @Blocking
    @Transactional
    public void processEvent(BankAccountBalanceWasChanged event) {
        LOGGER.info("Processing event: " + BankAccountBalanceWasChangedConsumer.class);

        BankAccount entity = BankAccount.findById(event.getId());
        String newProfile = event.getBalance() < 100000 ? "regular" : "premium";

        if (entity != null && !newProfile.equals(entity.profile)) {
            entity.profile = newProfile;
            LOGGER.info(
                    "Updated account type - ID: "
                            + event.getId() + " - new type: " + entity.profile
            );
        } else {
            LOGGER.info("Nothing to do!");
        }
    }
}
