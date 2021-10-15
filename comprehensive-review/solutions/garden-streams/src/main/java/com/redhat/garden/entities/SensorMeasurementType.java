package com.redhat.garden.entities;

public enum SensorMeasurementType {
    TEMPERATURE("Temperature"),
    HUMIDITY("Humidity"),
    WIND("Wind");

    public String type;

    SensorMeasurementType(String type) {
        this.type = type;
    }
}
