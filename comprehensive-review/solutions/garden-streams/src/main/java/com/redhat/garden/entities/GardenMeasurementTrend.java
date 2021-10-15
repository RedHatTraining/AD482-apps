package com.redhat.garden.entities;

public enum GardenMeasurementTrend {
    UP("Up"),
    STABLE("Stable"),
    DOWN("Down");

    public String trend;

    GardenMeasurementTrend(String trend) {
        this.trend = trend;
    }
}