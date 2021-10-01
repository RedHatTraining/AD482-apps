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
    public static void main(String[] args) {
        // TODO: Create Kafka consumer
    }

    private static Properties configureProperties() {
        Properties props = new Properties();

        // TODO: Add Kafka configuration properties

        return props;
    }
}
