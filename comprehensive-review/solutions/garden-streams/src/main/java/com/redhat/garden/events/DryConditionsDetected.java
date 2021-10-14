package com.redhat.garden.events;

import java.util.Date;

public class DryConditionsDetected {
    public String gardenName;
    public Integer sensorId;
    public Double value;
    public Date timestamp;

    public DryConditionsDetected() {}

    public DryConditionsDetected(String gardenName, Integer sensorId, Double value, Date timestamp) {
        this.gardenName = gardenName;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }
}
