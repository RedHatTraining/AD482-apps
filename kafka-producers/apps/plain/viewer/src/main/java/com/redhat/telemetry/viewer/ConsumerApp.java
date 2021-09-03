package com.redhat.telemetry.viewer;

import com.redhat.telemetry.producer.ProducerApp;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerApp
{
    public static Properties configureProperties() {
        Properties producerProperties = ProducerApp.configureProperties();
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperties.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "total-connected-devices-consumer-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");

        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, producerProperties.getProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG));
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, producerProperties.getProperty(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG));
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, producerProperties.getProperty(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG));

        return props;
    }

    public static void main(String[] args) {

        System.out.println(configureProperties());

        Consumer<Void,Integer> consumer = new KafkaConsumer<>(configureProperties());

        consumer.subscribe(Collections.singletonList("total-connected-devices"));

        while (true) {
            System.out.println("Waiting for events...");

            ConsumerRecords<Void, Integer> records = consumer.poll(Duration.ofMillis(10000));

            for (ConsumerRecord<Void, Integer> record : records) {
                System.out.println("Received total-connected-devices: " + record.value());
            }
        }
    }
}
