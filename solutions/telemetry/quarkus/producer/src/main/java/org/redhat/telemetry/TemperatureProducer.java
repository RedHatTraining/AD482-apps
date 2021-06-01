package org.redhat.telemetry;

import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;

import io.smallrye.reactive.messaging.kafka.KafkaRecord;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;


@ApplicationScoped
public class TemperatureProducer {

    @Inject
    @Channel("temperatures")
    Emitter<Integer> temperatures;

    public void produce(Temperature temperature) {
        KafkaRecord<Void, Integer> record = KafkaRecord.of(null, temperature.value);
        temperatures.send(record);
    }

}
