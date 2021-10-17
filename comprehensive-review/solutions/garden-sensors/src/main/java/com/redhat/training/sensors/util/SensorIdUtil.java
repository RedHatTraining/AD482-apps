package com.redhat.training.sensors.util;

import java.util.Random;

public class SensorIdUtil {

    public static int getRandomId() {
        Random random = new Random();
        return random.nextInt(10);
    }

    private SensorIdUtil() {
        // avoid direct instantiation
    }
}
