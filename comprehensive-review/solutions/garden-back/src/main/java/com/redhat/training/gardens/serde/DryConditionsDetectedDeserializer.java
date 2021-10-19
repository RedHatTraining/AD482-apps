package com.redhat.training.gardens.serde;

import com.redhat.training.gardens.event.DryConditionsDetected;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class DryConditionsDetectedDeserializer
        extends ObjectMapperDeserializer<DryConditionsDetected> {
    public DryConditionsDetectedDeserializer() {
        super(DryConditionsDetected.class);
    }
}
