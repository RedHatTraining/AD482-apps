package com.redhat.training.gardens.util;

import java.util.Random;

public class SensorValueUtil {

    public static double getRandomValue(int leftLimit, int rightLimit) {
        Random random = new Random();
        return leftLimit + random.nextDouble() * (rightLimit - leftLimit);
    }

    private SensorValueUtil() {
        // avoid direct instantiation
    }
}
