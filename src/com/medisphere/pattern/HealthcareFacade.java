package com.medisphere.pattern;

import com.medisphere.model.*;
import java.time.*;
import java.util.List;

/**
 * =========================================================
 * DESIGN PATTERN 5: FACADE (Structural)
 * =========================================================
 * Purpose: Provides a simplified, unified interface to the
 * complex subsystem of data management, appointment handling,
 * user authentication, and recommendation logic.
 *
 * Client code (UI layers) interacts only with this Facade,
 * keeping the subsystem implementation hidden.
 * =========================================================
 */
public class HealthcareFacade {
    private static HealthcareFacade instance;

    private final DataStore         dataStore   = DataStore.getInstance();
    private final AppointmentManager aptManager = AppointmentManager.getInstance();
    private RecommendationStrategy  recStrategy = new SymptomBasedRecommendation();

    private HealthcareFacade() {}

    public static synchronized HealthcareFacade getInstance() {
        if (instance == null) instance = new HealthcareFacade();
        return instance;
    }

    // ---- Authentication ----
    public User login(String email, String password) {
        User user = dataStore.findUserByEmail(email);
        if (user != null && user.authenticate(password)) {
            System.out.println("[Facade] Login success: " + user.getName());
            return user;
        }
        System.out.println("[Facade] Login failed for: " + email);
        return null;
    }

    public Patient registerPatient(String name, String email, String password,
                                    String phone, int age, String bloodType) {
        if (dataStore.findUserByEmail(email) != null) return null; // duplicate
        Patient p = UserFactory.createPatient(name, email, password, phone, age, bloodType);
        dataStore.addUser(p);
        return p;
    }

    // ---- Doctor Search ----
    public List<Doctor> searchDoctors(String specialty, String name) {
        return dataStore.searchDoctors(specialty, name);
    }

    public List<Doctor> getAllDoctors() {
        return dataStore.getAllDoctors();
    }

    // ---- Appointments ----
    public Appointment bookAppointment(String patientId, String doctorId,
                                        LocalDate date, LocalTime time, String reason) {
        return aptManager.bookAppointment(patientId, doctorId, date, time, reason);
    }

    public boolean cancelAppointment(String appointmentId) {
        return aptManager.cancelAppointment(appointmentId);
    }

    public boolean rescheduleAppointment(String id, LocalDate date, LocalTime time) {
        return aptManager.rescheduleAppointment(id, date, time);
    }

    public List<Appointment> getPatientAppointments(String patientId) {
        return dataStore.getAppointmentsForPatient(patientId);
    }

    public List<Appointment> getDoctorAppointments(String doctorId) {
        return dataStore.getAppointmentsForDoctor(doctorId);
    }

    public List<Appointment> getAllAppointments() {
        return dataStore.getAllAppointments();
    }

    public List<TimeSlot> getAvailableSlots(String doctorId, LocalDate date) {
        return aptManager.getAvailableSlots(doctorId, date);
    }

    // ---- Recommendations (Strategy Pattern) ----
    public void setRecommendationStrategy(RecommendationStrategy strategy) {
        this.recStrategy = strategy;
        System.out.println("[Facade] Recommendation strategy set to: " + strategy.getStrategyName());
    }

    public List<Doctor> getRecommendations(Patient patient) {
        return recStrategy.recommend(patient, dataStore.getAllDoctors());
    }

    public RecommendationStrategy getCurrentStrategy() { return recStrategy; }

    // ---- Stats (for Admin) ----
    public int getTotalDoctors()       { return dataStore.getAllDoctors().size(); }
    public int getTotalPatients()      { return dataStore.getAllPatients().size(); }
    public int getTotalAppointments()  { return dataStore.getTotalAppointmentsCount(); }
    public int getPendingAppointments(){ return dataStore.getPendingAppointmentsCount(); }

    public DataStore getDataStore()           { return dataStore; }
    public AppointmentManager getAptManager() { return aptManager; }
}
