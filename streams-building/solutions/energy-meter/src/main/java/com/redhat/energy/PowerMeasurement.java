package com.redhat.energy;

public class PowerMeasurement {
    public Integer turbineId;
    public Double megawatts;

    public PowerMeasurement() {}

    public PowerMeasurement(Integer turbineId, Double megawatts) {
        this.turbineId = turbineId;
        this.megawatts = megawatts;
    }
}
