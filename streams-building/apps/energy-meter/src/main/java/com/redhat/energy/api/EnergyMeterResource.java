package com.redhat.energy.api;

import com.redhat.energy.records.MWattsMeasurement;
import com.redhat.energy.records.WindTurbine;
import com.redhat.energy.records.WindTurbineStats;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.eclipse.microprofile.reactive.messaging.Channel;

import java.util.List;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

@Path( "/turbines" )
public class EnergyMeterResource {

    @Inject
    KafkaStreams streams;

    @Inject
    @Channel("turbines-generated-mwatts")
    Publisher<MWattsMeasurement> generatedPowerValues;

    @Inject
    @Channel("turbine-stats")
    Publisher<WindTurbineStats> turbineStats;

    @GET
    @Path("/")
    public List<WindTurbine> list() {
        List<WindTurbine> turbines = new ArrayList<>();

        ReadOnlyKeyValueStore<Integer, WindTurbine> store = streams
            .store(
                StoreQueryParameters.fromNameAndType(
                    "turbines-store",
                    QueryableStoreTypes.<Integer, WindTurbine>keyValueStore()
                )
            );

        store
            .all()
            .forEachRemaining( row -> {
                turbines.add( row.value );
            } );

        return turbines;
    }

    @GET
    @Path("/generated-power")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<MWattsMeasurement> stream() {
        return generatedPowerValues;
    }


    @GET
    @Path("/measurements-count")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<WindTurbineStats> countStream() {
        return turbineStats;
    }


}