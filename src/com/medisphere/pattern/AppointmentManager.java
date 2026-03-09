package com.medisphere.pattern;

import com.medisphere.model.*;
import java.time.*;
import java.util.*;

/**
 * Singleton that manages all appointment lifecycle operations,
 * integrating with the Observer pattern via AppointmentSubject.
 */
public class AppointmentManager {
    private static AppointmentManager instance;

    private final DataStore dataStore = DataStore.getInstance();
    private final AppointmentSubject subject = new AppointmentSubject();

    private AppointmentManager() {
        // Register default observers (Observer pattern integration)
        subject.addObserver(new EmailNotificationObserver());
        subject.addObserver(new InAppNotificationObserver());
    }

    public static synchronized AppointmentManager getInstance() {
        if (instance == null) instance = new AppointmentManager();
        return instance;
    }

    public AppointmentSubject getSubject() { return subject; }

    /** Book a new appointment */
    public Appointment bookAppointment(String patientId, String doctorId,
                                        LocalDate date, LocalTime time, String reason) {
        Patient patient = (Patient) dataStore.findUserById(patientId);
        Doctor  doctor  = (Doctor)  dataStore.findUserById(doctorId);
        if (patient == null || doctor == null) return null;

        Appointment apt = new Appointment(
                patientId, patient.getName(),
                doctorId, doctor.getName(),
                doctor.getSpecialty(), date, time, reason);

        dataStore.addAppointment(apt);
        patient.addPastDoctorId(doctorId);
        subject.notifyBooked(apt);         // Notify observers
        return apt;
    }

    /** Cancel an appointment */
    public boolean cancelAppointment(String appointmentId) {
        Appointment apt = dataStore.findAppointmentById(appointmentId);
        if (apt == null) return false;
        apt.setStatus(AppointmentStatus.CANCELLED);
        subject.notifyCancelled(apt);     // Notify observers
        return true;
    }

    /** Reschedule an appointment */
    public boolean rescheduleAppointment(String appointmentId, LocalDate newDate, LocalTime newTime) {
        Appointment apt = dataStore.findAppointmentById(appointmentId);
        if (apt == null) return false;
        apt.setDate(newDate);
        apt.setTime(newTime);
        apt.setStatus(AppointmentStatus.RESCHEDULED);
        subject.notifyRescheduled(apt);   // Notify observers
        return true;
    }

    /** Generate available time slots for a doctor on a given date */
    public List<TimeSlot> getAvailableSlots(String doctorId, LocalDate date) {
        List<Appointment> existing = dataStore.getAppointmentsForDoctor(doctorId);
        Set<LocalTime> bookedTimes = new HashSet<>();
        for (Appointment a : existing) {
            if (a.getDate().isEqual(date) && a.getStatus() != AppointmentStatus.CANCELLED)
                bookedTimes.add(a.getTime());
        }

        List<TimeSlot> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end   = LocalTime.of(17, 0);
        while (!start.isAfter(end)) {
            slots.add(new TimeSlot(start, !bookedTimes.contains(start)));
            start = start.plusMinutes(30);
        }
        return slots;
    }
}
