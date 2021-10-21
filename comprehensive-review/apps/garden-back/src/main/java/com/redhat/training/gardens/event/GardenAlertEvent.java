package com.redhat.training.gardens.event;

abstract public class GardenAlertEvent {
    public String name;
    public String gardenName;
    public Integer sensorId;
    public Double value;
    public Long timestamp;

    public GardenAlertEvent() {}

    public GardenAlertEvent(String name, String gardenName, Integer sensorId, Double value, Long timestamp) {
        this.name = name;
        this.gardenName = gardenName;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }
}