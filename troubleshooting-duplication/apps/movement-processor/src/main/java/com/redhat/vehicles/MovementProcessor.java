package com.redhat.vehicles;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MovementProcessor {

    private static String MOVEMENT_TOPIC = "duplications";
    private static String SPEED_TOPIC = "speeds";
    private static final Logger logger = LoggerFactory.getLogger(MovementProcessor.class);
    private static Producer<Void, Float> producer;


    public static void main(String[] args) throws InterruptedException, RuntimeException {
        Consumer<Void, MovementReported> consumer = new KafkaConsumer<>(
            getConsumerConfig(),
            new VoidDeserializer(),
            new EventDeserializer<MovementReported>(MovementReported.class));
        producer = new KafkaProducer<>(getProducerConfig());

        consumer.subscribe(Collections.singletonList(MOVEMENT_TOPIC));

        while (true) {
            // Kafka auto-commits the last offset of the previous poll batch
            // It works well if the processing body is sync
            // If async, kafka might auto-commit, even if the processing failed
            ConsumerRecords<Void, MovementReported> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));

            // Simulate some network delay
            Thread.sleep(2000);

            for (ConsumerRecord<Void, MovementReported> record : records) {
                MovementReported event = record.value();

                calculateSpeed(event)
                    .whenComplete((speed, t) -> {
                        produceResult(speed);
                    })
                    .exceptionally(error -> {
                        error.printStackTrace();
                        System.exit(-1);
                        return null;
                    });
            }
        }

        // producer.close();
    }

    private static void produceResult(float speed) {
        ProducerRecord<Void, Float> result = new ProducerRecord<>(SPEED_TOPIC, speed);

        Callback callback = new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null) {
                    logger.error(e.getMessage());
                } else {
                    System.out.println("Speed "  + speed + " sent to topic");
                }
            }
        };

        producer.send(result, callback);
    }

    private static CompletableFuture<Float> calculateSpeed(MovementReported event) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("\n\nProcessing " + event);

            float speed = event.distance / event.time;

            return speed;
        });
    }

    public static Properties getConsumerConfig() {

        ClassroomConfig classroomConfig = getClassroomConfig();
        Properties props = new Properties();

        props.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
            classroomConfig.getBoostrapServer() + ":" + classroomConfig.getBootstrapPort()
        );
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "movements14-consumer-group");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(
            SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
            classroomConfig.getWorkspacePath() + "/truststore.jks"
        );
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);

        return props;
    }

    public static Properties getProducerConfig() {

        ClassroomConfig classroomConfig = getClassroomConfig();
        Properties props = new Properties();

        props.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            classroomConfig.getBoostrapServer() + ":" + classroomConfig.getBootstrapPort()
        );
        props.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringSerializer"
        );
        props.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.FloatSerializer"
        );
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(
            SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
            classroomConfig.getWorkspacePath() + "/truststore.jks"
        );
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        return props;
    }

    private static ClassroomConfig getClassroomConfig() {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(
                    new File(System.getProperty("user.home") + "/.grading/ad482-workspace.json"),
                    ClassroomConfig.class);
        } catch (IOException e) {
            logger.error("Make sure to run 'lab start eda-setup' in your workspace directory", e);
            return null;
        }
    }

}