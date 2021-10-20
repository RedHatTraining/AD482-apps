package com.redhat.training.gardens.rest;

import com.redhat.training.gardens.event.GardenStatusEvent;
import com.redhat.training.gardens.event.LowHumidityDetected;
import com.redhat.training.gardens.event.LowTemperatureDetected;
import com.redhat.training.gardens.event.StrongWindDetected;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("garden")
public class GardenResource {

    @Inject @Channel("garden-low-temperature-events")
    Publisher<LowTemperatureDetected> gardenTemperatureEvents;

    @Inject @Channel("garden-low-humidity-events")
    Publisher<LowHumidityDetected> gardenHumidityEvents;

    @Inject @Channel("garden-strong-wind-events")
    Publisher<StrongWindDetected> gardenWindEvents;

    @Inject @Channel("garden-status-events")
    Publisher<GardenStatusEvent> gardenStatuses;


    // Endpoints -------------------------------------------------------------------------------------------------------

    @GET
    @Path("events/temperature")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<LowTemperatureDetected> getGardenTemperatureEvents() {
        return gardenTemperatureEvents;
    }

    @GET
    @Path("events/humidity")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<LowHumidityDetected> getGardenHumidityEvents() {
        return gardenHumidityEvents;
    }

    @GET
    @Path("events/wind")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<StrongWindDetected> getGardenWindEvents() {
        return gardenWindEvents;
    }

    @GET
    @Path("statuses")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<GardenStatusEvent> getGardenStatuses() {
        return gardenStatuses;
    }
}
