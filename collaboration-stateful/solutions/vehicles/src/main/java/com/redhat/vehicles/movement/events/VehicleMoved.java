package com.redhat.vehicles.movement.events;

/**
 * This event occurs when a vehicle moves
 */
public class VehicleMoved {
    public int vehicleId;
    public float latitude;
    public float longitude;
    public float elevation; // In meters

    public VehicleMoved(int vehicleId, float latitude, float longitude, float elevation) {
        this.vehicleId = vehicleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    public String toString() {
        return String.format(
            "\n [VEHICLE MOVED] \n  Vehicle Id: %d\n  Latitude: %.2f\n  Longitude:  %.2f\n  Elevation:  %.2f meters \n",
            vehicleId,
            latitude,
            longitude,
            elevation
        );
    }
}
