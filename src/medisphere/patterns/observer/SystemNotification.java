package medisphere.patterns.observer;

import medisphere.data.DataStore;
import medisphere.models.*;

/**
 * ============================================================
 * DESIGN PATTERN 3 — OBSERVER (Behavioral) — Concrete Observer B
 * ============================================================
 * Creates in-app (system) notifications that appear in the
 * notification bell / panel inside the dashboard.
 */
public class SystemNotification implements AppointmentObserver {

    private final DataStore store = DataStore.getInstance();
    private int sysCounter = 8000;

    @Override
    public void onAppointmentBooked(Appointment a) {
        User patient = store.getUserById(a.getPatientId());
        User doctor  = store.getUserById(a.getDoctorId());
        if (patient != null)
            save(patient.getUserId(),
                 "Appointment Booked",
                 "Your appointment on " + a.getFormattedDateTime() + " is confirmed.");
        if (doctor != null)
            save(doctor.getUserId(),
                 "New Appointment",
                 "New patient appointment scheduled on " + a.getFormattedDateTime() + ".");
    }

    @Override
    public void onAppointmentCancelled(Appointment a) {
        User patient = store.getUserById(a.getPatientId());
        User doctor  = store.getUserById(a.getDoctorId());
        if (patient != null)
            save(patient.getUserId(),
                 "Appointment Cancelled",
                 "Your appointment on " + a.getFormattedDateTime() + " has been cancelled.");
        if (doctor != null)
            save(doctor.getUserId(),
                 "Appointment Cancelled",
                 "An appointment on " + a.getFormattedDateTime() + " was cancelled by patient.");
    }

    @Override
    public void onAppointmentRescheduled(Appointment a) {
        User patient = store.getUserById(a.getPatientId());
        if (patient != null)
            save(patient.getUserId(),
                 "Appointment Rescheduled",
                 "Your appointment has been moved to " + a.getFormattedDateTime() + ".");
    }

    @Override
    public void onAppointmentStatusChanged(Appointment a) {
        User patient = store.getUserById(a.getPatientId());
        if (patient != null)
            save(patient.getUserId(),
                 "Status Updated: " + a.getStatus().getLabel(),
                 "Your appointment on " + a.getFormattedDateTime()
                 + " is now " + a.getStatus().getLabel() + ".");
    }

    private void save(String userId, String title, String message) {
        Notification n = new Notification("SN" + (++sysCounter), userId, title, message, "SYSTEM");
        store.addNotification(n);
    }
}
