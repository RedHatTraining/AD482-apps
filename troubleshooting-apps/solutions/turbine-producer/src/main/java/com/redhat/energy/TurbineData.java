package com.redhat.energy;

public class TurbineData {

    public Integer turbineId;
    public Integer power;
    public Integer wind;
    public Long timestamp;

    public TurbineData(Integer turbineId, Integer power, Integer wind, Long timestamp) {
        this.turbineId = turbineId;
        this.power = power;
        this.wind = wind;
        this.timestamp = timestamp;
    }
}
