package com.redhat.training.service;

import com.redhat.training.model.Plumber;
import com.redhat.training.model.PlumberStatus;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlumberService {
    public Plumber findAvailable() {
        return (Plumber) Plumber.list("status", PlumberStatus.AVAILABLE).get(0);
    }
}
