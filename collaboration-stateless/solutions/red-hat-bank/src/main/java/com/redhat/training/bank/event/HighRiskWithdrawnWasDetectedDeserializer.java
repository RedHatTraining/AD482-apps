package com.redhat.training.bank.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class HighRiskWithdrawnWasDetectedDeserializer
        extends ObjectMapperDeserializer<HighRiskWithdrawnWasDetected> {
    public HighRiskWithdrawnWasDetectedDeserializer() {
        super(HighRiskWithdrawnWasDetected.class);
    }
}
