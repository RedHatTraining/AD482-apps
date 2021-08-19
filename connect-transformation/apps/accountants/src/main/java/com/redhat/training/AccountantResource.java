package com.redhat.training;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.redhat.training.util.NameUtil;
import com.redhat.training.util.SSNUtil;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.panache.common.Sort;

@Path("accountants")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class AccountantResource {

    private static final Logger LOGGER = Logger.getLogger(AccountantResource.class.getName());

    @GET
    public List<Accountant> get() {
        return Accountant.listAll(Sort.by("id"));
    }

    @POST
    @Transactional
    public Response create(Accountant accountant) {
        if (accountant.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        accountant.setUsername(NameUtil.getRandomName());
        accountant.setSsn(SSNUtil.getRandomSSN());

        accountant.persist();
        return Response.ok(accountant).status(201).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
