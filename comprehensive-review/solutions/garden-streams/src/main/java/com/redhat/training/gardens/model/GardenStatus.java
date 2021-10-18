package com.redhat.training.gardens.model;

public class GardenStatus {
    public String gardenName;
    public Double temperature = 0.0;
    public GardenMeasurementTrend temperatureTrend;
    public Double humidity = 0.0;
    public GardenMeasurementTrend humidityTrend;
    public Double wind = 0.0;
    public GardenMeasurementTrend windTrend;

    public GardenStatus() {}

    public GardenStatus updateWith(SensorMeasurementEnriched measurement) {
        gardenName = measurement.gardenName;

        if (measurement.type == SensorMeasurementType.TEMPERATURE) {
            updateTemperature(measurement);
        }

        if (measurement.type == SensorMeasurementType.HUMIDITY) {
            updateHumidity(measurement);
        }

        if (measurement.type == SensorMeasurementType.WIND) {
            updateWind(measurement);
        }

        return this;
    }

    private void updateTemperature(SensorMeasurementEnriched measurement) {
        if (temperature < measurement.value) {
            temperatureTrend = GardenMeasurementTrend.UP;
        } else if (temperature == measurement.value) {
            temperatureTrend = GardenMeasurementTrend.STABLE;
        } else {
            temperatureTrend = GardenMeasurementTrend.DOWN;
        }
        temperature = measurement.value;
    }

    private void updateHumidity(SensorMeasurementEnriched measurement) {
        if (humidity < measurement.value) {
            humidityTrend = GardenMeasurementTrend.UP;
        } else if (humidity == measurement.value) {
            humidityTrend = GardenMeasurementTrend.STABLE;
        } else {
            humidityTrend = GardenMeasurementTrend.DOWN;
        }
        humidity = measurement.value;
    }

    private void updateWind(SensorMeasurementEnriched measurement) {
        if (wind < measurement.value) {
            windTrend = GardenMeasurementTrend.UP;
        } else if (wind == measurement.value) {
            windTrend = GardenMeasurementTrend.STABLE;
        } else {
            windTrend = GardenMeasurementTrend.DOWN;
        }
        wind = measurement.value;
    }

}
