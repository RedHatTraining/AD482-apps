package com.redhat.garden.sensors;

public enum SensorMeasurementType {
    TEMPERATURE("Temperature"),
    HUMIDITY("Humidity"),
    WIND("Wind");

    public String type;

    SensorMeasurementType(String type) {
        this.type = type;
    }
}
