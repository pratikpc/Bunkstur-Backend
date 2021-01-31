package com.bunkstur.rest;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.bunkstur.storage.subjects.SubjectAsyncService;

@Path("/api/v1/attendance")
@RequestScoped
@RolesAllowed("**")
public class Attendance {
    @Inject
    SubjectAsyncService subjectAsyncService;

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
