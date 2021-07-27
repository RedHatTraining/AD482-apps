package com.redhat.vehicles;

import java.util.ArrayList;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.StoreQueryParameters;


@Path("/vehicle")
public class VehicleResource {

    @Inject
    KafkaStreams streams;

    @Inject VehicleManager vehicleManager;

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
    public void add(Vehicle vehicle) {
        vehicleManager.register(vehicle);
    }

}