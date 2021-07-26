package com.redhat.training.bank.event;

public class HighRiskWithdrawnWasDetected {
    public Long bankAccountId;
    public Long amount;

    public HighRiskWithdrawnWasDetected() {}

    public HighRiskWithdrawnWasDetected(Long bankAccountId, Long amount) {
        this.bankAccountId = bankAccountId;
        this.amount = amount;
    }
}
