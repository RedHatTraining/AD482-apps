package com.redhat.telemetry;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.SslConfigs;

public class ProducerApp {
    public static Properties configureProperties() {
        Properties props = new Properties();

        // TODO: configure the Kafka bootstrap server
        props.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "..."
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

        // TODO: configure the truststore path
        props.put(
            SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
            "..."
        );

        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // TODO: configure timeouts

        // TODO: configure idempotence

        // TODO: configure retries

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

}