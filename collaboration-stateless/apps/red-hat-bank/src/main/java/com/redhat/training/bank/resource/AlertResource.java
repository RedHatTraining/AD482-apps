package com.redhat.training.bank.resource;

import com.redhat.training.bank.event.HighRiskWithdrawnWasDetected;
import com.redhat.training.bank.event.HighValueDepositWasDetected;
import com.redhat.training.bank.event.LowRiskWithdrawnWasDetected;
import com.redhat.training.bank.event.ModerateRiskWithdrawnWasDetected;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.SseElementType;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("alerts")
public class AlertResource {

    private static final Logger LOGGER = Logger.getLogger(AlertResource.class.getName());

    @Inject @Channel("in-memory-deposit-alerts")
    Publisher<HighValueDepositWasDetected> depositAlerts;

    @Inject @Channel("in-memory-low-risk-alerts")
    Publisher<LowRiskWithdrawnWasDetected> lowRiskAlerts;

    @Inject @Channel("in-memory-moderate-risk-alerts")
    Publisher<ModerateRiskWithdrawnWasDetected> moderateRiskAlerts;

    @Inject @Channel("in-memory-high-risk-alerts")
    Publisher<HighRiskWithdrawnWasDetected> highRiskAlerts;

    // Event processors ------------------------------------------------------------------------------------------------

    @Incoming("high-value-deposit-alert-read")
    @Outgoing("in-memory-deposit-alerts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public HighValueDepositWasDetected processDepositEvent(HighValueDepositWasDetected event) {
        return event;
    }

    @Incoming("low-risk-withdrawn-alert-read")
    @Outgoing("in-memory-low-risk-alerts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public LowRiskWithdrawnWasDetected processLowRiskEvent(LowRiskWithdrawnWasDetected event) {
        return event;
    }

    @Incoming("moderate-risk-withdrawn-alert-read")
    @Outgoing("in-memory-moderate-risk-alerts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public ModerateRiskWithdrawnWasDetected processModerateRiskEvent(ModerateRiskWithdrawnWasDetected event) {
        return event;
    }

    @Incoming("high-risk-withdrawn-alert-read")
    @Outgoing("in-memory-high-risk-alerts")
    @Broadcast
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public HighRiskWithdrawnWasDetected processHighRiskEvent(HighRiskWithdrawnWasDetected event) {
        return event;
    }

    // Endpoints -------------------------------------------------------------------------------------------------------

    @GET
    @Path("deposits")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<HighValueDepositWasDetected> getHighValueDepositAlerts() {
        return depositAlerts;
    }

    @GET
    @Path("withdrawn/low")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<LowRiskWithdrawnWasDetected> getLowRiskAlerts() {
        return lowRiskAlerts;
    }

    @GET
    @Path("withdrawn/moderate")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<ModerateRiskWithdrawnWasDetected> getModerateRiskAlerts() {
        return moderateRiskAlerts;
    }

    @GET
    @Path("withdrawn/high")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Publisher<HighRiskWithdrawnWasDetected> getHighRiskAlerts() {
        return highRiskAlerts;
    }
}
