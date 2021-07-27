package com.redhat.vehicles;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class VehicleManager {

    // @Inject
    // @Channel("vehicles")
    Emitter<VehicleRegistered> emitter;

    public VehicleManager(
        @Channel("vehicles")
        Emitter<VehicleRegistered> emitter) {
            this.emitter = emitter;
        }

    public void register(Vehicle vehicle) {

        if (vehicle.id < 0) {
            // TODO
        }

        emitter.send(new VehicleRegistered(vehicle.id, vehicle.type, vehicle.model));
    }
}
