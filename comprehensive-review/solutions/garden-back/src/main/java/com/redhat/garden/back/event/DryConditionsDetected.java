package com.redhat.garden.back.event;

public class DryConditionsDetected {
    public String gardenName;
    public Integer sensorId;
    public Double value;
    public Integer timestamp;

    public DryConditionsDetected() {}

    public DryConditionsDetected(String gardenName, Integer sensorId, Double value, Integer timestamp) {
        this.gardenName = gardenName;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }
}
