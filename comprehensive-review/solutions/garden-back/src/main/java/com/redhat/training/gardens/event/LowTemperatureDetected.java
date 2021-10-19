package com.redhat.training.gardens.event;

public class LowTemperatureDetected extends GardenEvent {
    public LowTemperatureDetected() {}

    public LowTemperatureDetected(String name, String gardenName, Integer sensorId, Double value, Long timestamp) {
        super(name, gardenName, sensorId, value, timestamp);
    }
}
