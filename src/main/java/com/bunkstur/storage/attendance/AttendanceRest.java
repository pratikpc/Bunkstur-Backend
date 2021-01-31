package com.bunkstur.storage.attendance;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.reactivex.annotations.NonNull;
import io.smallrye.common.constraint.NotNull;

@RegisterForReflection
public class AttendanceRest {
    private @NotNull @NotEmpty @NotBlank String subject;
    private @NotNull @NotEmpty @NotBlank String attendanceType;
    private @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date;
    private @NotNull @JsonFormat(pattern = "HH:mm") LocalTime start;
    private @NotNull @JsonFormat(pattern = "HH:mm") LocalTime end;

    public boolean Empty() throws IllegalArgumentException {
        return (subject == "" || subject == null || attendanceType == "" || attendanceType == null || date == null || start == null || end == null);
    }

    /**
     * @return the attendanceType
     */
    public String getAttendanceType() {
        return attendanceType;
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return the end
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * @return the start
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param attendanceType the attendanceType to set
     */
    public void setAttendanceType(@NotNull @NonNull String attendanceType) {
        this.attendanceType = attendanceType;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(LocalTime end) {
        this.end = end;
    }

    /**
     * @param start the start to set
     */
    public void setStart(LocalTime start) {
        this.start = start;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
}
