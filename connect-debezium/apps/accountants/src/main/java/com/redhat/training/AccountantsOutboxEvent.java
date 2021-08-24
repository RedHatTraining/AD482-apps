package com.redhat.training;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.Type;

@Entity(name = "accountants_outbox")
public class AccountantsOutboxEvent extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "aggregatetype")
    private String aggregateType;

    @Column(name = "aggregateid")
    private String aggregateId;

    @Column(name = "payload")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String payload;

    @Column(name = "content_type")
    private String contentType;

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
