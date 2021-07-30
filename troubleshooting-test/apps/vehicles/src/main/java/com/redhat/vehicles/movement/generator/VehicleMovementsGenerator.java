package com.redhat.vehicles.movement.generator;

import java.util.List;
import java.util.Arrays;
import java.time.Duration;
import java.util.Collections;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.vehicles.events.VehicleMoved;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

/**
 * This class simulates multiple vehicles reporting their movements
 */
@ApplicationScoped
public class VehicleMovementsGenerator {

    private List<VehiclePosition> positions;
    private Random random = new Random();

    public VehicleMovementsGenerator() {
        // Only three vehicles for this exercise
        positions = Collections.unmodifiableList(
            Arrays.asList(
                new VehiclePosition(1),
                new VehiclePosition(2),
                new VehiclePosition(3)
            )
        );
    }

    @Outgoing("vehicle-moved")
    public Multi<Record<Integer, VehicleMoved>> generate() {

        return Multi.createFrom().ticks().every(Duration.ofMillis(3000))
                .onOverflow().drop()
                .map(tick -> {
                    VehiclePosition position = positions.get(random.nextInt(positions.size()));

                    position.move();

                    VehicleMoved event = new VehicleMoved(
                        position.vehicleId,
                        position.latitude,
                        position.longitude,
                        position.elevation
                    );

                    // Event produced with the vehicle id as key
                    return Record.of(event.vehicleId, event);
                });
    }
}

