package com.redhat.vehicles;

import static java.time.Duration.ofMillis;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.util.concurrent.CountDownLatch;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;

import org.apache.kafka.streams.KafkaStreams;

@QuarkusTest
public class E2eTest {

    @Inject
    KafkaStreams streams;

    @Test
    public void testHelloEndpoint() {

        CountDownLatch latch = new CountDownLatch(1);

        streams.setStateListener((newState, oldState) -> {
            System.out.println("\nKafka state changed from " + oldState + " to " + newState + "\n");

            if (newState == KafkaStreams.State.RUNNING) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        given()
            .when().get("/vehicle/metrics")
            .then()
            .statusCode(200)
            .body(is("[]"));

    }

}
