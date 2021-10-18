package com.redhat.training.gardens;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.redhat.training.gardens.event.SensorMeasurementTaken;
import com.redhat.training.gardens.event.SensorMeasurementType;
import com.redhat.training.gardens.model.MeasureType;
import com.redhat.training.gardens.model.SensorMeasurement;

@ApplicationScoped
public class SensorMeasurementsConsumer {

    @Incoming("garden-sensor-measurements")
    @Outgoing("garden-received-sensor-measurements")
    public SensorMeasurement processMessage(SensorMeasurementTaken event) {
        return new SensorMeasurement(
            event.getSensorId(),
            getMeasureType(event),
            event.getValue(),
            event.getTimestamp()
        );
    }

    private MeasureType getMeasureType(SensorMeasurementTaken event) {
        MeasureType type = MeasureType.TEMPERATURE;

        if (event.getType() == SensorMeasurementType.TEMPERATURE) {
                type = MeasureType.TEMPERATURE;
        }

        if (event.getType() == SensorMeasurementType.HUMIDITY) {
                type = MeasureType.HUMIDITY;
        }

        if (event.getType() == SensorMeasurementType.WIND) {
                type = MeasureType.WIND;
        }

        return type;
    }

}
