package com.redhat.training.gardens.serde;

import com.redhat.training.gardens.event.LowTemperatureDetected;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class LowTemperatureDetectedDeserializer
        extends ObjectMapperDeserializer<LowTemperatureDetected> {
    public LowTemperatureDetectedDeserializer() {
        super(LowTemperatureDetected.class);
    }
}
