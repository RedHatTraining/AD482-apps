package com.redhat.garden.back.resource;

import com.redhat.garden.back.event.*;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("garden")
public class GardenResource {

    @Inject @Channel("in-memory-garden-alerts")
    Publisher<GardenEvent> gardenEvents;

    @Inject @Channel("in-memory-garden-statuses")
    Publisher<GardenStatus> gardenStatuses;

    // Event processors ------------------------------------------------------------------------------------------------

    @Incoming("garden-low-temperature-alerts")
    @Outgoing("in-memory-garden-alerts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public GardenEvent processLowTemperatureAlerts(LowTemperatureDetected event) {
        return new GardenEvent(

        );
    }

    @Incoming("garden-dry-soil-alerts")
    @Outgoing("in-memory-garden-alerts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public GardenEvent processDrySoilAlerts(DrySoilDetected event) {
        return new GardenEvent(

        );
    }

    @Incoming("garden-low-nutrients-alerts")
    @Outgoing("in-memory-garden-alerts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public GardenEvent processLowNutrientsAlerts(LowNutrientsDetected event) {
        return new GardenEvent(

        );
    }

    // Endpoints -------------------------------------------------------------------------------------------------------

    @GET
    @Path("events")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<GardenEvent> getGardenEvents() {
        return gardenEvents;
    }

    @GET
    @Path("statuses")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<GardenStatus> getGardenStatuses() {
        return gardenStatuses;
    }
}
