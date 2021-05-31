package com.redhat.telemetry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;

import io.reactivex.Flowable;


@ApplicationScoped
public class TemperaturesConsumer {

    @Inject
    @Channel("temperatures")
    Flowable<Integer> temperatures;

    public Flowable<Integer> consume() {
        return temperatures;
    }

}
