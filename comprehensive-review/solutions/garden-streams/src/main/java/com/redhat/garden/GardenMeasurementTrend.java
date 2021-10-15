package com.redhat.garden;

public enum GardenMeasurementTrend {
    UP("Up"),
    STABLE("Stable"),
    DOWN("Down");

    public String trend;

    GardenMeasurementTrend(String trend) {
        this.trend = trend;
    }
}