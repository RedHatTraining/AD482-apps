package com.redhat.training.gardens.model;

public enum SensorType {
    TEMPERATURE("Temperature"),
    HUMIDITY("Humidity"),
    WIND("Wind");

    public String type;

    SensorType(String type) {
        this.type = type;
    }
}
