package com.redhat.training;

import com.redhat.training.util.NameUtil;
import com.redhat.training.util.SSNUtil;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;
import io.cloudevents.jackson.JsonFormat;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.net.URI;

@ApplicationScoped
public class AccountantsService {
    private static final Logger LOGGER = Logger.getLogger(AccountantsService.class.getName());

    public List<Accountant> get() {
        return Accountant.listAll(Sort.by("id"));
    }

    @Transactional
    public Long create(Accountant accountant) {
        if (accountant.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        accountant.setUsername(NameUtil.getRandomName());
        accountant.setSsn(SSNUtil.getRandomSSN());
        accountant.setStatus(AccountantStatus.JOINED);
        accountant.persist();

        AccountantsOutboxEvent outboxEvent = buildOutBoxEvent(accountant);
        outboxEvent.persist();
        outboxEvent.delete();

        return accountant.id;
    }


    @Transactional
    public Accountant update(@PathParam Long id, Accountant accountant) {
        if (accountant.getStatus() == null) {
            throw new WebApplicationException("Accountant status was not set on request.", 422);
        }

        Accountant entity = Accountant.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Accountant with id of " + id + " does not exist.", 404);
        }

        entity.setStatus(accountant.getStatus());

        AccountantsOutboxEvent outboxEvent = buildOutBoxEvent(entity);
        outboxEvent.persist();
        outboxEvent.delete();

        return entity;
    }

    private AccountantsOutboxEvent buildOutBoxEvent(Accountant accountant) {
        AccountantsOutboxEvent outboxEvent = new AccountantsOutboxEvent();
        outboxEvent.setAggregateType("accountant-event");
        outboxEvent.setAggregateId(Long.toString(accountant.id));
        outboxEvent.setContentType("application/cloudevents+json; charset=UTF-8");
        outboxEvent.setPayload(toCloudEvent(accountant));
        return outboxEvent;
    }

    private String toCloudEvent(Accountant accountant) {
        CloudEvent event = CloudEventBuilder.v1()
                .withType("AccountCreatedEvent")
                .withTime(OffsetDateTime.now())
                .withSource(URI.create("accountancy/accountant-service"))
                .withDataContentType("application/json")
                .withId(UUID.randomUUID().toString())
                .withData(accountant.toJson().encode().getBytes()).build();
        EventFormat format = EventFormatProvider.getInstance().resolveFormat(JsonFormat.CONTENT_TYPE);
        return new String(format.serialize(event));
    }

}
