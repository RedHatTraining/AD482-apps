package com.redhat.training.gardens;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.redhat.training.gardens.event.SensorMeasurementTaken;
import com.redhat.training.gardens.model.SensorMeasurement;

@ApplicationScoped
public class SensorMeasurementsConsumer {

    @Incoming("garden-sensor-measurements")
    @Outgoing("garden-received-sensor-measurements")
    public SensorMeasurement processMessage(SensorMeasurementTaken measurement) {

        measurement.getSensorId();

        return new SensorMeasurement();
        // return SensorMeasurement(
        //     measurement.getSensorId(),
        //     measurement.getType(),
        //     measurement.getValue(),
        //     measurement.getTimestamp()
        // );
    }

}
