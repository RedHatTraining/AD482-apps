package com.redhat.training.bank.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

@Entity
@Cacheable
public class BankAccount extends PanacheEntity {

    public Long balance;

    public String profile;

    public BankAccount() {
    }

    public BankAccount(Long balance, String profile) {
        this.balance = balance;
        this.profile = profile;
    }
}
