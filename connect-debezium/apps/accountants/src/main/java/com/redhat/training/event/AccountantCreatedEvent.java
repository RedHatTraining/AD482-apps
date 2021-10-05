package com.redhat.training.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.training.model.Accountant;
import io.debezium.outbox.quarkus.ExportedEvent;

import java.time.Instant;

public class AccountantCreatedEvent implements ExportedEvent<String, JsonNode> {

    private static ObjectMapper mapper = new ObjectMapper();

    private final long id;
    private final JsonNode accountant;
    private final Instant timestamp;

    private AccountantCreatedEvent(long id, JsonNode accountant) {
        this.id = id;
        this.accountant = accountant;
        this.timestamp = Instant.now();
    }

    public static AccountantCreatedEvent of(Accountant accountant) {
        ObjectNode asJson = mapper.createObjectNode()
                .put("id", accountant.getId())
                .put("userName", accountant.getUsername())
                .put("ssn", accountant.getSsn())
                .put("status", accountant.getStatus().toString());
        return new AccountantCreatedEvent(accountant.getId(), asJson);
    }

    @Override
    public String getAggregateId() {
        return null;
    }

    @Override
    public String getAggregateType() {
        return "accountant-event";
    }

    @Override
    public String getType() {
        return "AccountCreated";
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public JsonNode getPayload() {
        return accountant;
    }
}
