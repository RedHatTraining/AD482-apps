package com.redhat.energy;

import java.util.Properties;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;

public class TurbinesDataProducerApp {

    public static void main(String[] args) throws InterruptedException {
        // The emitter encapsulates a Kafka producer. Kakfa producer config is passed as a parameter
        Properties config = setProducerConfig();
        PowerMeasurementsEmitter emitter = new PowerMeasurementsEmitter("turbine-generated-watts", config);
        // TODO: Activate the turbine timestamps feature

        // Start turbines
        Turbine turbine1 = new Turbine(1, 2000000);
        Turbine turbine2 = new Turbine(2, 2500000);
        Turbine turbine3 = new Turbine(3, 3000000);

        turbine1.start().subscribe().with(data -> emitter.emit(data));
        turbine2.start().subscribe().with(data -> emitter.emit(data));
        turbine3.start().subscribe().with(data -> emitter.emit(data));
    }

    public static Properties setProducerConfig() {
        ClassroomConfig classroomConfig = ClassroomConfig.loadFromFile();
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                classroomConfig.getBoostrapServer() + ":" + classroomConfig.getBootstrapPort());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, classroomConfig.getWorkspacePath() + "/truststore.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);
        // TODO: fix delivery issues
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 10);

        return props;
    }

}