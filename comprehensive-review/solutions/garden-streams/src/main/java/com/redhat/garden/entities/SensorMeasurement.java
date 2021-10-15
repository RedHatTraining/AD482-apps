package com.redhat.garden.entities;

public class SensorMeasurement {
    public Integer sensorId;
    public SensorMeasurementType type;
    public Double value;
    public Long timestamp;

    public SensorMeasurement() {}

    public SensorMeasurement(Integer sensorId, SensorMeasurementType type, Double value, Long timestamp) {
        this.sensorId = sensorId;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }
}

