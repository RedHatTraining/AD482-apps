package com.redhat.training.gardens.serde;

import com.redhat.training.gardens.model.SensorMeasurementEnriched;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class SensorMeasurementEnrichedDeserializer
        extends ObjectMapperDeserializer<SensorMeasurementEnriched> {
    public SensorMeasurementEnrichedDeserializer() {
        super(SensorMeasurementEnriched.class);
    }
}
