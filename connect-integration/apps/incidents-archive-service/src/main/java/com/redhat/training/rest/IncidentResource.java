package com.redhat.training.rest;

import com.redhat.training.model.Incident;
import com.redhat.training.service.IncidentSearchService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.List;

@Path("/incidents")
public class IncidentResource {

    @Inject
    private IncidentSearchService incidentSearchService;

    @GET
    @Path("/search")
    public List<Incident> search(@QueryParam("id") Long id, @QueryParam("requesterName") String requesterName) throws IOException {
        if (id != null) {
            return incidentSearchService.searchById(id);
        } else if (requesterName != null) {
            return incidentSearchService.searchByRequesterName(requesterName);
        } else {
            return incidentSearchService.searchAll();
        }
    }

}
