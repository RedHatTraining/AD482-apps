package com.redhat.telemetry;

import java.time.Duration;
import java.util.Properties;
import java.util.Collections;

import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;


public class ConsumerApp
{
    public static void main( String[] args )
    {
        Consumer<Void,Integer> consumer = new KafkaConsumer<>(configureProperties());
        consumer.subscribe(Collections.singletonList("humidity-conditions"));

        while (true) {
            ConsumerRecords<Void, Integer> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));

            for (ConsumerRecord<Void, Integer> record : records) {
                System.out.println("Received humidity value: " + record.value());
            }
        }
    }

    private static Properties configureProperties() {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "your-kafka-cluster-name.apps.cluster.nextcle.com:443");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "humidityMonitoring");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/PATH/TO/keystore.jks");
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "password");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/PATH/TO/truststore.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        return props;
    }
}
