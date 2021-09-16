package com.redhat.energy.profit.stream.common;

import org.apache.kafka.streams.StreamsConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Properties;

public abstract class StreamProcessor {

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

        props.put("security.protocol", securityProtocol);
        props.put("ssl.truststore.location", truststoreLocation);
        props.put("ssl.truststore.password", truststorePassword);

        props.put(
                StreamsConfig.APPLICATION_ID_CONFIG,
                this.getClass().getSimpleName()
        );

        props.put(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        props.put("commit.interval.ms", 1000);

        return props;
    }
}
