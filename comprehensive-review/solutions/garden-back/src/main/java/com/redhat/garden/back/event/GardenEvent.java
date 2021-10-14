package com.redhat.garden.back.event;

public class GardenEvent {
    public String name;
    public String garden;
    public Integer sensorId;
    public Integer timestamp;

    public GardenEvent() {}

    public GardenEvent(String name, String garden, Integer sensorId, Integer timestamp) {
        this.name = name;
        this.garden = garden;
        this.sensorId = sensorId;
        this.timestamp = timestamp;
    }
}
