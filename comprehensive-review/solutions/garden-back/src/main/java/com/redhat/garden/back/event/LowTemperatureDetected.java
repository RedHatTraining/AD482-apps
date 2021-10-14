package com.redhat.garden.back.event;

public class LowTemperatureDetected {
    public String name;
    public String garden;
    public Integer sensorId;
    public Integer timestamp;

    public LowTemperatureDetected() {}

    public LowTemperatureDetected(String name, String garden, Integer sensorId, Integer timestamp) {
        this.name = name;
        this.garden = garden;
        this.sensorId = sensorId;
        this.timestamp = timestamp;
    }
}
