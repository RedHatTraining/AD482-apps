package com.redhat.energy.api;

public class WindSpeed {
    public Integer turbineId;
    public Integer windSpeed;
    public Long timestamp;

    public WindSpeed(Integer turbineId, Integer windSpeed, Long timestamp) {
        this.turbineId = turbineId;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
    }

    // @Override
    // public String toString() {
    //     return " { distance: " + distance + ", time: " + time + " }";
    // }
}