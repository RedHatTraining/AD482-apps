package com.redhat.training.bank.event;

public class LowRiskWithdrawnWasDetected {
    public Long bankAccountId;
    public Long amount;

    public LowRiskWithdrawnWasDetected() {}

    public LowRiskWithdrawnWasDetected(Long bankAccountId, Long amount) {
        this.bankAccountId = bankAccountId;
        this.amount = amount;
    }
}
