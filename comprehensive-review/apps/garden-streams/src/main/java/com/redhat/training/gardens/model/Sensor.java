package com.redhat.training.gardens.model;

public class Sensor {
    public Integer id;
    public String name;
    public String type;
    public String customer;
    public String garden;

    public Sensor() {}

    public Sensor(Integer id, String name, String type, String customer, String garden) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.customer = customer;
        this.garden = garden;
    }
}
