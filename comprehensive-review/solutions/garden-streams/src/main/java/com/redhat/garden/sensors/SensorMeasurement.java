package com.redhat.garden.sensors;

import java.util.Date;

public class SensorMeasurement {
    public Integer sensorId;
    public SensorMeasurementType type;
    public Double value;
    public Date timestamp;

    public SensorMeasurement() {}

    public SensorMeasurement(Integer sensorId, SensorMeasurementType type, Double value, Date timestamp) {
        this.sensorId = sensorId;
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }
}

