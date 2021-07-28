package com.redhat.customers.producer;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;

public abstract class LateProducer {

    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrapServers;

    @ConfigProperty(name = "kafka.security.protocol")
    String securityProtocol;

    @ConfigProperty(name = "kafka.ssl.truststore.location")
    String truststoreLocation;

    @ConfigProperty(name = "kafka.ssl.truststore.password")
    String truststorePassword;

    protected Properties generateConfig() {
        Properties props = new Properties();

        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer"
        );
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "io.quarkus.kafka.client.serialization.ObjectMapperSerializer"
        );

        props.put(
                CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
                securityProtocol
        );

        props.put(
                SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                truststoreLocation
        );

        props.put(
                SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,
                truststorePassword
        );

        return props;
    }
}
