package com.redhat.training.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "requests")
@Cacheable
public class RepairRequest extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requester_name")
    private String requesterName;

    @Column(name="request_date")
    private LocalDateTime requestDate;

    @Column
    @Enumerated(EnumType.STRING)
    private RepairRequestStatus status;

    @Column(name="plumber_id")
    private Long plumberId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public RepairRequestStatus getStatus() {
        return status;
    }

    public void setStatus(RepairRequestStatus status) {
        this.status = status;
    }

    public Long getPlumberId() {
        return plumberId;
    }

    public void setPlumberId(Long plumberId) {
        this.plumberId = plumberId;
    }


    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
