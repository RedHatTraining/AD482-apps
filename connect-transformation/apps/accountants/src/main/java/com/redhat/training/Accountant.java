package com.redhat.training;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity(name="accountants")
@Cacheable
public class Accountant extends PanacheEntity {

    @Column
    private String username;

    @Column
    private Integer ssn;

    public Accountant() {
    }

    public Accountant(String username, Integer ssn) {
        this.username = username;
        this.ssn = ssn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSsn() {
        return ssn;
    }

    public void setSsn(Integer ssn) {
        this.ssn = ssn;
    }
}
