package com.redhat.training.gardens.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.training.gardens.event.SensorMeasurementTaken;
import com.redhat.training.gardens.model.SensorMeasurement;
import com.redhat.training.gardens.model.SensorMeasurementEnriched;
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

    @Inject @Channel("in-memory-garden-sensor-measurements-enriched")
    Publisher<SensorMeasurementEnriched> enrichedSensorMeasurements;

    @Inject @Channel("in-memory-garden-sensor-measurements-raw")
    Publisher<SensorMeasurement> rawSensorMeasurements;

    // Event processors ------------------------------------------------------------------------------------------------

    @Incoming("garden-sensor-measurements-enriched")
    @Outgoing("in-memory-garden-sensor-measurements-enriched")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public SensorMeasurementEnriched consumeEnrichedSensorMeasurements(SensorMeasurementEnriched event) {
        return event;
    }

    @Incoming("garden-sensor-measurements-raw")
    @Outgoing("in-memory-garden-sensor-measurements-raw")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public SensorMeasurement consumeRawSensorMeasurements(SensorMeasurementTaken event) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(event.toString());
        SensorMeasurement sensorMeasurement = objectMapper.treeToValue(jsonNode, SensorMeasurement.class);
        return sensorMeasurement;
    }

    // Endpoints -------------------------------------------------------------------------------------------------------

    @GET
    @Path("measurements/enriched")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<SensorMeasurementEnriched> getEnrichedSensorMeasurements() {
        return enrichedSensorMeasurements;
    }

    @GET
    @Path("measurements/raw")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<SensorMeasurement> getRawSensorMeasurements() {
        return rawSensorMeasurements;
    }
}
