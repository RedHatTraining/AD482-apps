package com.redhat.telemetry.producer;

import java.time.Duration;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ProducerApp {

    private static final Logger LOG = Logger.getLogger(ProducerApp.class);

    private final Random random = new Random();

    @Outgoing("retries1")
    public Multi<Record<String, Integer>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(1))
                .onOverflow().drop()
                .map(tick -> {
                    String currentDevice = "device-1"; // + random.nextInt(10);
                    int currentMeasure = tick.intValue() + 20000; //random.nextInt(100);

                    LOG.infov("Device ID: {0}, measure: {1}",
                            currentDevice,
                            currentMeasure
                    );

                    return Record.of(currentDevice, currentMeasure);
                });
    }
}