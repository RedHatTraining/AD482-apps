package com.redhat.energy.records;

public class MWattsMeasurement {
    public Integer turbineId;
    public Double megawatts;

    public MWattsMeasurement() {}

    public MWattsMeasurement(Integer turbineId, Double megawatts) {
        this.turbineId = turbineId;
        this.megawatts = megawatts;
    }
}
