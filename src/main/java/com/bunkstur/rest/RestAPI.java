package com.bunkstur.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.bunkstur.Utils;
import com.bunkstur.storage.attendance.Attendance;
import com.bunkstur.storage.attendance.AttendanceAsyncService;
import com.bunkstur.storage.attendance.AttendanceRest;
import com.bunkstur.storage.subjects.SubjectAsyncService;
import com.bunkstur.storage.users.User;
import com.bunkstur.storage.users.UsersAsyncService;

import io.reactivex.annotations.NonNull;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("/api/v1/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("**")
public class RestAPI {

    @Inject
    SubjectAsyncService subjectAsyncService;

    @Inject
    UsersAsyncService userAsyncService;

    @Inject
    AttendanceAsyncService attendanceAsyncService;

    @GET
    @Path("subjects")
    public Multi<String> Top30Subjects() {
        // Get only Top 30 results
        return subjectAsyncService.GetSubjects();
    }
    }
}
