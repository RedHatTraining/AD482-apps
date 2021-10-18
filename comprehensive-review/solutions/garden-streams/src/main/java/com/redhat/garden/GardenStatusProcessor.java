package com.redhat.garden;

import javax.enterprise.inject.Produces;

import java.time.Duration;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.garden.entities.GardenStatus;
import com.redhat.garden.entities.SensorMeasurementEnriched;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;


@ApplicationScoped
public class GardenStatusProcessor {
    public static final String ENRICHED_SENSOR_MEASUREMENTS_TOPIC = "garden-enriched-sensor-measurements";
    public static final String GARDEN_STATUS_EVENTS_TOPIC = "garden-status-events";

    private final ObjectMapperSerde<SensorMeasurementEnriched> sensorMeasurementEnrichedSerde = new ObjectMapperSerde<>(SensorMeasurementEnriched.class);
    private final ObjectMapperSerde<GardenStatus> gardenStatusSerde = new ObjectMapperSerde<>(GardenStatus.class);

    @Produces
    public Topology getTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        builder
            .stream(
                ENRICHED_SENSOR_MEASUREMENTS_TOPIC,
                Consumed.with(Serdes.Integer(), sensorMeasurementEnrichedSerde))
            .groupBy(
                (sensorId, measurement) -> measurement.gardenName,
                Grouped.with(Serdes.String(), sensorMeasurementEnrichedSerde)
            )
            .windowedBy(
                TimeWindows.of(Duration.ofMinutes(1)).advanceBy(Duration.ofMinutes(1))
            )
            .aggregate(
                GardenStatus::new,
                (gardenName, measurement, gardenStatus) -> gardenStatus.updateWith(measurement),
                Materialized
                    .<String, GardenStatus, WindowStore<Bytes, byte[]>>as("garden-status-store")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(gardenStatusSerde))
            .toStream()
            .map((windowedGardenName, gardenStatus) ->
                // Make key null
                new KeyValue<>(windowedGardenName.key(), gardenStatus))
            .to(
                GARDEN_STATUS_EVENTS_TOPIC,
                Produced.with(Serdes.String(), gardenStatusSerde));

        return builder.build();
    }
}
