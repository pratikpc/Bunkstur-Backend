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

    @GET
    @Path("attendance")
    public Multi<Attendance> UserAttendance(@Context SecurityContext context) {
        final var user = Utils.GetUser(context);
        return attendanceAsyncService.SingleUser(user.getName());
    }

    @DELETE
    @Path("attendance/{uuid}")
    public Uni<Response> RemoveUserAttendance(@PathParam("uuid") @NonNull String uuid,
            @Context SecurityContext context) {
        final var user = Utils.GetUser(context);
        return attendanceAsyncService.RemoveSingleRecord(user.getName(), uuid)
                .map(unused -> Response.status(Status.ACCEPTED).build());
    }

    @POST
    @Path("attendance")
    public Uni<Response> AddUserAttendance(@NonNull AttendanceRest attendance, @Context SecurityContext context) {
        final var acceptable = !attendance.Empty();
        if (!acceptable)
            return Uni.createFrom().item(Response.status(Status.BAD_REQUEST).build());
        final var user = Utils.GetUser(context);

        return Uni.combine().all().unis(
                // Add Subject to list of Subjects
                subjectAsyncService.Add(attendance.getSubject()),
                // Add user to list of user
                userAsyncService.Add(new User(user.getName(), user.getSubject())),
                // Add Attendance information to list
                attendanceAsyncService.Add(new Attendance(user.getName(), attendance))).discardItems()
                .map(unused -> Response.status(Status.ACCEPTED).build());
    }
    }
}
