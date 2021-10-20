package com.redhat.training.gardens.serde;

import com.redhat.training.gardens.event.StrongWindDetected;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class StrongWindDetectedDeserializer
        extends ObjectMapperDeserializer<StrongWindDetected> {
    public StrongWindDetectedDeserializer() {
        super(StrongWindDetected.class);
    }
}
