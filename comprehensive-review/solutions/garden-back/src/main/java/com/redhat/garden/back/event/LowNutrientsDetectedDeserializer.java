package com.redhat.garden.back.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class LowNutrientsDetectedDeserializer
        extends ObjectMapperDeserializer<LowNutrientsDetected> {
    public LowNutrientsDetectedDeserializer() {
        super(LowNutrientsDetected.class);
    }
}
