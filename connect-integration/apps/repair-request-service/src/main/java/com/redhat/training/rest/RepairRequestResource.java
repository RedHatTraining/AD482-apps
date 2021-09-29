package com.redhat.training.rest;

import com.redhat.training.model.RepairRequest;
import com.redhat.training.service.RepairRequestService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("requests")
@Produces("application/json")
@Consumes("application/json")
public class RepairRequestResource {

    @Inject
    private RepairRequestService repairRequestService;

    @GET
    public List<RepairRequest> get() {
        return repairRequestService.get();
    }

    @POST
    public Response create(RepairRequest repairRequest) {
        repairRequestService.create(repairRequest);
        return Response.ok(repairRequest).status(201).build();
    }

}
