package com.redhat.customers.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PotentialCustomersAverageWasCalculatedDeserializer
        extends ObjectMapperDeserializer<PotentialCustomersAverageWasCalculated> {
    public PotentialCustomersAverageWasCalculatedDeserializer() {
        super(PotentialCustomersAverageWasCalculated.class);
    }
}
