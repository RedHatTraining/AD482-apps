package com.redhat.garden.back.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class DryConditionsDetectedDeserializer
        extends ObjectMapperDeserializer<DryConditionsDetected> {
    public DryConditionsDetectedDeserializer() {
        super(DryConditionsDetected.class);
    }
}
