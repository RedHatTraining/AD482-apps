package com.redhat.vehicles.movement.tracker;

public class Vehicle {
    public int id;
    public String type;
    public String model;

    public Vehicle(int id, String type, String model) {
        this.id = id;
        this.type = type;
        this.model = model;
    }

    public String toString() {
        return "VEHICLE - Id: " + id + ", Type: '" + type + "', Model: '" + model + "'";
    }
}
