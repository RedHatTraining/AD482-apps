package com.redhat.training.gardens.model;

public class HumiditySensor extends Sensor{

    @Override
    public int getMinIndex() {
        return 1;
    }

    @Override
    public int getMaxIndex() {
        return 6;
    }

    @Override
    public SensorType getType() {
        return SensorType.HUMIDITY;
    }

}
