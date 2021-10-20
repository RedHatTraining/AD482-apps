package com.redhat.training.gardens.model;

public class SensorMeasurement {
    public Integer sensorId;
    public String type;
    public Double value;
    public Long timestamp;

    public SensorMeasurement() {}

    public SensorMeasurement(Integer sensorId, String type, Double value, Long timestamp) {
        this.sensorId = sensorId;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }
}
