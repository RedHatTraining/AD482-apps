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
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "YOUR_KAFKA_BOOTSTRAP_HOST:YOUR_KAFKA_BOOTSTRAP_PORT"
        );

        // TODO: set the consumer group ID
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "reportingSystem"
        );

        // TODO: set the key deserializer
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer"
        );

        // TODO: set the value deserializer
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerDeserializer"
        );

        // TODO: set the offset reset config
        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );
    }

    public static void main( String[] args ) throws IOException {
        // TODO: implement the business logic
        Consumer<Void,Integer> consumer = new KafkaConsumer<>(configureProperties());
        consumer.subscribe(Collections.singletonList("wind-turbine-production"));

        while (true) {
            ConsumerRecords<Void, Integer> records = consumer.poll(Duration.ofSeconds(10));

            int aggregatedEnergy = 0;
            int processedRecords = 0;

            for (ConsumerRecord<Void, Integer> record : records) {
                printRecord(record);

                aggregatedEnergy += record.value();
                processedRecords++;

                if (processedRecords % 5 == 0) {

                    printAggregation(aggregatedEnergy);
                    saveAggregationToFile(aggregatedEnergy);

                    aggregatedEnergy = 0;
                }
            }
        }
    }
}
