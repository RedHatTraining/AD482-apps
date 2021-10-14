package com.redhat.garden.sensors;

public class SensorMeasurementEnriched extends SensorMeasurement {
    public String sensorName;
    public String gardenName;

    public SensorMeasurementEnriched() {}

    public SensorMeasurementEnriched(SensorMeasurement measurement, Sensor sensor) {
        this.sensorId = sensor.id;
        this.sensorName = sensor.name;
        this.gardenName = sensor.gardenName;
        this.property = measurement.property;
        this.value = measurement.value;
        this.timestamp = measurement.timestamp;
    }
}

