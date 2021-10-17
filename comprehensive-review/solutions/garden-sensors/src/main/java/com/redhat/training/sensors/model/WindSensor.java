package com.redhat.training.sensors.model;

public class WindSensor extends Sensor{

    @Override
    public SensorType getType() {
        return SensorType.WIND;
    }

}
