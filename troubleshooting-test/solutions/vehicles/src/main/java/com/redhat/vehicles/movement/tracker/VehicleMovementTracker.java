package com.redhat.vehicles.movement.tracker;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.redhat.vehicles.events.VehicleMoved;
import com.redhat.vehicles.inventory.Vehicle;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class VehicleMovementTracker {

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        // Event Key SerDe (all events use the vehicle id as the Kafka record key)
        Serde<Integer> intSerde = Serdes.Integer();

        // Event Value SerDes
        ObjectMapperSerde<Vehicle> vehicleSerde = new ObjectMapperSerde<>(
            Vehicle.class
        );
        ObjectMapperSerde<VehicleMetrics> vehicleMetricsSerde = new ObjectMapperSerde<>(
            VehicleMetrics.class
        );
        ObjectMapperSerde<VehicleMoved> vehicleMovedSerde = new ObjectMapperSerde<>(
            VehicleMoved.class
        );
        ObjectMapperSerde<VehicleStatus> vehicleStatusSerde = new ObjectMapperSerde<>(
            VehicleStatus.class
        );

        GlobalKTable<Integer, Vehicle> vehiclesTable = builder.globalTable(
            "vehicle-registered",
            Materialized.<Integer, Vehicle, KeyValueStore<Bytes, byte[]>>as("vehicles-store")
                .withKeySerde(intSerde)
                .withValueSerde(vehicleSerde)
        );

        KStream<Integer, VehicleMoved> movementsStream = builder.stream(
            "vehicle-moved",
            Consumed.with(intSerde, vehicleMovedSerde)
        );

        KStream<Integer, VehicleStatus> vehicleStatusStream = movementsStream.join(
            vehiclesTable,
            (vehicleId, vehicleMoved) -> vehicleId,
            (vehicleMoved, vehicle) -> new VehicleStatus(
                    vehicle,
                    vehicleMoved.latitude,
                    vehicleMoved.longitude,
                    vehicleMoved.elevation
            ));

        // TODO: materialize vehicleStatusStream to the "vehicle-status" topic
        vehicleStatusStream.to(
            "vehicle-status",
            Produced.with(intSerde, vehicleStatusSerde)
        );

        vehicleStatusStream.groupByKey().aggregate(
            VehicleMetrics::new,
            (vehicleId, vehicleStatus, vehicleMetrics) -> vehicleMetrics.update(vehicleStatus),
            Materialized.<Integer, VehicleMetrics, KeyValueStore<Bytes, byte[]>>as("vehicle-metrics-store")
                .withKeySerde(intSerde)
                .withValueSerde(vehicleMetricsSerde)
            );

        return builder.build();
    }
}
