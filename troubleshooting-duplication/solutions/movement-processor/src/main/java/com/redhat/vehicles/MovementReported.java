package com.redhat.vehicles;

class MovementReported {
    public Integer distance;
    public Integer time;

    @Override
    public String toString() {
        return " { distance: " + distance + ", time: " + time + " }";
    }
}