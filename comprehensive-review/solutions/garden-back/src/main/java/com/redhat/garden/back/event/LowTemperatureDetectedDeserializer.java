package com.redhat.garden.back.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class LowTemperatureDetectedDeserializer
        extends ObjectMapperDeserializer<LowTemperatureDetected> {
    public LowTemperatureDetectedDeserializer() {
        super(LowTemperatureDetected.class);
    }
}
