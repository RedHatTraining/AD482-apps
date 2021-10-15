package com.redhat.garden.simulator.event;

public class MeasurementWasTaken {
    public Integer deviceID;
    public Integer gardenID;
    public String measurementType;
    public Double measurementValue;

    public MeasurementWasTaken() {}

    public MeasurementWasTaken(Integer deviceID, Integer gardenID, String measurementType, Double measurementValue) {
        this.deviceID = deviceID;
        this.gardenID = gardenID;
        this.measurementType = measurementType;
        this.measurementValue = measurementValue;
    }
}
