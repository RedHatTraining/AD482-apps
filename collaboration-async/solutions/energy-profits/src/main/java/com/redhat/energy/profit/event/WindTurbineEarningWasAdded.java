package com.redhat.energy.profit.event;

public class WindTurbineEarningWasAdded {
    public Integer windTurbineId;
    public Double amount;

    public WindTurbineEarningWasAdded() {}

    public WindTurbineEarningWasAdded(Integer windTurbineId, Double amount) {
        this.windTurbineId = windTurbineId;
        this.amount = amount;
    }
}
