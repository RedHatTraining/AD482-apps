package com.redhat.training.sensors.model;

public class WindSensor extends Sensor{

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public int getMinValue() {
        return 1;
    }

    @Override
    public int getMaxValue() {
        return 20;
    }

    @Override
    public SensorType getType() {
        return SensorType.WIND;
    }

}
