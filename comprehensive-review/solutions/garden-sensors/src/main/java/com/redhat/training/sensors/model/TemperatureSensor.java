package com.redhat.training.sensors.model;

public class TemperatureSensor extends Sensor{

    @Override
    public SensorType getType() {
        return SensorType.TEMPERATURE;
    }

}
