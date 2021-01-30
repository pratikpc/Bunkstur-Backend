package com.bunkstur.rest;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api/v1/subjects")
@RequestScoped
@RolesAllowed("**")
public class Subjects {
    @GET
    public String All(){
        return "All subjects";
    }
}
