package com.redhat.vehicles.events;

public class VehicleRegistered {
    public int id;
    public String type;
    public String model;

    public VehicleRegistered(int id, String type, String model) {
        this.id = id;
        this.type = type;
        this.model = model;
    }
}