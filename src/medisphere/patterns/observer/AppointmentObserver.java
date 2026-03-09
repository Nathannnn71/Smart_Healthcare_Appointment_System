package medisphere.patterns.observer;

import medisphere.models.Appointment;

/**
 * ============================================================
 * DESIGN PATTERN 3 — OBSERVER (Behavioral) — Observer interface
 * ============================================================
 * Any class that wants to be notified of appointment events
 * must implement this interface.
 */
public interface AppointmentObserver {

    /**
     * Called when an appointment is booked.
     * @param appointment The newly created appointment.
     */
    void onAppointmentBooked(Appointment appointment);

    /**
     * Called when an appointment is cancelled.
     * @param appointment The cancelled appointment.
     */
    void onAppointmentCancelled(Appointment appointment);

    /**
     * Called when an appointment is rescheduled.
     * @param appointment The updated appointment.
     */
    void onAppointmentRescheduled(Appointment appointment);

    /**
     * Called when an appointment status changes (e.g. CONFIRMED, COMPLETED).
     * @param appointment The updated appointment.
     */
    void onAppointmentStatusChanged(Appointment appointment);
}
