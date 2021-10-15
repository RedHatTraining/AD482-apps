package com.redhat.garden.back.measurement;

public class SensorMeasurement {
    public Integer sensorId;
    public String property;
    public Double value;
    public Integer timestamp;

    public SensorMeasurement() {}

    public SensorMeasurement(Integer sensorId, String property, Double value, Integer timestamp) {
        this.sensorId = sensorId;
        this.property = property;
        this.value = value;
        this.timestamp = timestamp;
    }
}
