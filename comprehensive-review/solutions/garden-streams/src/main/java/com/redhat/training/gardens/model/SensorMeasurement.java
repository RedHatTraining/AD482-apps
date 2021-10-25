package com.redhat.training.gardens.model;

import java.time.Instant;

public class SensorMeasurement {
    public Integer sensorId;
    public Double value;
    public SensorMeasurementType type;
    public Long timestamp;
    public Long receivedAt;

    public SensorMeasurement() {}

    public SensorMeasurement(Integer sensorId, SensorMeasurementType type, Double value, Long timestamp) {
        this.sensorId = sensorId;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
        this.receivedAt = Instant.now().getEpochSecond();
    }

    public SensorMeasurement(int sensorId, SensorMeasurementType type, double value, long timestamp) {
        this.sensorId = sensorId;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
        this.receivedAt = Instant.now().getEpochSecond();
    }
}

