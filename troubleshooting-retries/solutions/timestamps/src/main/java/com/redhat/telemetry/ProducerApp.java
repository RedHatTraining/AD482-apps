package com.redhat.telemetry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SslConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerApp {

    private static final Logger logger = LoggerFactory.getLogger(ProducerApp.class);

    public static Properties configureProperties() {

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
            "org.apache.kafka.common.serialization.LongSerializer"
        );
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(
            SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
            classroomConfig.getWorkspacePath() + "/truststore.jks"
        );
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // TODO: configure timeouts
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 150);

        // TODO: configure idempotence
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);

        // TODO: configure retries
        props.put(ProducerConfig.RETRIES_CONFIG, 0);

        return props;
    }

    public static void main(String[] args) throws InterruptedException {
        Producer<Void,Long> producer = new KafkaProducer<>(configureProperties());
        List<Long> sentValues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ProducerRecord<Void, Long> record = new ProducerRecord<>(
                "timestamps",
                System.currentTimeMillis()
            );

            Callback callback = new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e != null) {
                        System.out.println(e);
                    } else {
                        Long value = record.value();
                        System.out.println("Message sent: "  + value);
                        sentValues.add(value);
                    }
                }
            };

            producer.send(record, callback);

            Thread.sleep(2000);
        }

        System.out.println("Successfully sent messages: " + sentValues.size());

        producer.close();
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