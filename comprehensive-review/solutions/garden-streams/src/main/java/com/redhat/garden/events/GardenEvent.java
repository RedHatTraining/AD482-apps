package com.redhat.garden.events;

import java.util.Date;

/**
 * 
 */
public class GardenEvent {
    public String name;
    public String gardenName;
    public Integer sensorId;
    public Double value;
    public Date timestamp;

    public GardenEvent() {}

    public GardenEvent(String name, String gardenName, Integer sensorId, Double value, Date timestamp) {
        this.name = name;
        this.gardenName = gardenName;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }
}
