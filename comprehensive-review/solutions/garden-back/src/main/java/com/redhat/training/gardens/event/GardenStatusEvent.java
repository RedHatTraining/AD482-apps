package com.redhat.training.gardens.event;

public class GardenStatusEvent {
    public String gardenName;
    public Double temperature = 0.0;
    public GardenMeasurementTrend temperatureTrend;
    public Double humidity = 0.0;
    public GardenMeasurementTrend humidityTrend;
    public Double wind = 0.0;
    public GardenMeasurementTrend windTrend;

    public GardenStatusEvent() {
    }

    public GardenStatusEvent(String gardenName, Double temperature, GardenMeasurementTrend temperatureTrend, Double humidity,
            GardenMeasurementTrend humidityTrend,  Double wind, GardenMeasurementTrend windTrend) {
        this.gardenName = gardenName;
        this.temperature = temperature;
        this.temperatureTrend = temperatureTrend;
        this.humidity = humidity;
        this.humidityTrend = humidityTrend;
        this.wind = wind;
        this.windTrend = windTrend;
    }
}
