package com.redhat.training.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.training.model.RepairRequest;
import io.debezium.outbox.quarkus.ExportedEvent;

import java.time.Instant;

public class RequestCreatedEvent implements ExportedEvent<String, JsonNode> {

    private static ObjectMapper mapper = new ObjectMapper();

    private final long id;
    private final JsonNode repairRequest;
    private final Instant timestamp;

    private RequestCreatedEvent(long id, JsonNode repairRequest) {
        this.id = id;
        this.repairRequest = repairRequest;
        this.timestamp = Instant.now();
    }


    public static RequestCreatedEvent of(RepairRequest repairRequest) {
        ObjectNode asJson = mapper.createObjectNode()
                .put("id", repairRequest.getId())
                .put("requesterName", repairRequest.getRequesterName())
                .put("requestDate", repairRequest.getRequestDate().toString())
                .put("status", repairRequest.getStatus().toString())
                .put("plumberId", repairRequest.getPlumberId());
        return new RequestCreatedEvent(repairRequest.getId(), asJson);
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(id);
    }

    @Override
    public String getAggregateType() {
        return "repair-request";
    }

    @Override
    public String getType() {
        return "RequestCreated";
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public JsonNode getPayload() {
        return repairRequest;
    }
}
