package com.redhat.crd;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.crd.config.model.ClassroomConfig;
import com.redhat.crd.model.CallDetailRecord;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class CRDProducer {

    public static Properties getKafkaProperties() {

        ClassroomConfig classroomConfig = getClassroomConfig();

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, classroomConfig.getBoostrapServer() + ":"
                + classroomConfig.getBootstrapPort());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, classroomConfig.getWorkspacePath() + "/truststore.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        return props;
    }

    public static void main(String[] args) {
        Producer<Integer, String> producer = new KafkaProducer<>(getKafkaProperties());

        for (int i = 1; i <= 7; i++) {

            CallDetailRecord callDetailRecord = new CallDetailRecord(i, "Call record-" + i);

            ProducerRecord<Integer, String> record = new ProducerRecord<>(
                    "call-detail-records", callDetailRecord.getUserId(), callDetailRecord.toString()
            );

            producer.send(record);
            printRecord(record);
        }

        producer.flush();
        producer.close();
    }

    private static void printRecord(ProducerRecord record) {
        System.out.println("Sent record:");
        System.out.println("\tTopic = " + record.topic());
        System.out.println("\tKey = " + record.key());
        System.out.println("\tValue = " + record.value());
    }

    private static ClassroomConfig getClassroomConfig() {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(
                    new File(System.getProperty("user.home") + "/.grading/ad482-workspace.json"),
                    ClassroomConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Make sure to run 'lab start eda-setup' in your workspace directory ", e);
        }
    }

}
