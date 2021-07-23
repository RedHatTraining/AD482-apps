package com.redhat.training.bank.command;

public class DepositAmountInBankAccount {
    public Long bankAccountId;
    public Long amount;

    public DepositAmountInBankAccount() {
    }

    public DepositAmountInBankAccount(Long bankAccountId, Long amount) {
        this.bankAccountId = bankAccountId;
        this.amount = amount;
    }
}
