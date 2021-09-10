package com.redhat.energy.profit.event.alert;

public class LowProfitMarginWasDetected {
    public Integer windTurbineId;
    public Double rate;

    public LowProfitMarginWasDetected() {}

    public LowProfitMarginWasDetected(Integer windTurbineId, Double rate) {
        this.windTurbineId = windTurbineId;
        this.rate = rate;
    }
}
