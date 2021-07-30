package com.redhat.vehicles.movement.tracker;

import com.redhat.vehicles.inventory.Vehicle;

public class VehicleStatus {
    public Vehicle vehicle;
    public float latitude;
    public float longitude;
    public float elevation; // In meters

    public VehicleStatus(Vehicle vehicle, float latitude, float longitude, float elevation) {
        this.vehicle = vehicle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    public String toString() {
        return String.format(
            "\n [VEHICLE STATUS] \n  " + vehicle + "\n  Latitude: %.2f\n  Longitude:  %.2f\n  Elevation:  %.2f meters \n",
            latitude,
            longitude,
            elevation
        );
    }
}
