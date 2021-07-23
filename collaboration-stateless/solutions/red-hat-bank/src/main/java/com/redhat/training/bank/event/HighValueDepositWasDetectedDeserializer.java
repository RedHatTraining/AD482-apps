package com.redhat.training.bank.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class HighValueDepositWasDetectedDeserializer
        extends ObjectMapperDeserializer<HighValueDepositWasDetected> {
    public HighValueDepositWasDetectedDeserializer() {
        super(HighValueDepositWasDetected.class);
    }
}
