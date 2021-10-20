package com.redhat.training.gardens.rest;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;

import java.util.List;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.redhat.training.gardens.model.Sensor;


@Path("/store")
public class StoreResource {

    @Inject
    KafkaStreams streams;

    @GET
    @Path("/")
    public List<Sensor> list() {
        List<Sensor> sensors = new ArrayList<>();

        streams.store(StoreQueryParameters.fromNameAndType("sensors-store",
            QueryableStoreTypes.<Integer, Sensor>keyValueStore())).all().forEachRemaining(row -> {
                sensors.add(row.value);
            });

        return sensors;
    }

}
