package com.redhat.training.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.training.model.Plumber;
import com.redhat.training.model.PlumberStatus;
import io.quarkus.panache.common.Sort;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import org.apache.kafka.common.header.Header;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class PlumberService {

    public List<Plumber> get() {
        return Plumber.listAll(Sort.by("id"));
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Incoming("requests")
    @Blocking
    @Transactional
    public CompletionStage<Void> onRequestEvent(Message<String> message) {

        String eventStr = message.getPayload();
        JsonNode event = deserialize(eventStr);

        String eventType = getHeaderAsString(message, "eventType");

        if (eventType.equals("RequestCreated")) {
            updatePlumber(event);
        }

        return message.ack();
    }

    private void updatePlumber(JsonNode event) {
        Long plumberId = event.get("plumberId").asLong();
        Plumber plumber = Plumber.findById(plumberId);
        plumber.setStatus(PlumberStatus.ASSIGNED);
        plumber.persistAndFlush();
    }


    private JsonNode deserialize(String event) {
        JsonNode eventPayload;

        try {
            eventPayload = objectMapper.readTree(event);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't deserialize event", e);
        }

        return eventPayload.has("schema") ? eventPayload.get("payload") : eventPayload;
    }

    private String getHeaderAsString(Message<?> message, String name) {
        var metadata = message.getMetadata(IncomingKafkaRecordMetadata.class).orElseThrow();
        Header header = metadata.getHeaders().lastHeader(name);
        if (header == null) {
            throw new IllegalArgumentException("Expected record header '" + name + "' not present");
        }

        return new String(header.value(), Charset.forName("UTF-8"));
    }

}
