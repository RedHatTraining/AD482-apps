package com.redhat.training.bank.event;

public class ModerateRiskWithdrawnWasDetected {
    public Long bankAccountId;
    public Long amount;

    public ModerateRiskWithdrawnWasDetected() {}

    public ModerateRiskWithdrawnWasDetected(Long bankAccountId, Long amount) {
        this.bankAccountId = bankAccountId;
        this.amount = amount;
    }
}
