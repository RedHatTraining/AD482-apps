package com.redhat.garden.back.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class StrongWindDetectedDeserializer
        extends ObjectMapperDeserializer<StrongWindDetected> {
    public StrongWindDetectedDeserializer() {
        super(StrongWindDetected.class);
    }
}
