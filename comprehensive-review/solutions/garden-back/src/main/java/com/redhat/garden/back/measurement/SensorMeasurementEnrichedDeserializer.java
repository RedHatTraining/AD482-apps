package com.redhat.garden.back.measurement;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class SensorMeasurementEnrichedDeserializer
        extends ObjectMapperDeserializer<SensorMeasurementEnriched> {
    public SensorMeasurementEnrichedDeserializer() {
        super(SensorMeasurementEnriched.class);
    }
}
