package com.redhat.garden.events;

import java.util.Date;

public class DryConditionsDetected extends GardenEvent {

    public DryConditionsDetected() {}

    public DryConditionsDetected(String gardenName, Integer sensorId, Double value, Date timestamp) {
        super("Dry conditions detected", gardenName, sensorId, value, timestamp);
    }
}
