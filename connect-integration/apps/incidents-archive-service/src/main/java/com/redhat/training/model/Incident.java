package com.redhat.training.model;

import java.time.LocalDateTime;

public class Incident {

    private Long id;

    private String requesterName;

    private LocalDateTime requestDate;

    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
