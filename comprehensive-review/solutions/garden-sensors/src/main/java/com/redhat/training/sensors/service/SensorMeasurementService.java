package com.redhat.training.sensors.service;

import com.redhat.training.sensors.model.Sensor;
import com.redhat.training.sensors.model.SensorMeasurementTaken;
import com.redhat.training.sensors.model.SensorMeasurementType;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;

@ApplicationScoped
public class SensorMeasurementService {

    @Inject
    private SensorService sensorService;

    @Outgoing("garden-sensor-measurements")
    public Multi<Record<Integer, SensorMeasurementTaken>> measure() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(3000))
                .onOverflow().drop()
                .map(tick -> {
                    SensorMeasurementTaken event = generateEvent(sensorService.getSensor());
                    return Record.of(event.getSensorId(), event);
                });
    }

    private SensorMeasurementTaken generateEvent(Sensor sensor) {
        return new SensorMeasurementTaken(sensor.getId(), sensor.getValue(), Instant.now().toEpochMilli(), SensorMeasurementType.valueOf(sensor.getType().name()));
    }

}
