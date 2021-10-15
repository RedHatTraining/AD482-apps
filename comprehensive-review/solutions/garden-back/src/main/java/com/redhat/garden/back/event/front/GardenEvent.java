package com.redhat.garden.back.event.front;

public class GardenEvent {
    public String name;
    public String garden;
    public Integer sensorId;
    public Long timestamp;

    public GardenEvent() {}

    public GardenEvent(String name, String garden, Integer sensorId, Long timestamp) {
        this.name = name;
        this.garden = garden;
        this.sensorId = sensorId;
        this.timestamp = timestamp;
    }
}
