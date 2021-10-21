package com.redhat.training.gardens.event;

public class StrongWindDetected extends GardenEvent {
    public StrongWindDetected() {}

    public StrongWindDetected(String gardenName, Integer sensorId, Double value, Long timestamp) {
        super("Strong wind detected", gardenName, sensorId, value, timestamp);
    }
}
