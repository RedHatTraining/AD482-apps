package com.redhat.energy;

public class WindTurbineStats {

    public Integer turbineId;
    public Long count;

    public WindTurbineStats() {}

    public WindTurbineStats(Integer turbineId, Long count) {
        this.turbineId = turbineId;
        this.count = count;
    }

}
