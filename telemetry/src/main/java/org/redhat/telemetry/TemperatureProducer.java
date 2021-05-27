package org.redhat.telemetry;

import java.time.Duration;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.reactive.messaging.kafka.OutgoingKafkaRecord;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Outgoing;


@ApplicationScoped
public class TemperatureProducer {

    private Random random = new Random();

    @Outgoing("temperatures-produced")
    public  Multi<KafkaRecord<Void, Integer>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onOverflow().drop()
                .map(tick -> {
                    int temperature = random.nextInt(100);
                    System.out.println("Produce: " + temperature);
                    return KafkaRecord.of(null, temperature);
                });
    }

    // protected void produceTemperature(int temperature) {
    //     KafkaRecord<Integer, Integer> record = KafkaRecord.of(null, temperature);
    //     temperatures.send(record);
    // }
}
