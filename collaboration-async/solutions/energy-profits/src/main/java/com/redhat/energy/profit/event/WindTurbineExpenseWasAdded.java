package com.redhat.energy.profit.event;

public class WindTurbineExpenseWasAdded {
    public Integer windTurbineId;
    public Double amount;

    public WindTurbineExpenseWasAdded() {}

    public WindTurbineExpenseWasAdded(Integer windTurbineId, Double amount) {
        this.windTurbineId = windTurbineId;
        this.amount = amount;
    }
}
