package com.redhat.energy.records;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class MWattsMeasurementDeserializer extends ObjectMapperDeserializer<MWattsMeasurement>  {
    public MWattsMeasurementDeserializer() {
        super(MWattsMeasurement.class);
    }
}
