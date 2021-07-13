package com.redhat.training.bank.command;

public class DepositAmountInBankAccount {
    public Long id;
    public int amount;

    public DepositAmountInBankAccount() {
    }

    public DepositAmountInBankAccount(Long id, int amount) {
        this.id     = id;
        this.amount = amount;
    }
}
