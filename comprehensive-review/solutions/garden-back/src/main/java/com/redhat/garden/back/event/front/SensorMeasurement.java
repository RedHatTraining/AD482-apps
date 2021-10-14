package com.redhat.garden.back.event.front;

public class SensorMeasurement {
    public String type;
    public Double value;
    public String garden;
    public Integer sensorId;
    public Integer timestamp;

    public SensorMeasurement() {}

    public SensorMeasurement(String type, Double value, String garden, Integer sensorId, Integer timestamp) {
        this.type = type;
        this.value = value;
        this.garden = garden;
        this.sensorId = sensorId;
        this.timestamp = timestamp;
    }
}
