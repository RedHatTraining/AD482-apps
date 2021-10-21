package com.redhat.training.gardens;

import java.time.Duration;

import javax.enterprise.inject.Produces;
import javax.enterprise.context.ApplicationScoped;

import com.redhat.training.gardens.model.Sensor;
import com.redhat.training.gardens.model.GardenStatus;
import com.redhat.training.gardens.model.SensorMeasurement;
import com.redhat.training.gardens.model.SensorMeasurementType;
import com.redhat.training.gardens.model.SensorMeasurementEnriched;
import com.redhat.training.gardens.event.LowHumidityDetected;
import com.redhat.training.gardens.event.LowTemperatureDetected;
import com.redhat.training.gardens.event.StrongWindDetected;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
import org.apache.kafka.streams.state.KeyValueStore;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class GardenStreamsTopology {
    public static final String SENSORS_TOPIC = "garden-sensors";
    public static final String GARDEN_STATUS_EVENTS_TOPIC = "garden-status-events";
    public static final String SENSOR_MEASUREMENTS_TOPIC = "garden-sensor-measurements-repl";
    public static final String ENRICHED_SENSOR_MEASUREMENTS_TOPIC = "garden-sensor-measurements-enriched";
    public static final String LOW_TEMPERATURE_EVENTS_TOPIC = "garden-low-temperature-events";
    public static final String LOW_HUMIDITY_EVENTS_TOPIC = "garden-low-humidity-events";
    public static final String STRONG_WIND_EVENTS_TOPIC = "garden-strong-wind-events";

    private static final double LOW_TEMPERATURE_THRESHOLD_CELSIUS = 5.0;
    private static final double LOW_HUMIDITY_THRESHOLD_PERCENT = 0.2;
    private static final double STRONG_WIND_THRESHOLD_MS = 10;

    private final ObjectMapperSerde<SensorMeasurementEnriched> sensorMeasurementEnrichedSerde = new ObjectMapperSerde<>(SensorMeasurementEnriched.class);
    private final ObjectMapperSerde<LowTemperatureDetected> lowTemperatureEventSerde = new ObjectMapperSerde<>(LowTemperatureDetected.class);
    private final ObjectMapperSerde<LowHumidityDetected> lowHumidityEventSerde = new ObjectMapperSerde<>(LowHumidityDetected.class);
    private final ObjectMapperSerde<StrongWindDetected> strongWindEventSerde = new ObjectMapperSerde<>(StrongWindDetected.class);
    private final ObjectMapperSerde<GardenStatus> gardenStatusSerde = new ObjectMapperSerde<>(GardenStatus.class);

    private final ObjectMapperSerde<Sensor> sensorSerde = new ObjectMapperSerde<>(Sensor.class);
    private final ObjectMapperSerde<SensorMeasurement> sensorMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurement.class);

    @Produces
    public Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        // TODO: Read sensors

        // TODO: Read sensor measurements

        // TODO: Join measurements with sensor table

        // TODO: Send enriched measurements to topic

        // TODO: split stream

        // TODO: Aggregate enriched measurements

        return builder.build();
    }

    // TODO: implement temperature processor

    // TODO: implement humidity processor

    // TODO: implement wind processor

}
