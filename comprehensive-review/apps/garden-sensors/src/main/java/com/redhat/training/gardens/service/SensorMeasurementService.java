package com.redhat.training.gardens.service;

import com.redhat.training.gardens.model.Sensor;
import com.redhat.training.gardens.event.SensorMeasurementTaken;
import com.redhat.training.gardens.event.SensorMeasurementType;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

@ApplicationScoped
public class SensorMeasurementService {

    private static final Logger LOGGER = Logger.getLogger(SensorMeasurementService.class.getName());

    @Inject
    private SensorService sensorService;

    // TODO: Implement the Kafka producer

    private SensorMeasurementTaken generateEvent(Sensor sensor) {
        return new SensorMeasurementTaken(sensor.getId(), sensor.getValue(), Instant.now().toEpochMilli(),
                SensorMeasurementType.valueOf(sensor.getType().name()));
    }

}
