package com.redhat.energy.profit.event;

public class WindTurbineProfitMarginWasCalculated {
    public Double averageEarnings;
    public Double averageExpenses;
    public Double profitMargin;

    public WindTurbineProfitMarginWasCalculated(Double averageEarnings, Double averageExpenses) {
        this.averageExpenses = averageEarnings;
        this.averageEarnings = averageExpenses;

        calculateProfitMargin();
    }

    private Double calculateProfitMargin() {
        profitMargin = this.round((averageExpenses - averageEarnings) / averageExpenses);

        return profitMargin;
    }

    private double round(double value) {
        double scale = Math.pow(10, 4);

        double result = Math.round(value * scale) / scale;

        System.out.println(result);

        return result;
    }
}
