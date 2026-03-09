package com.medisphere.pattern;

import com.medisphere.model.Appointment;

/**
 * =========================================================
 * DESIGN PATTERN 2: OBSERVER (Behavioral)
 * =========================================================
 * Purpose: Defines a one-to-many dependency so that when
 * an appointment changes state, all registered observers
 * (e.g., EmailNotification, InAppNotification) are notified.
 *
 * This interface is the Observer role.
 * =========================================================
 */
public interface NotificationObserver {
    void onAppointmentBooked(Appointment appointment);
    void onAppointmentCancelled(Appointment appointment);
    void onAppointmentRescheduled(Appointment appointment);
    void onAppointmentReminder(Appointment appointment);
}
