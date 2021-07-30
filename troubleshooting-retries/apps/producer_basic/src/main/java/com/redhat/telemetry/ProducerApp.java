package com.redhat.telemetry;

import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;

public class ProducerApp {
    public static Properties configureProperties() {
        Properties props = new Properties();

        // @todo: configure the bootstrap server
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "my-cluster-kafka-bootstrap-jramirez-kafka-cluster.apps.na46-stage2.dev.nextcle.com:443"
        );

        // @todo: configure the key and value serializers
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer"
        );
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerSerializer"
        );

        // @todo: configure the SSL connection
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(
                SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                "/Users/jairamir/AD482-workspace/truststore.jks"
        );
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 0);

        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 30000);
        // props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1000);
        // // max.in.flight.requests.per.connection
        // props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        // // props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 110);
        // props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 0);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return props;
    }

    public static void main(String[] args) throws InterruptedException {
        // @todo: Implement the Kafka producer
        Random random = new Random();
        Producer<Void,Integer> producer = new KafkaProducer<>(
                configureProperties()
        );

        for (int i = 0; i < 10; i++) {
            ProducerRecord<Void, Integer> record = new ProducerRecord<>(
                    "retries-a",
                    (int)(System.currentTimeMillis() / 1000)
            );

            producer.send(record);
            printRecord(record);

            Thread.sleep(2000);
        }

        producer.close();
    }

    private static void printRecord(ProducerRecord record) {
        System.out.println("Sent record:");
        System.out.println("\tTopic = " + record.topic());
        System.out.println("\tPartition = " + record.partition());
        System.out.println("\tKey = " + record.key());
        System.out.println("\tValue = " + record.value());
    }
}