package com.redhat.training.bank.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.*;
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

// TODO: Import the NewBankAccount message
import com.redhat.training.bank.message.NewBankAccount;
import com.redhat.training.bank.model.BankAccount;
import com.redhat.training.bank.command.DepositAmountInBankAccount;
import com.redhat.training.bank.command.WithdrawAmountFromBankAccount;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.panache.common.Sort;

@Path("accounts")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class AccountResource {

    private static final Logger LOGGER = Logger.getLogger(AccountResource.class.getName());

    // TODO: Add an emitter for the bank account creation messages
    @Inject @Channel("new-bank-account-out")
    Emitter<NewBankAccount> emitter;

    @GET
    public List<BankAccount> get() {
        return BankAccount.listAll(Sort.by("id"));
    }

    @GET
    @Path("{id}")
    public BankAccount getSingle(@PathParam Long id) {
        BankAccount entity = BankAccount.findById(id);

        if (entity == null) {
            throw new WebApplicationException(
                    "Bank Account with id of " + id + " does not exist.",
                    Response.Status.NOT_FOUND
            );
        }

        return entity;
    }

    @POST
    public Response create(BankAccount bankAccount) {

        createBankAccount(bankAccount);
        sendMessageAboutNewBankAccount(bankAccount);

        return Response.ok(bankAccount).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}/deposit")
    @Transactional
    public BankAccount deposit(@PathParam Long id, DepositAmountInBankAccount deposit) {

        if (deposit.id == null) {
            throw new WebApplicationException(
                    "Bank Account ID was not set on request.",
                    Response.Status.BAD_REQUEST
                    );
        }

        if (deposit.amount <= 0) {
            throw new WebApplicationException(
                    "Unprocessable amount on request.",
                    Response.Status.BAD_REQUEST
            );
        }

        BankAccount entity = BankAccount.findById(id);

        if (entity == null) {
            throw new WebApplicationException(
                    "Bank Account with id of " + id + " does not exist.",
                    Response.Status.NOT_FOUND
            );
        }

        entity.balance = entity.balance + deposit.amount;

        return entity;
    }

    @PUT
    @Path("{id}/withdraw")
    @Transactional
    public BankAccount withdraw(@PathParam Long id, WithdrawAmountFromBankAccount withdraw) {

        if (withdraw.id == null) {
            throw new WebApplicationException(
                    "Bank Account ID was not set on request.",
                    Response.Status.BAD_REQUEST
            );
        }

        if (withdraw.amount <= 0) {
            throw new WebApplicationException(
                    "Unprocessable amount on request.",
                    Response.Status.BAD_REQUEST
            );
        }

        BankAccount entity = BankAccount.findById(id);

        if (entity == null) {
            throw new WebApplicationException(
                    "Bank Account with id of " + id + " does not exist.",
                    Response.Status.NOT_FOUND
            );
        }

        if (entity.balance < withdraw.amount) {
            throw new WebApplicationException(
                    "Insufficient funds for withdraw.",
                    Response.Status.CONFLICT
            );
        }

        entity.balance = entity.balance - withdraw.amount;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        BankAccount entity = BankAccount.findById(id);

        if (entity == null) {
            throw new WebApplicationException(
                    "Bank Account with id of " + id + " does not exist.",
                    Response.Status.NOT_FOUND
            );
        }

        entity.delete();
        LOGGER.info("Deleted bank account - ID: " + id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
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

    @Transactional
    public void createBankAccount(BankAccount bankAccount) {
        if (bankAccount.balance == null || bankAccount.balance < 0) {
            throw new WebApplicationException(
                    "Invalid amount to open a bank account.",
                    Response.Status.BAD_REQUEST
            );
        }

        if (bankAccount.id != null) {
            throw new WebApplicationException(
                    "Id was invalidly set on request.",
                    Response.Status.BAD_REQUEST
            );
        }

        bankAccount.persist();
    }

    private void sendMessageAboutNewBankAccount(BankAccount bankAccount)
    {
        LOGGER.info(
                "New Bank Account - ID: " + bankAccount.id
                + ", Balance: " + bankAccount.balance
        );

        // TODO: Send a message about the new bank account
        emitter.send(
                new NewBankAccount(
                        bankAccount.id,
                        bankAccount.balance
                )
        );
    }
}
