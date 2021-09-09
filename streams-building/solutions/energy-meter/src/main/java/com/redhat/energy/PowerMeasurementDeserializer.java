package com.redhat.energy;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PowerMeasurementDeserializer extends ObjectMapperDeserializer<PowerMeasurement>  {
    public PowerMeasurementDeserializer() {
        super(PowerMeasurement.class);
    }
}
