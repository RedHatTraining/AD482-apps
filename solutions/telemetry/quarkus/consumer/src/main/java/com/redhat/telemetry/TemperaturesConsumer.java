package com.redhat.telemetry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;

import io.smallrye.mutiny.Multi;


@ApplicationScoped
public class TemperaturesConsumer {

    @Inject
    @Channel("temperatures")
    Multi<Integer> temperatures;

    public Multi<Integer> consume() {
        return temperatures;
    }

}
