package com.redhat.training.bank.event;

public class AmountWasDeposited {
    public Long bankAccountId;
    public Long amount;

    public AmountWasDeposited() {}

    public AmountWasDeposited(Long bankAccountId, Long amount) {
        this.bankAccountId = bankAccountId;
        this.amount = amount;
    }
}
