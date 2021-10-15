package com.redhat.garden.events;

public class StrongWindDetected extends GardenEvent {
    public StrongWindDetected() {}

    public StrongWindDetected(String gardenName, Integer sensorId, Double value, Long timestamp) {
        super("Strong wind detected", gardenName, sensorId, value, timestamp);
    }
}
