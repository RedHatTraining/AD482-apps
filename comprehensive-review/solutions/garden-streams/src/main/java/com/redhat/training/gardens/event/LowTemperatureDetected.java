package com.redhat.training.gardens.event;

public class LowTemperatureDetected extends GardenEvent {
    public LowTemperatureDetected() {}

    public LowTemperatureDetected(String gardenName, Integer sensorId, Double value, Long timestamp) {
        super("Low temperature detected", gardenName, sensorId, value, timestamp);
    }
}
