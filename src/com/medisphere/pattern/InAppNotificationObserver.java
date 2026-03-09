package com.medisphere.pattern;

import com.medisphere.model.Appointment;
import com.medisphere.model.User;

/**
 * Concrete Observer: delivers in-app notifications to users.
 */
public class InAppNotificationObserver implements NotificationObserver {

    @Override
    public void onAppointmentBooked(Appointment a) {
        String msg = "✓ Appointment confirmed with Dr. " + a.getDoctorName()
                + " on " + a.getFormattedDateTime();
        notifyUser(a.getPatientId(), msg);
        notifyUser(a.getDoctorId(), "📅 New appointment: " + a.getPatientName()
                + " on " + a.getFormattedDateTime());
    }

    @Override
    public void onAppointmentCancelled(Appointment a) {
        String msg = "✗ Appointment with Dr. " + a.getDoctorName()
                + " on " + a.getFormattedDate() + " was cancelled.";
        notifyUser(a.getPatientId(), msg);
        notifyUser(a.getDoctorId(), "✗ Appointment cancelled: " + a.getPatientName()
                + " on " + a.getFormattedDate());
    }

    @Override
    public void onAppointmentRescheduled(Appointment a) {
        String msg = "⟳ Appointment rescheduled to " + a.getFormattedDateTime()
                + " with Dr. " + a.getDoctorName();
        notifyUser(a.getPatientId(), msg);
        notifyUser(a.getDoctorId(), "⟳ Appointment rescheduled with " + a.getPatientName()
                + " to " + a.getFormattedDateTime());
    }

    @Override
    public void onAppointmentReminder(Appointment a) {
        String msg = "🔔 Reminder: Appointment with Dr. " + a.getDoctorName()
                + " tomorrow at " + a.getFormattedTime();
        notifyUser(a.getPatientId(), msg);
    }

    private void notifyUser(String userId, String message) {
        User user = DataStore.getInstance().findUserById(userId);
        if (user != null) {
            user.addNotification(message);
            System.out.println("[InApp] Notified " + user.getName() + ": " + message);
        }
    }
}
