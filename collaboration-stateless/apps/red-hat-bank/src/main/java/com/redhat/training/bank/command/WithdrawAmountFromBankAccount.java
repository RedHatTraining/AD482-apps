package com.redhat.training.bank.command;

public class WithdrawAmountFromBankAccount {
    public Long bankAccountId;
    public Long amount;

    public WithdrawAmountFromBankAccount() {
    }

    public WithdrawAmountFromBankAccount(Long id, Long amount) {
        this.bankAccountId = id;
        this.amount = amount;
    }
}
