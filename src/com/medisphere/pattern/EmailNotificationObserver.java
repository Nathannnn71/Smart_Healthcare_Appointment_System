package com.medisphere.pattern;

import com.medisphere.model.Appointment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Observer: simulates email notifications.
 * In production, this would connect to a mail server (e.g., JavaMail).
 */
public class EmailNotificationObserver implements NotificationObserver {
    private static final List<String> emailLog = new ArrayList<>();

    private void sendEmail(String subject, String to, String body) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String log = String.format("[%s] EMAIL to %s | Subject: %s | %s",
                timestamp, to, subject, body);
        emailLog.add(log);
        System.out.println("[EmailService] Sent: " + log);
    }

    @Override
    public void onAppointmentBooked(Appointment a) {
        sendEmail(
            "Appointment Confirmed - " + a.getAppointmentId(),
            a.getPatientName(),
            "Your appointment with Dr. " + a.getDoctorName() +
            " (" + a.getSpecialty().getDisplayName() + ") is confirmed for " +
            a.getFormattedDateTime() + ". ID: " + a.getAppointmentId()
        );
    }

    @Override
    public void onAppointmentCancelled(Appointment a) {
        sendEmail(
            "Appointment Cancelled - " + a.getAppointmentId(),
            a.getPatientName(),
            "Your appointment with Dr. " + a.getDoctorName() +
            " on " + a.getFormattedDate() + " has been cancelled."
        );
    }

    @Override
    public void onAppointmentRescheduled(Appointment a) {
        sendEmail(
            "Appointment Rescheduled - " + a.getAppointmentId(),
            a.getPatientName(),
            "Your appointment with Dr. " + a.getDoctorName() +
            " has been rescheduled to " + a.getFormattedDateTime() + "."
        );
    }

    @Override
    public void onAppointmentReminder(Appointment a) {
        sendEmail(
            "Reminder: Upcoming Appointment Tomorrow",
            a.getPatientName(),
            "Reminder: You have an appointment with Dr. " + a.getDoctorName() +
            " tomorrow at " + a.getFormattedTime() + "."
        );
    }

    public static List<String> getEmailLog() { return emailLog; }
}
