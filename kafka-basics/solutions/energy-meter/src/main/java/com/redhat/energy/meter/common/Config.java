package com.redhat.energy.meter.common;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;

import java.util.Properties;

public class Config {
    protected static void configureConnectionSecurity(Properties props) {
        // TODO: configure the connection protocol
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");

        // TODO: configure the path to the truststore file
        props.put(
                SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                "ABSOLUTE_PATH_TO_YOUR_WORKSPACE_FOLDER/truststore.jks"
        );

        // TODO: configure the truststore password
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "password");
    }
}
