package com.redhat.cdr.model;

import java.io.Serializable;

public class CallDetailRecord implements Serializable {

    private int userId;
    private String record;

    public CallDetailRecord() {
    }

    public CallDetailRecord(int userId, String record) {
        this.userId = userId;
        this.record = record;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "CallDetailRecord{" +
                "userId=" + userId +
                ", record='" + record + '\'' +
                '}';
    }
}
