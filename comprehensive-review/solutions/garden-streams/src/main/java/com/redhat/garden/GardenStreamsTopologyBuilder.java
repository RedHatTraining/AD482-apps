package com.redhat.garden;

import javax.enterprise.inject.Produces;
import javax.enterprise.context.ApplicationScoped;

import com.redhat.garden.events.DryConditionsDetected;
import com.redhat.garden.events.LowNutrientsDetected;
import com.redhat.garden.events.LowTemperatureDetected;
import com.redhat.garden.sensors.Sensor;
import com.redhat.garden.sensors.SensorMeasurement;
import com.redhat.garden.sensors.SensorMeasurementEnriched;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class GardenStreamsTopologyBuilder {

    private static final double LOW_TEMPERATURE_THRESHOLD_CELSIUS = 5.0;
    private static final double LOW_HUMIDITY_THRESHOLD_PERCENT = 0.2;
    private static final double LOW_NUTRIENTS_THRESHOLD_PERCENT = 0.5;
    public static String SENSORS_TOPIC = "sensors";
    public static String SENSOR_MEASUREMENTS_TOPIC = "sensor-measurements";
    public static String LOW_TEMPERATURE_EVENTS_TOPIC = "low-temperature-events";
    public static String DRY_CONDITIONS_EVENTS_TOPIC = "dry-conditions-events";
    public static String LOW_NUTRIENTS_EVENTS_TOPIC = "low-nutrients-events";

    ObjectMapperSerde<Sensor> sensorSerde = new ObjectMapperSerde<>(Sensor.class);
    ObjectMapperSerde<SensorMeasurement> sensorMeasurementSerde = new ObjectMapperSerde<>(SensorMeasurement.class);
    ObjectMapperSerde<LowTemperatureDetected> lowTemperatureEventSerde = new ObjectMapperSerde<>(LowTemperatureDetected.class);
    ObjectMapperSerde<DryConditionsDetected> dryConditionsEventSerde = new ObjectMapperSerde<>(DryConditionsDetected.class);
    ObjectMapperSerde<LowNutrientsDetected> lowNutrientsEventSerde = new ObjectMapperSerde<>(LowNutrientsDetected.class);

    @Produces
    public Topology build() {
        StreamsBuilder builder = new StreamsBuilder();

        GlobalKTable<Integer, Sensor> sensors = builder.globalTable(
            SENSORS_TOPIC,
            Consumed.with(Serdes.Integer(), sensorSerde),
            Materialized.as("sensors-store"));

        builder
            .stream(SENSOR_MEASUREMENTS_TOPIC, Consumed.with(Serdes.Integer(), sensorMeasurementSerde))
            .join(
                sensors,
                (sensorId, measurement) -> sensorId,
                (measurement, sensor) -> new SensorMeasurementEnriched(measurement, sensor)
            )
            .split()
                .branch((sensorId, measurement) -> measurement.property.equals("temperature"), Branched.withConsumer(this::proccessTemperature))
                .branch((sensorId, measurement) -> measurement.property.equals("humidity"), Branched.withConsumer(this::processHumidity))
                .branch((sensorId, measurement) -> measurement.property.equals("nutrients"), Branched.withConsumer(this::processNutrients));

        return builder.build();
    }

    private void proccessTemperature(KStream<Integer, SensorMeasurementEnriched> temperatureMeasurements) {
        temperatureMeasurements
            .filter((sensorId, measurement) -> measurement.value < LOW_TEMPERATURE_THRESHOLD_CELSIUS)
            .mapValues((measurement) -> new LowTemperatureDetected(measurement.gardenName, measurement.sensorId, measurement.value, measurement.timestamp))
            .to(LOW_TEMPERATURE_EVENTS_TOPIC, Produced.with(Serdes.Integer(), lowTemperatureEventSerde));
    }

    private void processHumidity(KStream<Integer, SensorMeasurementEnriched> humidityMeasurements) {
        humidityMeasurements
            .filter((sensorId, measurement) -> measurement.value < LOW_HUMIDITY_THRESHOLD_PERCENT)
            .mapValues((measurement) -> new DryConditionsDetected(measurement.gardenName, measurement.sensorId, measurement.value, measurement.timestamp))
            .to(DRY_CONDITIONS_EVENTS_TOPIC, Produced.with(Serdes.Integer(), dryConditionsEventSerde));
    }

    private void processNutrients(KStream<Integer, SensorMeasurementEnriched> nutrientsMeasurements) {
        nutrientsMeasurements
            .filter((sensorId, measurement) -> measurement.value < LOW_NUTRIENTS_THRESHOLD_PERCENT)
            .mapValues((measurement) -> new LowNutrientsDetected(measurement.gardenName, measurement.sensorId, measurement.value, measurement.timestamp))
            .to(LOW_NUTRIENTS_EVENTS_TOPIC, Produced.with(Serdes.Integer(), lowNutrientsEventSerde));
    }

}
