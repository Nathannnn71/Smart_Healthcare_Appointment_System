package medisphere.patterns.observer;

import medisphere.models.Appointment;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * DESIGN PATTERN 3 — OBSERVER (Behavioral) — Subject
 * ============================================================
 * NotificationService is the "subject" (publisher).  It
 * maintains a list of observers and broadcasts appointment
 * events to all registered observers.
 */
public class NotificationService {

    private final List<AppointmentObserver> observers = new ArrayList<>();

    // ── Observer registration ────────────────────────────────
    public void registerObserver(AppointmentObserver observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    public void removeObserver(AppointmentObserver observer) {
        observers.remove(observer);
    }

    // ── Event broadcast methods ──────────────────────────────
    public void notifyBooked(Appointment appointment) {
        for (AppointmentObserver obs : observers) obs.onAppointmentBooked(appointment);
    }

    public void notifyCancelled(Appointment appointment) {
        for (AppointmentObserver obs : observers) obs.onAppointmentCancelled(appointment);
    }

    public void notifyRescheduled(Appointment appointment) {
        for (AppointmentObserver obs : observers) obs.onAppointmentRescheduled(appointment);
    }

    public void notifyStatusChanged(Appointment appointment) {
        for (AppointmentObserver obs : observers) obs.onAppointmentStatusChanged(appointment);
    }

    public int getObserverCount() { return observers.size(); }
}
