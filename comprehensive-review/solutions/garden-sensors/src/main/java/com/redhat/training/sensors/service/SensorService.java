package com.redhat.training.sensors.service;

import com.redhat.training.sensors.model.HumiditySensor;
import com.redhat.training.sensors.model.Sensor;
import com.redhat.training.sensors.model.TemperatureSensor;
import com.redhat.training.sensors.model.WindSensor;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class SensorService {

    private final List<Sensor> VALUES = Arrays.asList(
            new HumiditySensor(),
            new TemperatureSensor(),
            new WindSensor()
    );

    public Sensor getSensor() {
        Random random = new Random();
        int index = random.nextInt(VALUES.size());
        return VALUES.get(index);
    }

}
