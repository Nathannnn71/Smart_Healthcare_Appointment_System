package com.medisphere.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private static int idCounter = 10000;

    private String appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private Specialty specialty;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private String reason;
    private String notes;
    private LocalDate createdDate;

    public Appointment(String patientId, String patientName,
                       String doctorId, String doctorName,
                       Specialty specialty, LocalDate date, LocalTime time, String reason) {
        this.appointmentId = "APT" + (++idCounter);
        this.patientId  = patientId;
        this.patientName = patientName;
        this.doctorId   = doctorId;
        this.doctorName = doctorName;
        this.specialty  = specialty;
        this.date       = date;
        this.time       = time;
        this.reason     = reason;
        this.status     = AppointmentStatus.CONFIRMED;
        this.notes      = "";
        this.createdDate = LocalDate.now();
    }

    // Getters
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId()     { return patientId; }
    public String getPatientName()   { return patientName; }
    public String getDoctorId()      { return doctorId; }
    public String getDoctorName()    { return doctorName; }
    public Specialty getSpecialty()  { return specialty; }
    public LocalDate getDate()       { return date; }
    public LocalTime getTime()       { return time; }
    public AppointmentStatus getStatus() { return status; }
    public String getReason()        { return reason; }
    public String getNotes()         { return notes; }
    public LocalDate getCreatedDate(){ return createdDate; }

    // Setters
    public void setStatus(AppointmentStatus s) { this.status = s; }
    public void setDate(LocalDate d)            { this.date = d; }
    public void setTime(LocalTime t)            { this.time = t; }
    public void setNotes(String n)              { this.notes = n; }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public String getFormattedTime() {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public String getFormattedDateTime() {
        return getFormattedDate() + " at " + getFormattedTime();
    }

    public boolean isUpcoming() {
        return (date.isAfter(LocalDate.now()) ||
                (date.isEqual(LocalDate.now()) && time.isAfter(LocalTime.now())))
               && status == AppointmentStatus.CONFIRMED;
    }

    @Override
    public String toString() {
        return String.format("Apt[%s] %s with Dr.%s on %s",
                appointmentId, patientName, doctorName, getFormattedDateTime());
    }
}
