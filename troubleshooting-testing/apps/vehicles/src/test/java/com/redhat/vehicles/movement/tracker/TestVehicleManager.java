package com.redhat.vehicles.movement.tracker;

import static org.mockito.ArgumentMatchers.argThat;

import javax.inject.Inject;

import com.redhat.vehicles.Vehicle;
import com.redhat.vehicles.VehicleManager;
import com.redhat.vehicles.VehicleRegistered;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;


public class TestVehicleManager {

    VehicleManager vehicleManager;
    Emitter<VehicleRegistered> emitter;

    // @InjectMock
    // Emitter<VehicleRegistered> emitter;

    @BeforeEach
    public void setup() {
        emitter = Mockito.mock(Emitter.class);
        // // Mockito.when(mock.greet("Stuart")).thenReturn("A mock for Stuart");
        // QuarkusMock.installMockForType(mock, Emitter.class);
        vehicleManager = new VehicleManager(emitter);
    }

    @Test
    public void testRegistersNewVehicle() {
        Vehicle vehicle = new Vehicle(2, "car", "Test Car");
        vehicleManager.register(vehicle);

        Mockito.verify(emitter, Mockito.times(1)).send(
            argThat((VehicleRegistered event) -> 2 == event.id)
        );
    }

}
