package com.redhat.garden;

import com.redhat.garden.sensors.SensorMeasurementEnriched;
// import com.redhat.garden.sensors.SensorMeasurementType;

import java.util.List;

public class GardenStatus {
    public String gardenName;
    public Double temperature;
    // public GardenMeasurementTrend temperatureTrend;
    // public Double humidity;
    // public GardenMeasurementTrend humidityTrend;
    // public Double wind;
    // public GardenMeasurementTrend windTrend;
    // public Double windSum;
    // public List<String> sensorNames;

    public GardenStatus() {}

    public GardenStatus(String gardenName, Double temperature) {
        this.gardenName = gardenName;
        this.temperature = temperature;
    }

    public GardenStatus updateWith(SensorMeasurementEnriched measurement) {
        gardenName = measurement.gardenName;

        // if (measurement.type == SensorMeasurementType.TEMPERATURE) {
        //     updateTemperature(measurement);
        // }

        // if (measurement.type == SensorMeasurementType.HUMIDITY) {
        //     updateHumidity(measurement);
        // }

        // if (measurement.type == SensorMeasurementType.WIND) {
        //     updateWind(measurement);
        // }

        return this;
    }

    // private void updateTemperature(SensorMeasurementEnriched measurement) {
    //     if (temperature < measurement.value) {
    //         temperatureTrend = GardenMeasurementTrend.UP;
    //     } else if (temperature == measurement.value) {
    //         temperatureTrend = GardenMeasurementTrend.STABLE;
    //     } else {
    //         temperatureTrend = GardenMeasurementTrend.DOWN;
    //     }
    // }

    // private void updateHumidity(SensorMeasurementEnriched measurement) {
    //     if (humidity < measurement.value) {
    //         humidityTrend = GardenMeasurementTrend.UP;
    //     } else if (humidity == measurement.value) {
    //         humidityTrend = GardenMeasurementTrend.STABLE;
    //     } else {
    //         humidityTrend = GardenMeasurementTrend.DOWN;
    //     }
    // }

    // private void updateWind(SensorMeasurementEnriched measurement) {
    //     if (wind < measurement.value) {
    //         windTrend = GardenMeasurementTrend.UP;
    //     } else if (wind == measurement.value) {
    //         windTrend = GardenMeasurementTrend.STABLE;
    //     } else {
    //         windTrend = GardenMeasurementTrend.DOWN;
    //     }
    // }

}
