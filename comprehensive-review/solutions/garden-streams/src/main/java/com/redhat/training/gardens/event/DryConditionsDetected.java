package com.redhat.training.gardens.event;

public class DryConditionsDetected extends GardenEvent {

    public DryConditionsDetected() {}

    public DryConditionsDetected(String gardenName, Integer sensorId, Double value, Long timestamp) {
        super("Dry conditions detected", gardenName, sensorId, value, timestamp);
    }
}
