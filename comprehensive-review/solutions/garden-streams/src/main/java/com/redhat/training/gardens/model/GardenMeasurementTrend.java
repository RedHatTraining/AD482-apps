package com.redhat.training.gardens.model;

public enum GardenMeasurementTrend {
    UP("Up"),
    STABLE("Stable"),
    DOWN("Down");

    public String trend;

    GardenMeasurementTrend(String trend) {
        this.trend = trend;
    }
}