package com.redhat.vehicles.movement.tracker;

import com.redhat.vehicles.Vehicle;

public class VehicleMetrics {

    public Vehicle vehicle;
    public int movementsReported = 0;
    public double initialLatitude;
    public double initialLongitude;
    public double finalLatitude;
    public double finalLongitude;
    public double latitudeDelta;
    public double longitudeDelta;

    public VehicleMetrics update(VehicleStatus value) {
        vehicle = value.vehicle;

        if (movementsReported == 0) {
            initialLatitude = value.latitude;
            initialLongitude = value.longitude;
        }

        finalLatitude = value.latitude;
        finalLongitude = value.longitude;

        latitudeDelta = finalLatitude - initialLatitude;
        longitudeDelta = finalLongitude - initialLongitude;

        movementsReported++;

        return this;
    }
}
