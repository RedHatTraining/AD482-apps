package com.redhat.energy;

import java.util.Properties;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;

public class TurbineProducerApp {

    public static void main(String[] args) throws InterruptedException {

        // Emitters encapsulate Kafka producers
        // Kakfa producer config is passed to emitters
        Emitter<Integer> powerEmitter = new Emitter<>("turbine-generated-watts", configureProducerProperties());
        Emitter<Integer> windEmitter = new Emitter<>("turbine-wind-speed", configureProducerProperties());

        // Start turbines
        Turbine turbine1 = new Turbine(1, 1000000);

        turbine1.start()
            .subscribe().with(data -> {
                powerEmitter.emit(data.turbineId, data.power, data.timestamp);
                windEmitter.emit(data.turbineId, data.wind, data.timestamp);
            });
    }

    public static Properties configureProducerProperties() {

        ClassroomConfig classroomConfig = ClassroomConfig.loadFromFile();
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                classroomConfig.getBoostrapServer() + ":" + classroomConfig.getBootstrapPort());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, classroomConfig.getWorkspacePath() + "/truststore.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // TODO: configure timeouts
        // props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 1);
        // props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1);
        // props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);
        // props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 150);

        // TODO: configure idempotence

        // TODO: configure retries

        return props;
    }





}