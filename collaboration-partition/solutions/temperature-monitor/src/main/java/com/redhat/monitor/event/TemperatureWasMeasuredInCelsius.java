package com.redhat.monitor.event;

public class TemperatureWasMeasuredInCelsius {
    public Integer locationId;
    public Integer measure;

    public TemperatureWasMeasuredInCelsius() {}

    public TemperatureWasMeasuredInCelsius(Integer locationId, Integer measure) {
        this.locationId = locationId;
        this.measure = measure;
    }
}
