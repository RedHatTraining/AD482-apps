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
        profitMargin = (averageExpenses - averageEarnings) / averageExpenses;

        return profitMargin;
    }
}
