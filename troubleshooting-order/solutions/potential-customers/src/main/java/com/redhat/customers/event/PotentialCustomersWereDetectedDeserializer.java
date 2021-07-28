package com.redhat.customers.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PotentialCustomersWereDetectedDeserializer
        extends ObjectMapperDeserializer<PotentialCustomersWereDetected> {
    public PotentialCustomersWereDetectedDeserializer() {
        super(PotentialCustomersWereDetected.class);
    }
}
