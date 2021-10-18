package com.redhat.training.sensors.model;

public class TemperatureSensor extends Sensor{

    @Override
    public int getMinIndex() {
        return 6;
    }

    @Override
    public int getMaxIndex() {
        return 11;
    }

    @Override
    public SensorType getType() {
        return SensorType.TEMPERATURE;
    }

}
