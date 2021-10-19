package com.redhat.garden.back.resource;

import com.redhat.garden.back.measurement.SensorMeasurement;
import com.redhat.garden.back.measurement.SensorMeasurementEnriched;
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

@Path("sensor")
public class SensorResource {

    @Inject @Channel("in-memory-garden-enriched-sensor-measurements")
    Publisher<SensorMeasurementEnriched> enrichedSensorMeasurements;

    // Event processors ------------------------------------------------------------------------------------------------

    @Incoming("garden-sensor-measurements-enriched")
    @Outgoing("in-memory-garden-enriched-sensor-measurements")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public SensorMeasurementEnriched consumeEnrichedSensorMeasurements(SensorMeasurementEnriched event) {
        return event;
    }

    // Endpoints -------------------------------------------------------------------------------------------------------

    @GET
    @Path("measurements")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<SensorMeasurementEnriched> getEnrichedSensorMeasurements() {
        return enrichedSensorMeasurements;
    }
}
