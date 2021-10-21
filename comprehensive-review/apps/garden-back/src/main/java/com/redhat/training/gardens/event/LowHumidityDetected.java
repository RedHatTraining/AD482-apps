package com.redhat.training.gardens.event;

public class LowHumidityDetected extends GardenAlertEvent {
    public LowHumidityDetected() {}

    public LowHumidityDetected(String name, String gardenName, Integer sensorId, Double value, Long timestamp) {
        super(name, gardenName, sensorId, value, timestamp);
    }
}
