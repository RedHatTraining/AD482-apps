package com.redhat.energy.records;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class WindTurbineStatsDeserializer extends ObjectMapperDeserializer<WindTurbineStats>  {
    public WindTurbineStatsDeserializer() {
        super(WindTurbineStats.class);
    }
}
