package com.redhat.vehicles.movement.generator;

import java.util.Random;

public class VehiclePosition {
    public int vehicleId;
    public float latitude;
    public float longitude;
    public float elevation; // In meters
    private Random random;

    public VehiclePosition(int vehicleId) {
        this.vehicleId = vehicleId;

        random = new Random();
        latitude = getRandomFloat(37, 42);
        longitude = getRandomFloat(-2, 5);
        elevation = 0;
    }

    public void move() {
        float delta = 0.001f;
        latitude += getRandomFloat(-delta, delta);
        longitude += getRandomFloat(-delta, delta);

        // For this exercise, only the vehicle with id 3 can fly
        // See: AD482-apps/collaboration-stateful/scripts/produce_vehicles.py
        if (vehicleId == 3) {
            elevation += getRandomFloat(-1, 1);
            if (elevation < 0) {
                elevation  = 0;
            }
        }
    }

    private float getRandomFloat(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }
}
