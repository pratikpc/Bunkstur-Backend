package com.bunkstur.storage.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@RegisterForReflection
public class Attendance {
    private final String userId;
    private final String uuid;
    private final String subject;
    private final String attendanceType;
    private final @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date;
    private final @JsonFormat(pattern = "HH:mm") LocalTime start;
    private final @JsonFormat(pattern = "HH:mm") LocalTime end;

    private static final DateTimeFormatter LocalTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DayTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Attendance(String userId, AttendanceRest attendance) {
        this(userId, attendance.getSubject(), attendance.getAttendanceType(), attendance.getDate(),
                attendance.getStart(), attendance.getEnd());
    }

    public Attendance(final String userId, final String subject, final String attendanceType, final LocalDate date,
            final LocalTime start, final LocalTime end, final String uuid) {
        if (!List.of("A", // Absent
                "P", // Present
                "N" // No Lecture
        ).contains(attendanceType)) {
            throw new IllegalArgumentException("Input must be A, P or N");
        }
        // Less than
        if (end.compareTo(start) < 0) {
            throw new IllegalArgumentException("Start must always be less than End");
        }
        this.userId = userId;
        this.uuid = uuid;
        this.attendanceType = attendanceType;
        this.subject = subject;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public Attendance(final String userId, final String subject, final String attendanceType, final LocalDate date,
            final LocalTime start, final LocalTime end) {
        this(userId, subject, attendanceType, date, start, end, UUID.randomUUID().toString());
    }

    public Attendance(final Map<String, AttributeValue> attendance) {
        this(attendance.get(Columns.USER_ID).s(),
                // Subject
                attendance.get(Columns.SUBJECT).s(),
                // Attendance Type
                attendance.get(Columns.ATT_TYPE).s(),
                // Date
                LocalDate.parse(attendance.get(Columns.DATE).s(), DayTimeFormatter),
                // Start
                LocalTime.parse(attendance.get(Columns.TIME_START).s(), LocalTimeFormatter),
                // End
                LocalTime.parse(attendance.get(Columns.TIME_END).s(), LocalTimeFormatter),
                // UUID
                attendance.get(Columns.UUID).s());
            }

    // Mapper Function
    public Map<String, AttributeValue> AsRow() {
        return Map.of(Columns.USER_ID, AttributeValue.builder().s(userId).build(),
                // Attribute
                Columns.ATT_TYPE, AttributeValue.builder().s(attendanceType).build(),
                // Subject
                Columns.SUBJECT, AttributeValue.builder().s(subject).build(),
                // UUID
                Columns.UUID, AttributeValue.builder().s(uuid).build(),
                // Date
                Columns.DATE, AttributeValue.builder().s(date.format(DayTimeFormatter)).build(),
                // Time Start
                Columns.TIME_START, AttributeValue.builder().s(start.format(LocalTimeFormatter)).build(),
                // Time End
                Columns.TIME_END, AttributeValue.builder().s(end.format(LocalTimeFormatter)).build());
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
     * @return the uuid
     */
    public String getUuid() {
        return uuid != null ? uuid : UUID.randomUUID().toString();
    }

    
    /**
     * @return the attendance type
     */
    public String getAttendanceType() {
        return attendanceType;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return AsRow().toString();
    }
}
