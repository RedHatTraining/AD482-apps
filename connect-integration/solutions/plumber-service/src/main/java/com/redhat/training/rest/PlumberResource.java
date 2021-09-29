package com.redhat.training.rest;

import com.redhat.training.model.Plumber;
import com.redhat.training.service.PlumberService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("plumbers")
@Produces("application/json")
@Consumes("application/json")
public class PlumberResource {

    @Inject
    private PlumberService plumberService;

    @GET
    public List<Plumber> get() {
        return plumberService.get();
    }

}
