package com.redhat.training.gardens.serde;

import com.redhat.training.gardens.event.GardenStatusEvent;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class GardenStatusDeserializer
        extends ObjectMapperDeserializer<GardenStatusEvent> {
    public GardenStatusDeserializer() {
        super(GardenStatusEvent.class);
    }
}
