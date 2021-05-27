package org.redhat.telemetry;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;

@Path("/telemetry")
public class TelemetryResource {

    @Inject
    @Channel("temperatures")
    Flowable<KafkaRecord<Void, Integer>> temperatures;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<Integer> getEvents() {
        return temperatures.map(k -> k.getPayload());

    }
}