package com.redhat.training.bank.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.training.bank.command.DepositAmountInBankAccount;
import com.redhat.training.bank.command.WithdrawAmountFromBankAccount;
import com.redhat.training.bank.event.AmountWasDeposited;
import com.redhat.training.bank.event.AmountWasWithdrawn;
import com.redhat.training.bank.event.BankAccountWasCreated;
import com.redhat.training.bank.model.BankAccount;
import io.quarkus.panache.common.Sort;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Path("accounts")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class AccountResource {

    private static final Logger LOGGER = Logger.getLogger(AccountResource.class.getName());

    @Inject @Channel("bank-account-was-created-write")
    Emitter<Record<Long, BankAccountWasCreated>> creationEmitter;

    @Inject @Channel("amount-was-deposited-write")
    Emitter<Record<Long, AmountWasDeposited>> depositEmitter;

    @Inject @Channel("amount-was-withdrawn-write")
    Emitter<Record<Long, AmountWasWithdrawn>> withdrawnEmitter;

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
        notifyAboutBankAccountCreation(bankAccount);

        return Response.ok(bankAccount).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}/deposit")
    public BankAccount deposit(@PathParam Long id, DepositAmountInBankAccount deposit) {

        BankAccount entity = depositAmountInBankAccount(id, deposit);

        notifyAboutDeposit(deposit);

        return entity;
    }

    @PUT
    @Path("{id}/withdraw")
    public BankAccount withdraw(@PathParam Long id, WithdrawAmountFromBankAccount withdrawal) {

        BankAccount entity = withdrawAmountFromBankAccount(id, withdrawal);

        notifyAboutWithdrawal(withdrawal);

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

    @Transactional
    public BankAccount depositAmountInBankAccount(Long bankAccountId, DepositAmountInBankAccount deposit) {
        if (deposit.bankAccountId == null || !deposit.bankAccountId.equals(bankAccountId)) {
            throw new WebApplicationException(
                    "Bank Account ID was not set on request.",
                    Response.Status.BAD_REQUEST
            );
        }

        if (deposit.amount == null || deposit.amount <= 0) {
            throw new WebApplicationException(
                    "Unprocessable amount on request.",
                    Response.Status.BAD_REQUEST
            );
        }

        BankAccount entity = BankAccount.findById(bankAccountId);

        if (entity == null) {
            throw new WebApplicationException(
                    "Bank Account with id of " + bankAccountId + " does not exist.",
                    Response.Status.NOT_FOUND
            );
        }

        entity.balance = entity.balance + deposit.amount;

        return entity;
    }

    @Transactional
    public BankAccount withdrawAmountFromBankAccount(Long bankAccountId, WithdrawAmountFromBankAccount withdrawal) {
        if (withdrawal.bankAccountId == null || !withdrawal.bankAccountId.equals(bankAccountId)) {
            throw new WebApplicationException(
                    "Bank Account ID was not set on request.",
                    Response.Status.BAD_REQUEST
            );
        }

        if (withdrawal.amount == null || withdrawal.amount <= 0) {
            throw new WebApplicationException(
                    "Unprocessable amount on request.",
                    Response.Status.BAD_REQUEST
            );
        }

        BankAccount entity = BankAccount.findById(bankAccountId);

        if (entity == null) {
            throw new WebApplicationException(
                    "Bank Account with id of " + bankAccountId + " does not exist.",
                    Response.Status.NOT_FOUND
            );
        }

        if (entity.balance < withdrawal.amount) {
            throw new WebApplicationException(
                    "Insufficient funds for withdraw.",
                    Response.Status.CONFLICT
            );
        }

        entity.balance = entity.balance - withdrawal.amount;

        return entity;
    }

    private void notifyAboutBankAccountCreation(BankAccount bankAccount)
    {
        LOGGER.info(
                "BankAccountWasCreated - ID: " + bankAccount.id
                + ", Balance: " + bankAccount.balance
        );

        creationEmitter.send(
                Record.of(
                        bankAccount.id,
                        new BankAccountWasCreated(
                            bankAccount.id,
                            bankAccount.balance
                        )
                )
        );
    }

    private void notifyAboutDeposit(DepositAmountInBankAccount deposit) {
        LOGGER.info(
                "AmountWasDeposited - Account ID: " + deposit.bankAccountId
                + ", Amount: " + deposit.amount
        );

        depositEmitter.send(
                Record.of(
                        deposit.bankAccountId,
                        new AmountWasDeposited(
                                deposit.bankAccountId,
                                deposit.amount
                        )
                )
        );
    }

    private void notifyAboutWithdrawal(WithdrawAmountFromBankAccount withdrawal) {
        LOGGER.info(
                "AmountWasWithdrawn - Account ID: " + withdrawal.bankAccountId
                + ", Amount: " + withdrawal.amount
        );

        withdrawnEmitter.send(
                Record.of(
                        withdrawal.bankAccountId,
                        new AmountWasWithdrawn(
                                withdrawal.bankAccountId,
                                withdrawal.amount
                        )
                )
        );
    }
}
