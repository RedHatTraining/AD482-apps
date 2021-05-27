package com.redhat.telemetry.console;

import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;


/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        Properties props = configureProperties();

        System.out.println( "Hello Producer!" );
        Producer<Void,Integer> producer = new KafkaProducer<>(props);

        Random random = new Random();

        for (int i=0;i < 10; i++) {
            ProducerRecord<Void, Integer> record = new ProducerRecord<>("temperatures", random.nextInt(10000));
            producer.send(record);
            System.out.println("Sent " + record);
        }

        producer.close();
        System.out.println( "Producer done!" );
    }

    private static Properties configureProperties() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "my-cluster-kafka-bootstrap-jramirez-kafka.apps.na46-stage2.dev.nextcle.com:443");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "./certs/keystore.jks");
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "password");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "./certs/truststore.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");

        return props;
    }
}
