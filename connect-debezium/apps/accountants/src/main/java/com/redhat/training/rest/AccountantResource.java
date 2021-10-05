package com.redhat.training.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.training.model.Accountant;
import com.redhat.training.service.AccountantsService;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Path("accountants")
@Produces("application/json")
@Consumes("application/json")
public class AccountantResource {

    private static final Logger LOGGER = Logger.getLogger(AccountantResource.class.getName());

    @Inject
    private AccountantsService accountantsService;

    @GET
    public List<Accountant> get() {
        return accountantsService.get();
    }

    @POST
    public Response create(Accountant accountant) {
        accountantsService.create(accountant);
        return Response.ok(accountant).status(201).build();
    }

    @PUT
    @Path("{id}")
    public Accountant update(@PathParam Long id, Accountant accountant) {
        return accountantsService.update(id, accountant);
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
