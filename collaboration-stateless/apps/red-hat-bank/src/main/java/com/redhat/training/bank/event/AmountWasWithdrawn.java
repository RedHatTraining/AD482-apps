package com.redhat.training.bank.event;

public class AmountWasWithdrawn {
    public Long bankAccountId;
    public Long amount;

    public AmountWasWithdrawn() {}

    public AmountWasWithdrawn(Long bankAccountId, Long amount) {
        this.bankAccountId = bankAccountId;
        this.amount = amount;
    }
}
