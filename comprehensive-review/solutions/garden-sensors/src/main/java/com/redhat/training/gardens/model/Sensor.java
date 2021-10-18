package com.redhat.training.gardens.model;

import com.redhat.training.gardens.util.SensorIdUtil;
import com.redhat.training.gardens.util.SensorValueUtil;

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
