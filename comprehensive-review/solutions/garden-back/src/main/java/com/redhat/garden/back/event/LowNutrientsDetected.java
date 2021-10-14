package com.redhat.garden.back.event;

public class LowNutrientsDetected {
    public String gardenName;
    public Integer sensorId;
    public Double value;
    public Integer timestamp;

    public LowNutrientsDetected() {}

    public LowNutrientsDetected(String gardenName, Integer sensorId, Double value,  Integer timestamp) {
        this.gardenName = gardenName;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }
}