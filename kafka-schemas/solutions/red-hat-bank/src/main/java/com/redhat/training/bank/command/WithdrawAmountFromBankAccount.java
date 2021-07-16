package com.redhat.training.bank.command;

public class WithdrawAmountFromBankAccount {
    public Long id;
    public int amount;

    public WithdrawAmountFromBankAccount() {
    }

    public WithdrawAmountFromBankAccount(Long id, int amount) {
        this.id     = id;
        this.amount = amount;
    }
}
