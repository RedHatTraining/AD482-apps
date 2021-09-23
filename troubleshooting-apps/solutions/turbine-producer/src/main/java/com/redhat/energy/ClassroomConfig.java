package com.redhat.energy;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassroomConfig {

    @JsonProperty("kafka_bootstrap_server")
    private String boostrapServer;

    @JsonProperty("kafka_bootstrap_port")
    private String bootstrapPort;

    @JsonProperty("workdir")
    private String workspacePath;

    private static final Logger logger = LoggerFactory.getLogger(ClassroomConfig.class);

    public static ClassroomConfig loadFromFile() {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        try {
            return objectMapper.readValue(new File(System.getProperty("user.home") + "/.grading/ad482-workspace.json"),
                    ClassroomConfig.class);
        } catch (IOException e) {
            logger.error("Make sure to run 'lab start eda-setup' in your workspace directory", e);
            return null;
        }
    }

    public String getBoostrapServer() {
        return boostrapServer;
    }

    public void setBoostrapServer(String boostrapServer) {
        this.boostrapServer = boostrapServer;
    }

    public String getBootstrapPort() {
        return bootstrapPort;
    }

    public void setBootstrapPort(String bootstrapPort) {
        this.bootstrapPort = bootstrapPort;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
    }
}