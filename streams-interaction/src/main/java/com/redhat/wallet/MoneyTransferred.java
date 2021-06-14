package com.redhat.wallet;

import java.time.Instant;

class MoneyTransferred {
    public int sourceWalletId;
    public int destinationWalletId;
    // public Instant timestamp;
    public double amount;
    public String currency;

    MoneyTransferred(int sourceWalletId, int destinationWalletId, Instant timestamp, double amount, String currency) {
        this.sourceWalletId = sourceWalletId;
        this.destinationWalletId = destinationWalletId;
        // this.timestamp = timestamp;
        this.amount = amount;
        this.currency = currency;
    }
}