package com.redhat.vehicles.inventory;

import java.util.ArrayList;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import com.redhat.vehicles.events.VehicleRegistered;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.StoreQueryParameters;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import io.smallrye.reactive.messaging.kafka.Record;



@Path("/vehicle")
public class VehicleResource {

    @Inject
    KafkaStreams streams;

    @Inject VehicleInventory vehicleManager;

    @Channel("vehicle-registered")
    Emitter<Record<Integer, VehicleRegistered>> emitter;

    @GET
    public List<Vehicle> list() {
        ReadOnlyKeyValueStore<Integer, Vehicle> store = streams
            .store(StoreQueryParameters.fromNameAndType(
                "vehicles-store",
                QueryableStoreTypes.keyValueStore()
            ));

        List<Vehicle> vehicles = new ArrayList<>();

        store.all()
            .forEachRemaining(row -> { vehicles.add(row.value); });

        return vehicles;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void add(Vehicle vehicle) throws InvalidVehicleException {
        VehicleRegistered event = vehicleManager.register(vehicle);
        emitter.send(Record.of(event.id, event));
    }

}