package medisphere.patterns.facade;

import medisphere.data.DataStore;
import medisphere.models.*;
import medisphere.patterns.observer.NotificationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * ============================================================
 * DESIGN PATTERN 5 — FACADE (Structural)
 * ============================================================
 * AppointmentFacade provides a single, simplified interface
 * to the complex appointment sub-system (DataStore persistence,
 * NotificationService events, Doctor-slot management, Patient
 * record updates).  UI layers call only this facade and are
 * shielded from internal complexity.
 */
public class AppointmentFacade {

    private final DataStore            store;
    private final NotificationService  notifier;

    public AppointmentFacade(NotificationService notifier) {
        this.store    = DataStore.getInstance();
        this.notifier = notifier;
    }

    // ────────────────────────────────────────────────────────
    //  Book a new appointment
    // ────────────────────────────────────────────────────────
    public Appointment bookAppointment(String patientId, String doctorId,
                                       LocalDate date, LocalTime time,
                                       String symptoms) {
        // 1. Validate entities exist
        User rawPatient = store.getUserById(patientId);
        User rawDoctor  = store.getUserById(doctorId);
        if (!(rawPatient instanceof Patient) || !(rawDoctor instanceof Doctor))
            throw new IllegalArgumentException("Invalid patient or doctor ID.");

        Patient patient = (Patient) rawPatient;
        Doctor  doctor  = (Doctor)  rawDoctor;

        // 2. Create appointment
        String id = store.nextId("APT");
        Appointment appt = new Appointment(id, patientId, doctorId, date, time, symptoms);

        // 3. Persist
        store.addAppointment(appt);
        patient.addAppointmentId(id);
        doctor.addAppointmentId(id);

        // 4. Auto-confirm (simulating instant confirmation)
        appt.setStatus(AppointmentStatus.CONFIRMED);

        // 5. Notify all registered observers
        notifier.notifyBooked(appt);

        return appt;
    }

    // ────────────────────────────────────────────────────────
    //  Cancel an appointment
    // ────────────────────────────────────────────────────────
    public boolean cancelAppointment(String appointmentId, String requestingUserId) {
        Appointment appt = store.getAppointmentById(appointmentId);
        if (appt == null) return false;

        // Permission check: only the patient or admin may cancel
        User requester = store.getUserById(requestingUserId);
        boolean isPatientOwner = appt.getPatientId().equals(requestingUserId);
        boolean isAdmin        = requester instanceof Admin;
        if (!isPatientOwner && !isAdmin) return false;

        if (appt.getStatus() == AppointmentStatus.CANCELLED ||
            appt.getStatus() == AppointmentStatus.COMPLETED)  return false;

        appt.setStatus(AppointmentStatus.CANCELLED);
        notifier.notifyCancelled(appt);
        return true;
    }

    // ────────────────────────────────────────────────────────
    //  Reschedule an appointment
    // ────────────────────────────────────────────────────────
    public boolean rescheduleAppointment(String appointmentId, LocalDate newDate,
                                         LocalTime newTime, String requestingUserId) {
        Appointment appt = store.getAppointmentById(appointmentId);
        if (appt == null) return false;

        boolean isPatientOwner = appt.getPatientId().equals(requestingUserId);
        User requester = store.getUserById(requestingUserId);
        if (!isPatientOwner && !(requester instanceof Admin)) return false;

        if (appt.getStatus() == AppointmentStatus.CANCELLED ||
            appt.getStatus() == AppointmentStatus.COMPLETED)  return false;

        appt.setDate(newDate);
        appt.setTime(newTime);
        appt.setStatus(AppointmentStatus.RESCHEDULED);
        notifier.notifyRescheduled(appt);
        return true;
    }

    // ────────────────────────────────────────────────────────
    //  Confirm an appointment (Doctor or Admin)
    // ────────────────────────────────────────────────────────
    public boolean confirmAppointment(String appointmentId, String doctorId) {
        Appointment appt = store.getAppointmentById(appointmentId);
        if (appt == null || !appt.getDoctorId().equals(doctorId)) return false;
        appt.setStatus(AppointmentStatus.CONFIRMED);
        notifier.notifyStatusChanged(appt);
        return true;
    }

    // ────────────────────────────────────────────────────────
    //  Complete an appointment (Doctor)
    // ────────────────────────────────────────────────────────
    public boolean completeAppointment(String appointmentId, String doctorId, String notes) {
        Appointment appt = store.getAppointmentById(appointmentId);
        if (appt == null || !appt.getDoctorId().equals(doctorId)) return false;
        appt.setStatus(AppointmentStatus.COMPLETED);
        appt.setDoctorNotes(notes);
        notifier.notifyStatusChanged(appt);
        return true;
    }

    // ────────────────────────────────────────────────────────
    //  Convenience queries (delegating to DataStore)
    // ────────────────────────────────────────────────────────
    public List<Appointment> getPatientAppointments(String patientId) {
        return store.getAppointmentsForPatient(patientId);
    }

    public List<Appointment> getDoctorAppointments(String doctorId) {
        return store.getAppointmentsForDoctor(doctorId);
    }

    public List<Appointment> getAllAppointments() {
        return (List<Appointment>) store.getAllAppointments();
    }

    public List<Doctor> searchDoctors(String keyword, Specialization spec) {
        return store.searchDoctors(keyword, spec);
    }
}
