package com.redhat.garden.back.event;

public class StrongWindDetected extends GardenEvent {
    public StrongWindDetected() {}

    public StrongWindDetected(String name, String gardenName, Integer sensorId, Double value, Long timestamp) {
        super(name, gardenName, sensorId, value, timestamp);
    }
}
