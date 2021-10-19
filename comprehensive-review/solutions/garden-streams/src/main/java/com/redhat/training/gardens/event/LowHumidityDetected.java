package com.redhat.training.gardens.event;

public class LowHumidityDetected extends GardenEvent {

    public LowHumidityDetected() {}

    public LowHumidityDetected(String gardenName, Integer sensorId, Double value, Long timestamp) {
        super("Dry conditions detected", gardenName, sensorId, value, timestamp);
    }
}
