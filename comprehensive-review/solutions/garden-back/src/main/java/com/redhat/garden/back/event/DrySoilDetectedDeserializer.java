package com.redhat.garden.back.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class DrySoilDetectedDeserializer
        extends ObjectMapperDeserializer<DrySoilDetected> {
    public DrySoilDetectedDeserializer() {
        super(DrySoilDetected.class);
    }
}
