package com.redhat;

public class VehiclePosition {
    public int id;
    public double latitude;
    public double longitude;
    public double elevation;

    public String toString() {
        return String.format(
            "\n  Vehicle Id: %d\n  Latitude: %.2f\n  Longitude:  %.2f\n  Elevation:  %.2f meters \n",
            id,
            latitude,
            longitude,
            elevation
        );

    }
}
