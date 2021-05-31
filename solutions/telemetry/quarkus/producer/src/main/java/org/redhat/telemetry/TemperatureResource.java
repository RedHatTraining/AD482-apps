package org.redhat.telemetry;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;


@Path("/temperature")
public class TemperatureResource {

    @Inject
    TemperatureProducer producer;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String postTemperature(Temperature temperature) {
        producer.produce(temperature);

        return "Produced temperature: " + temperature.getValue();
    }
}
