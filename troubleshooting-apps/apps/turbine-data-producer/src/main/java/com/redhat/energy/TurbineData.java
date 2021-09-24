package com.redhat.energy;

public class TurbineData {

    public Integer turbineId;
    public Integer power;
    public Integer wind;
    public Long timestamp;
    public Long tick;

    public TurbineData(Integer turbineId, Integer power, Integer wind, Long timestamp, Long tick) {
        this.turbineId = turbineId;
        this.power = power;
        this.wind = wind;
        this.timestamp = timestamp;
        this.tick = tick;
    }

    public String toString() {
        return "{ " +
            "turbineId: " + turbineId + ", " +
            "power: " + power + " }";

    }
}
