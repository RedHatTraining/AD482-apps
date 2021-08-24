package com.redhat.monitor.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class TemperatureWasMeasuredInCelsiusDeserializer
        extends ObjectMapperDeserializer<TemperatureWasMeasuredInCelsius> {
    public TemperatureWasMeasuredInCelsiusDeserializer() {
        super(TemperatureWasMeasuredInCelsius.class);
    }
}
