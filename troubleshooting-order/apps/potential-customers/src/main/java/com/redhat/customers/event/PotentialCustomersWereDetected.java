package com.redhat.customers.event;

public class PotentialCustomersWereDetected {
    public String locationId;
    public Integer amount;

    public PotentialCustomersWereDetected() {}

    public PotentialCustomersWereDetected(String locationId, Integer amount) {
        this.locationId = locationId;
        this.amount = amount;
    }
}
