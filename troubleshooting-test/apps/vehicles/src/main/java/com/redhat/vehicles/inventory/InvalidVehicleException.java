package com.redhat.vehicles.inventory;

public class InvalidVehicleException extends Exception {

    public Vehicle vehicle;

    InvalidVehicleException(Vehicle vehicle) {
        super("Invalid vehicle: " + vehicle);
        this.vehicle = vehicle;
    }
}
