package com.redhat.training.bank.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class LowRiskWithdrawnWasDetectedDeserializer
        extends ObjectMapperDeserializer<LowRiskWithdrawnWasDetected> {
    public LowRiskWithdrawnWasDetectedDeserializer() {
        super(LowRiskWithdrawnWasDetected.class);
    }
}
