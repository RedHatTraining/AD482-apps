package com.redhat.garden.simulator;

import com.redhat.garden.simulator.event.MeasurementWasTaken;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@ApplicationScoped
public class HumiditySimulator extends MeasurementSimulator {
    @Outgoing("sensor-gateway-write")
    public Multi<Record<Integer, MeasurementWasTaken>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1)) // TBD: Duration
                .onOverflow().drop()
                .map(tick -> {
                    int deviceID = getRandomDeviceId();
                    MeasurementWasTaken event = new MeasurementWasTaken(
                            deviceID,
                            getRandomGardenId(),
                            MEASUREMENT_TYPE_HUMIDITY,
                            0.0
                    );

                    logMeasurement(event);

                    return Record.of(
                            deviceID, // TBD: Are we going to use a record key?
                            event
                    );
                });
    }
}
