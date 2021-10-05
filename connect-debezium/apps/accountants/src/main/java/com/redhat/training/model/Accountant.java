package com.redhat.training.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.json.JsonObject;

import javax.persistence.*;

@Entity(name = "accountants")
@Cacheable
public class Accountant extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private Integer ssn;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountantStatus status;

    public Accountant() {
    }

    public Accountant(String username, Integer ssn, AccountantStatus status) {
        this.username = username;
        this.ssn = ssn;
        this.status = status;
    }

    public Long getId() {
        return id;
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

    public AccountantStatus getStatus() {
        return status;
    }

    public void setStatus(AccountantStatus status) {
        this.status = status;
    }

}
