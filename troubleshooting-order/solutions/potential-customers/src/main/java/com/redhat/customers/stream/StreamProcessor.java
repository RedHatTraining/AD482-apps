package com.redhat.customers.stream;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.streams.StreamsConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;
import java.util.Random;

public abstract class StreamProcessor {
    private final Random random = new Random();

    @ConfigProperty(name = "quarkus.kafka-streams.bootstrap-servers")
    String bootstrapServers;

    @ConfigProperty(name = "kafka.security.protocol")
    String securityProtocol;

    @ConfigProperty(name = "kafka.ssl.truststore.location")
    String truststoreLocation;

    @ConfigProperty(name = "kafka.ssl.truststore.password")
    String truststorePassword;

    protected Properties generateStreamConfig() {
        Properties props = new Properties();

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

        props.put(
                StreamsConfig.APPLICATION_ID_CONFIG,
                this.getClass().getSimpleName() + random.nextInt()
        );

        props.put(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        return props;
    }
}
