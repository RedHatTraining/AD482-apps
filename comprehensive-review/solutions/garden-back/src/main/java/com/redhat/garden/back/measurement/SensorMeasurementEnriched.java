package com.redhat.garden.back.measurement;

public class SensorMeasurementEnriched extends SensorMeasurement {
    public String sensorName;
    public String gardenName;

    public SensorMeasurementEnriched() {}

    public SensorMeasurementEnriched(
            Integer sensorId,
            String property,
            Double value,
            Integer timestamp,
            String sensorName,
            String gardenName
    ) {
        super(sensorId, property, value, timestamp);

        this.sensorName = sensorName;
        this.gardenName = gardenName;
    }
}
