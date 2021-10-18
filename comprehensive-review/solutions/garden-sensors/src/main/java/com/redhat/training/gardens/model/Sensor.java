package com.redhat.training.gardens.model;

import com.redhat.training.gardens.util.SensorIdUtil;
import com.redhat.training.gardens.util.SensorValueUtil;

public abstract class Sensor {

    public abstract int getMinValue();

    public abstract int getMaxValue();

    public abstract int getId();

    public abstract SensorType getType();

    public double getValue() {
        return SensorValueUtil.getRandomValue(getMinValue(), getMaxValue());
    }


}
