package com.redhat.energy.records;

public class WindTurbine {
    public Integer id;
    public String description;
    public Integer powerCapacity;
    public Integer cutOutWindSpeed;

    public WindTurbine() {}

    public WindTurbine(Integer id, String description, Integer powerCapacity, Integer cutOutWindSpeed) {
        this.id = id;
        this.description = description;
        this.powerCapacity = powerCapacity;
        this.cutOutWindSpeed = cutOutWindSpeed;
    }
}
