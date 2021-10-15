package com.redhat.garden.events;

/**
 * Base event class
 */
public class GardenEvent {
    public String name;
    public String gardenName;
    public Integer sensorId;
    public Double value;
    public Long timestamp;

    public GardenEvent() {}

    public GardenEvent(String name, String gardenName, Integer sensorId, Double value, Long timestamp) {
        this.name = name;
        this.gardenName = gardenName;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }
}
