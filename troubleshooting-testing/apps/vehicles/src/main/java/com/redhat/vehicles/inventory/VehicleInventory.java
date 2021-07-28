package com.redhat.vehicles.inventory;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.vehicles.events.VehicleRegistered;


/**
 * Business logic to control vehicles inventory
 */
@ApplicationScoped
public class VehicleInventory {

    public VehicleRegistered register(Vehicle vehicle) throws InvalidVehicleException {

        if (vehicle.type.isEmpty()) {
            throw new InvalidVehicleException(vehicle);
        }

        return new VehicleRegistered(vehicle.id, vehicle.type, vehicle.model);
    }
}
