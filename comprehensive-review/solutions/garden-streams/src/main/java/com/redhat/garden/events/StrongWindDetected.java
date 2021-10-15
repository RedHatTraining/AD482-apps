package com.redhat.garden.events;

import java.util.Date;

public class StrongWindDetected extends GardenEvent {
    public StrongWindDetected() {}

    public StrongWindDetected(String gardenName, Integer sensorId, Double value, Date timestamp) {
        super("Strong wind detected", gardenName, sensorId, value, timestamp);
    }
}
