package com.redhat.training.gardens.util;

import java.util.Random;

public class SensorIdUtil {

    public static int getRandomId(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private SensorIdUtil() {
        // avoid direct instantiation
    }
}
