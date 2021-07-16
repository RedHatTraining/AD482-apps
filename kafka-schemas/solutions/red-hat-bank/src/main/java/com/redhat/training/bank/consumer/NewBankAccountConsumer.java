package com.redhat.training.bank.consumer;

import com.redhat.training.bank.message.NewBankAccount;
import com.redhat.training.bank.model.BankAccount;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class NewBankAccountConsumer {
    private static final Logger LOGGER = Logger.getLogger(NewBankAccountConsumer.class);

    // TODO: Create the consumer implementation
    @Incoming("new-bank-account-in")
    @Blocking
    @Transactional
    public void processMessage(NewBankAccount message) {

        BankAccount entity = BankAccount.findById(message.getId());

        if (entity != null) {
            entity.profile = message.getBalance() < 100000
                    ? "regular" : "premium";
            LOGGER.info(
                    "Updated Bank Account - ID: "
                    + message.getId() + " - Type: " + entity.profile
            );
        } else {
            LOGGER.info("Bank Account not found!");
        }
    }
}
