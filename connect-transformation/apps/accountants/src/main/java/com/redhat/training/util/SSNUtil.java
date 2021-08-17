package com.redhat.training.util;

import java.util.Random;

public class SSNUtil {

    public static Integer getRandomSSN() {
        Random r = new Random();
        int low = 100000000;
        int high = 999999999;
        return r.nextInt(high - low) + low;
    }

    private SSNUtil() {
        // avoid direct instantiation
    }
}
