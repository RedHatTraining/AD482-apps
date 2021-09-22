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

    // TODO: implement exception test case
    @Test
    public void testRegisterThrowsExceptionIfTypeIsEmpty() {
        // Given
        Vehicle vehicle = new Vehicle(2, "", "Test Car");

        // When
        Exception exception = assertThrows(InvalidVehicleException.class, () -> {
            vehicleManager.register(vehicle);
        });

        // Then
        assertThat(exception.getMessage(), containsString("Invalid vehicle"));
    }

    // TODO: implement VehicleRegistered test case
    @Test
    public void testRegisterReturnsVehicleRegistered() throws InvalidVehicleException {
        // Given
        Vehicle vehicle = new Vehicle(2, "car", "Test Car");

        // When
        VehicleRegistered event = vehicleManager.register(vehicle);

        // Then
        assertEquals(2, event.id);
        assertEquals("car", event.type);
        assertEquals("Test Car", event.model);
    }

}
