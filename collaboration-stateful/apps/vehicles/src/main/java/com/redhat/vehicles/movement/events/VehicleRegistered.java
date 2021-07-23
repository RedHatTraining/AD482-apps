package com.redhat.vehicles.movement.events;

/**
 * This event occurs when a new vehicle is registered in the system
 */
public class VehicleRegistered {
    int id;
    String type;
    String model;

    public VehicleRegistered(int id, String model) {
        this.id = id;
        this.model = model;
    }
}