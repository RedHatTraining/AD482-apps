package com.redhat.garden.sensors;

import java.util.Date;

public class SensorMeasurement {
    public Integer sensorId;
    public String property;
    public Double value;
    public Date timestamp;

    public SensorMeasurement() {}

    public SensorMeasurement(Integer sensorId, String property, Double value, Date timestamp) {
        this.sensorId = sensorId;
        this.property = property;
        this.value = value;
        this.timestamp = timestamp;
    }
}

