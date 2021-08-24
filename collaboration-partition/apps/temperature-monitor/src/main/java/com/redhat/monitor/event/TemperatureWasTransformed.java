package com.redhat.monitor.event;

public class TemperatureWasTransformed {
    public Integer locationId;
    public Integer celsius;
    public Double fahrenheit;

    public TemperatureWasTransformed() {}

    public TemperatureWasTransformed(Integer locationId, Integer celsius, Double fahrenheit) {
        this.locationId = locationId;
        this.celsius = celsius;
        this.fahrenheit = fahrenheit;
    }
}
