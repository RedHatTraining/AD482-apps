package com.redhat.garden.sensors;

public class Sensor {
    public Integer id;
    public String name;
    public String gardenName;

    public Sensor() {}

    public Sensor(Integer id, String name, String gardenName) {
        this.id = id;
        this.name = name;
        this.gardenName = gardenName;
    }
}
