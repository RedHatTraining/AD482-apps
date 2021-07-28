package com.redhat.vehicles.inventory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.redhat.vehicles.events.VehicleRegistered;

import org.eclipse.microprofile.reactive.messaging.Emitter;


public class TestVehicleInventory {

    VehicleInventory vehicleManager;
    Emitter<VehicleRegistered> emitter;

    @BeforeEach
    public void setup() {
        vehicleManager = new VehicleInventory();
    }

    @Test
    public void testRegisterThrowsExceptionIfTypeIsEmpty() {
        // TODO: implement exception test case
    }

    @Test
    public void testRegisterReturnsVehicleRegistered() throws InvalidVehicleException {
        // TODO: implement VehicleRegistered test case
    }

}
