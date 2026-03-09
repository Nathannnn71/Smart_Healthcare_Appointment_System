package medisphere.patterns.observer;

import medisphere.data.DataStore;
import medisphere.models.*;

/**
 * ============================================================
 * DESIGN PATTERN 3 — OBSERVER (Behavioral) — Concrete Observer A
 * ============================================================
 * Simulates sending e-mail notifications for appointment events.
 * In production this would call an SMTP / email-API service.
 */
public class EmailNotification implements AppointmentObserver {

    private final DataStore store = DataStore.getInstance();
    private int emailCounter = 5000;

    @Override
    public void onAppointmentBooked(Appointment a) {
        User patient = store.getUserById(a.getPatientId());
        User doctor  = store.getUserById(a.getDoctorId());
        if (patient == null || doctor == null) return;

        String title = "Appointment Booking Confirmed";
        String msg = String.format(
            "Dear %s,\n\nYour appointment with %s has been successfully booked.\n" +
            "Date & Time : %s\nSymptoms    : %s\nStatus      : %s\n\n" +
            "Please arrive 10 minutes early.\n\nRegards,\nMediSphere Team",
            patient.getName(), doctor.getName(),
            a.getFormattedDateTime(), a.getSymptoms(), a.getStatus());

        saveNotification(patient.getUserId(), title, msg, "EMAIL");

        // Notify doctor too
        String docMsg = String.format(
            "Dear %s,\n\nA new appointment has been booked with you by %s.\n" +
            "Date & Time : %s\n\nMediSphere Team",
            doctor.getName(), patient.getName(), a.getFormattedDateTime());
        saveNotification(doctor.getUserId(), "New Appointment", docMsg, "EMAIL");

        System.out.printf("[EMAIL] Booking confirmation sent to %s (%s)%n",
                patient.getName(), patient.getEmail());
    }

    @Override
    public void onAppointmentCancelled(Appointment a) {
        User patient = store.getUserById(a.getPatientId());
        User doctor  = store.getUserById(a.getDoctorId());
        if (patient == null) return;

        String title = "Appointment Cancellation Notice";
        String msg = String.format(
            "Dear %s,\n\nYour appointment scheduled on %s has been cancelled.\n\n" +
            "If this was unintentional, please rebook through MediSphere.\n\nMediSphere Team",
            patient.getName(), a.getFormattedDateTime());

        saveNotification(patient.getUserId(), title, msg, "EMAIL");
        if (doctor != null) {
            saveNotification(doctor.getUserId(), "Appointment Cancelled",
                "Patient " + patient.getName() + " has cancelled the appointment on " + a.getFormattedDateTime(),
                "EMAIL");
        }
        System.out.printf("[EMAIL] Cancellation notice sent to %s%n", patient.getName());
    }

    @Override
    public void onAppointmentRescheduled(Appointment a) {
        User patient = store.getUserById(a.getPatientId());
        if (patient == null) return;

        String title = "Appointment Rescheduled";
        String msg = String.format(
            "Dear %s,\n\nYour appointment has been rescheduled.\nNew Date & Time : %s\n\nMediSphere Team",
            patient.getName(), a.getFormattedDateTime());

        saveNotification(patient.getUserId(), title, msg, "EMAIL");
        System.out.printf("[EMAIL] Reschedule notice sent to %s%n", patient.getName());
    }

    @Override
    public void onAppointmentStatusChanged(Appointment a) {
        User patient = store.getUserById(a.getPatientId());
        if (patient == null) return;

        String title = "Appointment Status Update";
        String msg = String.format(
            "Dear %s,\n\nThe status of your appointment on %s has changed to: %s\n\nMediSphere Team",
            patient.getName(), a.getFormattedDateTime(), a.getStatus().getLabel());

        saveNotification(patient.getUserId(), title, msg, "EMAIL");
    }

    private void saveNotification(String userId, String title, String msg, String channel) {
        Notification n = new Notification("N" + (++emailCounter), userId, title, msg, channel);
        store.addNotification(n);
    }
}
