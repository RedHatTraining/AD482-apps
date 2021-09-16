package com.redhat.energy.profit.model;

public class AverageData {
    public Integer count = 0;
    public Double sum = 0.0;

    public AverageData() {
    }

    public AverageData(Integer count, Double sum) {
        this.count = count;
        this.sum = sum;
    }

    public void increaseCount(Integer increase) {
        this.count += increase;
    }

    public void increaseSum(Double increase) {
        this.sum += increase;
    }
}
