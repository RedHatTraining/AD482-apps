package com.redhat.training.bank.event;

public class HighValueDepositWasDetected {
    public Long bankAccountId;
    public Long amount;

    public HighValueDepositWasDetected() {}

    public HighValueDepositWasDetected(Long bankAccountId, Long amount) {
        this.bankAccountId = bankAccountId;
        this.amount = amount;
    }
}
