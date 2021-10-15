package com.redhat.garden.back.event;

public class DryConditionsDetected extends GardenEvent {
    public DryConditionsDetected() {}

    public DryConditionsDetected(String name, String gardenName, Integer sensorId, Double value, Long timestamp) {
        super(name, gardenName, sensorId, value, timestamp);
    }
}
