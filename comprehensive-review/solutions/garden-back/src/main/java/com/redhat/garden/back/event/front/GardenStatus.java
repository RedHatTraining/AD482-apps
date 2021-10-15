package com.redhat.garden.back.event.front;

public class GardenStatus {
    public Integer id;
    public String name;
    public Double temperature;
    public String garden;
    public Integer sensorId;
    public Long timestamp;

    public GardenStatus() {}

    public GardenStatus(Integer id, String name, Double temperature, String garden, Integer sensorId, Long timestamp) {
        this.id = id;
        this.name = name;
        this.temperature = temperature;
        this.garden = garden;
        this.sensorId = sensorId;
        this.timestamp = timestamp;
    }
}
