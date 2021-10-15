package com.redhat.garden.simulator;

import com.redhat.garden.simulator.event.MeasurementWasTaken;
import org.jboss.logging.Logger;
import java.util.Random;

public abstract class MeasurementSimulator {
    protected static final Logger LOGGER = Logger.getLogger(MeasurementSimulator.class);
    protected static final String MEASUREMENT_TYPE_HUMIDITY = "humidity";
    protected static final String MEASUREMENT_TYPE_TEMPERATURE = "temperature";
    protected static final String MEASUREMENT_TYPE_SUNLIGHT = "sunlight";
    protected static final String MEASUREMENT_TYPE_NUTRIENTS = "nutrients";


    protected int getRandomDeviceId() {
        return new Random().nextInt(10);
    }

    protected int getRandomGardenId() {
        return new Random().nextInt(10);
    }

    protected void logMeasurement(MeasurementWasTaken event) {
        LOGGER.infov(
                "Created measurement: Device {0} - Garden ID {1} - Type: {2} - Value: {3}",
                event.deviceID,
                event.gardenID,
                event.measurementType,
                event.measurementValue
        );
    }
}
