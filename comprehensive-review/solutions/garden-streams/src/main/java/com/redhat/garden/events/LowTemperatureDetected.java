package com.redhat.garden.events;

import java.util.Date;

public class LowTemperatureDetected extends GardenEvent {
    public LowTemperatureDetected() {}

    public LowTemperatureDetected(String gardenName, Integer sensorId, Double value, Date timestamp) {
        super("Low temperature detected", gardenName, sensorId, value, timestamp);
    }
}
