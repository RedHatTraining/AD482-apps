package com.redhat.training.gardens.model;

public class SensorMeasurementEnriched extends SensorMeasurement {
    public String sensorName;
    public String gardenName;

    public SensorMeasurementEnriched() {}

    public SensorMeasurementEnriched(
            Integer sensorId,
            String type,
            Double value,
            Long timestamp,
            String sensorName,
            String gardenName
    ) {
        super(sensorId, type, value, timestamp);

        this.sensorName = sensorName;
        this.gardenName = gardenName;
    }
}
