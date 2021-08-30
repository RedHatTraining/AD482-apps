package com.redhat.energy.meter.consumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.Collections;

import com.redhat.energy.meter.common.Config;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class ReportingSystem extends Config {
    private static void printRecord(ConsumerRecord<Void, Integer> record) {
        System.out.println("Received record:");
        System.out.println("\tTopic = " + record.topic());
        System.out.println("\tPartition = " + record.partition());
        System.out.println("\tKey = " + record.key());
        System.out.println("\tValue = " + record.value());
    }

    private static void printAggregation(int aggregationResult) {
        System.out.println("Writing aggregation result to file: " + aggregationResult);
    }

    private static void saveAggregationToFile(int aggregationResult) throws IOException {
        Path reportFile = Path.of("report.txt");
        Files.writeString(reportFile, Integer.toString(aggregationResult));
    }

    private static Properties configureProperties() {
        Properties props = new Properties();

        configureConsumer(props);
        configureConsumerForLab(props);
        configureConnectionSecurity(props);

        return props;
    }

    private static void configureConsumerForLab(Properties props) {
        props.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                "false"
        );
    }

    private static void configureConsumer(Properties props) {
        // TODO: set the bootstrap server

        // TODO: set the consumer group ID

        // TODO: set the key deserializer

        // TODO: set the value deserializer

        // TODO: set the offset reset config
    }

    public static void main( String[] args ) throws IOException {
        // TODO: implement the business logic
    }
}
