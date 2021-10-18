package com.redhat.training.gardens;

import javax.enterprise.inject.Produces;

import com.redhat.training.gardens.model.Sensor;
import com.redhat.training.gardens.model.SensorMeasurement;
import com.redhat.training.gardens.model.SensorMeasurementEnriched;

import javax.enterprise.context.ApplicationScoped;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Produced;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class MeasurementsEnricher {

    public static final String SENSORS_TOPIC = "garden-sensors";
    public static final String SENSOR_MEASUREMENTS_TOPIC = "garden-sensor-measurements";
    public static final String ENRICHED_SENSOR_MEASUREMENTS_TOPIC = "garden-enriched-sensor-measurements";

    private final ObjectMapperSerde<Sensor> sensorSerde = new ObjectMapperSerde<>(Sensor.class);
    private final ObjectMapperSerde<SensorMeasurement> sensorMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurement.class);
    private final ObjectMapperSerde<SensorMeasurementEnriched> sensorMeasurementEnrichedSerde = new ObjectMapperSerde<>(SensorMeasurementEnriched.class);

    @Produces
    public Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        GlobalKTable<Integer, Sensor> sensors = builder.globalTable(
            SENSORS_TOPIC,
            Consumed.with(Serdes.Integer(), sensorSerde));

        builder
            .stream(SENSOR_MEASUREMENTS_TOPIC, Consumed.with(Serdes.Integer(), sensorMeasurementSerde))
            .join(
                sensors,
                (sensorId, measurement) -> sensorId,
                (measurement, sensor) -> new SensorMeasurementEnriched(measurement, sensor)
            ).to(
                ENRICHED_SENSOR_MEASUREMENTS_TOPIC,
                Produced.with(Serdes.Integer(), sensorMeasurementEnrichedSerde));

        return builder.build();
    }
}
