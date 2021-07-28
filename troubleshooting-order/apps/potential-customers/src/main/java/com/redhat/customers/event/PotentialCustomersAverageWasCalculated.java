package com.redhat.customers.event;

public class PotentialCustomersAverageWasCalculated {
    public double total;
    public long measurements;

    public PotentialCustomersAverageWasCalculated update(PotentialCustomersWereDetected measure) {
        measurements++;
        total += measure.amount;

        return this;
    }
}
