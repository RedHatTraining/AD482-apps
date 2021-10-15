package com.redhat.garden.back.event;

public class LowTemperatureDetected {
    public String gardenName;
    public Integer sensorId;
    public Double value;
    public Long timestamp;

    public LowTemperatureDetected() {}

    public LowTemperatureDetected(String gardenName, Integer sensorId, Double value,  Long timestamp) {
        this.gardenName = gardenName;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }
}
