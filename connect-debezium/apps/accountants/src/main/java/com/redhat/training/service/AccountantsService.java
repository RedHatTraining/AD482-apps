package com.redhat.training.service;

import com.redhat.training.event.AccountantUpdatedEvent;
import com.redhat.training.model.Accountant;
import com.redhat.training.model.AccountantStatus;
import com.redhat.training.event.AccountantCreatedEvent;
import com.redhat.training.util.NameUtil;
import com.redhat.training.util.SSNUtil;
import io.debezium.outbox.quarkus.ExportedEvent;
import io.quarkus.panache.common.Sort;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import javax.enterprise.event.Event;

@ApplicationScoped
public class AccountantsService {
    private static final Logger LOGGER = Logger.getLogger(AccountantsService.class.getName());

    @Inject
    Event<ExportedEvent<?, ?>> event;

    public List<Accountant> get() {
        return Accountant.listAll(Sort.by("id"));
    }

    @Transactional
    public Long create(Accountant accountant) {
        if (accountant.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        accountant.setUsername(NameUtil.getRandomName());
        accountant.setSsn(SSNUtil.getRandomSSN());
        accountant.setStatus(AccountantStatus.JOINED);
        accountant.persist();

        event.fire(AccountantCreatedEvent.of(accountant));

        return accountant.getId();
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

        event.fire(AccountantUpdatedEvent.of(entity));

        return entity;
    }
}
