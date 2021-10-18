package com.redhat.training.sensors.model;

public class WindSensor extends Sensor{

    @Override
    public int getMinIndex() {
        return 11;
    }

    @Override
    public int getMaxIndex() {
        return 16;
    }

    @Override
    public SensorType getType() {
        return SensorType.WIND;
    }

}
