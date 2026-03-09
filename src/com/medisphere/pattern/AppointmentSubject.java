package com.medisphere.pattern;

import com.medisphere.model.Appointment;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject in the Observer Pattern.
 * Manages observer registration and notifies them of appointment events.
 */
public class AppointmentSubject {
    private final List<NotificationObserver> observers = new ArrayList<>();

    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyBooked(Appointment appointment) {
        for (NotificationObserver obs : observers) obs.onAppointmentBooked(appointment);
    }

    public void notifyCancelled(Appointment appointment) {
        for (NotificationObserver obs : observers) obs.onAppointmentCancelled(appointment);
    }

    public void notifyRescheduled(Appointment appointment) {
        for (NotificationObserver obs : observers) obs.onAppointmentRescheduled(appointment);
    }

    public void notifyReminder(Appointment appointment) {
        for (NotificationObserver obs : observers) obs.onAppointmentReminder(appointment);
    }
}
