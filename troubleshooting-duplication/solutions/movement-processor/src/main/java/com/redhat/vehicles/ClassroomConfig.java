package com.redhat.vehicles;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClassroomConfig {

    @JsonProperty("kafka_bootstrap_server")
    private String boostrapServer;

    @JsonProperty("kafka_bootstrap_port")
    private String bootstrapPort;

    @JsonProperty("workdir")
    private String workspacePath;

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