package com.redhat.training.gardens.model;

import com.redhat.training.sensors.util.SensorIdUtil;

public class TemperatureSensor extends Sensor{

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public int getMinValue() {
        return -20;
    }

    @Override
    public int getMaxValue() {
        return 45;
    }

    @Override
    public SensorType getType() {
        return SensorType.TEMPERATURE;
    }

}
