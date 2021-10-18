package com.redhat.training.sensors.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.training.sensors.model.SensorMeasurementTaken;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;


@ApplicationScoped
public class SensorMeasurementReplicatorService {

    private static final Logger LOGGER = Logger.getLogger(SensorMeasurementReplicatorService.class.getName());

    @Incoming("garden-sensor-measurements-in")
    @Outgoing("garden-sensor-measurements-repl")
    public JsonNode replicate(SensorMeasurementTaken event) throws JsonProcessingException {
        LOGGER.info("Sensor measurement event replicated: " + event);
        return new ObjectMapper().readTree(event.toString());
    }


}
