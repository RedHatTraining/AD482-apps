package com.redhat.training.gardens.serde;

import com.redhat.training.gardens.event.LowHumidityDetected;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class LowHumidityDetectedDeserializer
        extends ObjectMapperDeserializer<LowHumidityDetected> {
    public LowHumidityDetectedDeserializer() {
        super(LowHumidityDetected.class);
    }
}
