package com.redhat.training.gardens.model;

public enum MeasureType {
    TEMPERATURE("Temperature"),
    HUMIDITY("Humidity"),
    WIND("Wind");

    public String type;

    MeasureType(String type) {
        this.type = type;
    }
}
