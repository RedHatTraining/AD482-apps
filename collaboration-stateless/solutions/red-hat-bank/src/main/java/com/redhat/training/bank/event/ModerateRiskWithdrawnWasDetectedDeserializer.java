package com.redhat.training.bank.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ModerateRiskWithdrawnWasDetectedDeserializer
        extends ObjectMapperDeserializer<ModerateRiskWithdrawnWasDetected> {
    public ModerateRiskWithdrawnWasDetectedDeserializer() {
        super(ModerateRiskWithdrawnWasDetected.class);
    }
}
