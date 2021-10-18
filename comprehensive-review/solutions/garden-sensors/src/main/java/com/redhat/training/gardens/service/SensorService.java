package com.redhat.training.gardens.service;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.training.gardens.model.HumiditySensor;
import com.redhat.training.gardens.model.Sensor;
import com.redhat.training.gardens.model.TemperatureSensor;
import com.redhat.training.gardens.model.WindSensor;

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
