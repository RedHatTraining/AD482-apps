package com.redhat.training.sensors.model;

import com.redhat.training.sensors.util.SensorIdUtil;
import com.redhat.training.sensors.util.SensorValueUtil;

public abstract class Sensor {

    public abstract int getMinIndex();

    public abstract int getMaxIndex();

    public int getId() {
        return SensorIdUtil.getRandomId(getMinIndex(), getMaxIndex());
    }

    public abstract SensorType getType();

    public double getValue() {
        return SensorValueUtil.getRandomValue();
    }


}
