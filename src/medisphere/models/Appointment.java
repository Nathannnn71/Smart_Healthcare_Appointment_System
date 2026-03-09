package medisphere.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Core entity representing a healthcare appointment.
 */
public class Appointment {
    private final String appointmentId;
    private final String patientId;
    private String doctorId;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private String symptoms;
    private String patientNotes;
    private String doctorNotes;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appointment(String appointmentId, String patientId, String doctorId,
                       LocalDate date, LocalTime time, String symptoms) {
        this.appointmentId = appointmentId;
        this.patientId     = patientId;
        this.doctorId      = doctorId;
        this.date          = date;
        this.time          = time;
        this.symptoms      = symptoms;
        this.status        = AppointmentStatus.PENDING;
        this.patientNotes  = "";
        this.doctorNotes   = "";
        this.createdAt     = LocalDateTime.now();
        this.updatedAt     = LocalDateTime.now();
    }

    // Getters
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId()     { return patientId; }
    public String getDoctorId()      { return doctorId; }
    public LocalDate getDate()       { return date; }
    public LocalTime getTime()       { return time; }
    public AppointmentStatus getStatus() { return status; }
    public String getSymptoms()      { return symptoms; }
    public String getPatientNotes()  { return patientNotes; }
    public String getDoctorNotes()   { return doctorNotes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters (update timestamp on mutation)
    public void setDate(LocalDate date)          { this.date = date; touch(); }
    public void setTime(LocalTime time)          { this.time = time; touch(); }
    public void setDoctorId(String id)           { this.doctorId = id; touch(); }
    public void setStatus(AppointmentStatus s)   { this.status = s; touch(); }
    public void setSymptoms(String s)            { this.symptoms = s; touch(); }
    public void setPatientNotes(String n)        { this.patientNotes = n; touch(); }
    public void setDoctorNotes(String n)         { this.doctorNotes = n; touch(); }

    private void touch() { this.updatedAt = LocalDateTime.now(); }

    // Formatted helpers
    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }
    public String getFormattedTime() {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
    public String getFormattedDateTime() {
        return getFormattedDate() + "  at  " + getFormattedTime();
    }
    public String getDayOfWeek() {
        return date.getDayOfWeek().toString().charAt(0)
             + date.getDayOfWeek().toString().substring(1).toLowerCase();
    }

    /** Whether this appointment is upcoming (not in the past) */
    public boolean isUpcoming() {
        return !date.isBefore(LocalDate.now());
    }
}
