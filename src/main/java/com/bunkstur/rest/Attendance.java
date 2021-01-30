package com.bunkstur.rest;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/api/v1/attendance")
@RequestScoped
@RolesAllowed("**")
public class Attendance {
    @GET
    public String All() {
        return "All attend";
    }

    @GET
    @Path("{id}")
    public String Single(@PathParam("id") final String name) {
        return "Attend" + name;
    }

    @POST
    public String Add() {
        return "Add";
    }
}
