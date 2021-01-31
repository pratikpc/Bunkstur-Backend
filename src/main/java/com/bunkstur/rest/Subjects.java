package com.bunkstur.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bunkstur.storage.subjects.SubjectAsyncService;

import io.smallrye.mutiny.Uni;

@Path("/api/v1/subjects")
@RequestScoped
@RolesAllowed("**")
public class Subjects {

    @Inject
    SubjectAsyncService subjectAsyncService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<String>> All() {
        
        return subjectAsyncService.GetAllSubjects();
    }
}
