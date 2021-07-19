package com.redhat.vehicles;

public class VehiclePosition {
    public int vehicleId;
    public double latitude;
    public double longitude;
    public double elevation; // In meters

    public String toString() {
        return String.format(
            "\n  Vehicle Id: %d\n  Latitude: %.2f\n  Longitude:  %.2f\n  Elevation:  %.2f meters \n",
            vehicleId,
            latitude,
            longitude,
            elevation
        );

    }
}
