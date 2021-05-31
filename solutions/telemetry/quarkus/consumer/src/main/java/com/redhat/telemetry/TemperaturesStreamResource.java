package com.redhat.telemetry;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestSseElementType;

import io.smallrye.mutiny.Multi;

@Path("/temperatures")
public class TemperaturesStreamResource {

    @Inject
    TemperaturesConsumer consumer;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestSseElementType(MediaType.TEXT_PLAIN)
    public Multi<String> getTemperaturesStream() {
        return consumer.consume().map(temp -> "Received temperature: " + temp).toHotStream();
    }
}
