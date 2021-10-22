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

    // TODO: Implement a Kafka consumer that returns "SensorMeasurementEnriched" data.
    //  Stream messages to an outgoing channel called "in-memory-garden-sensor-measurements-enriched"
    @Incoming("garden-sensor-measurements-enriched")
    @Outgoing("in-memory-garden-sensor-measurements-enriched")
    @Broadcast
    public SensorMeasurementEnriched
            consumeEnrichedSensorMeasurements(SensorMeasurementEnriched event) {
        return event;
    }

    // TODO: Implement a Kafka consumer that returns "SensorMeasurement" data.
    //  Stream messages to an outgoing channel called "in-memory-garden-sensor-measurements-raw"
    @Incoming("garden-sensor-measurements-raw")
    @Outgoing("in-memory-garden-sensor-measurements-raw")
    @Broadcast
    public SensorMeasurement consumeRawSensorMeasurements(SensorMeasurementTaken event)
            throws JsonProcessingException {
        SensorMeasurement sensorMeasurement = createSensorMeasurementFromEvent(event);
        return sensorMeasurement;
    }

    private SensorMeasurement createSensorMeasurementFromEvent(SensorMeasurementTaken event) throws JsonProcessingException {
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
