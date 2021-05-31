package com.redhat.telemetry;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Publisher;

@Path("/temperatures")
public class TemperaturesStreamResource {

    @Inject
    TemperaturesConsumer consumer;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<String> getTemperaturesStream() {
        return consumer.consume().map(temp -> "Received temperature: " + temp);
    }
}