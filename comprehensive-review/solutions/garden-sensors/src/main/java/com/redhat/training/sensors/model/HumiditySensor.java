package com.redhat.training.sensors.model;

public class HumiditySensor extends Sensor{

    @Override
    public SensorType getType() {
        return SensorType.HUMIDITY;
    }

}
