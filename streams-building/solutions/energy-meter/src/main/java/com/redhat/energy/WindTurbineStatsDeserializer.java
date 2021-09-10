package com.redhat.energy;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class WindTurbineStatsDeserializer extends ObjectMapperDeserializer<WindTurbineStats>  {
    public WindTurbineStatsDeserializer() {
        super(WindTurbineStats.class);
    }
}
