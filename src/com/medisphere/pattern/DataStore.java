package com.medisphere.pattern;

import com.medisphere.model.*;
import java.util.*;

/**
 * =========================================================
 * DESIGN PATTERN 4: SINGLETON (Creational)
 * =========================================================
 * Purpose: Ensures a single shared data store exists across
 * the entire application, preventing duplicate data sources
 * and providing a global access point.
 * =========================================================
 */
public class DataStore {
    private static DataStore instance;

    private final List<User> users = new ArrayList<>();
    private final List<Doctor> doctors = new ArrayList<>();
    private final List<Patient> patients = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();

    // Private constructor prevents external instantiation
    private DataStore() {}

    // Thread-safe double-checked locking
    public static synchronized DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    // ---------- User Operations ----------
    public void addUser(User user) {
        users.add(user);
        if (user instanceof Doctor)  doctors.add((Doctor) user);
        if (user instanceof Patient) patients.add((Patient) user);
    }

    public User findUserByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    public User findUserById(String id) {
        return users.stream().filter(u -> u.getUserId().equals(id))
                .findFirst().orElse(null);
    }

    public List<User>       getAllUsers()    { return Collections.unmodifiableList(users); }
    public List<Doctor>     getAllDoctors()  { return Collections.unmodifiableList(doctors); }
    public List<Patient>    getAllPatients() { return Collections.unmodifiableList(patients); }

    // ---------- Appointment Operations ----------
    public void addAppointment(Appointment a) { appointments.add(a); }

    public void removeAppointment(String id) {
        appointments.removeIf(a -> a.getAppointmentId().equals(id));
    }

    public Appointment findAppointmentById(String id) {
        return appointments.stream().filter(a -> a.getAppointmentId().equals(id))
                .findFirst().orElse(null);
    }

    public List<Appointment> getAppointmentsForPatient(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments)
            if (a.getPatientId().equals(patientId)) result.add(a);
        result.sort(Comparator.comparing(Appointment::getDate).reversed());
        return result;
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments)
            if (a.getDoctorId().equals(doctorId)) result.add(a);
        result.sort(Comparator.comparing(Appointment::getDate));
        return result;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> sorted = new ArrayList<>(appointments);
        sorted.sort(Comparator.comparing(Appointment::getDate).reversed());
        return sorted;
    }

    public List<Doctor> searchDoctors(String specialtyFilter, String nameFilter) {
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : doctors) {
            boolean matchSpec = specialtyFilter == null || specialtyFilter.isEmpty()
                    || d.getSpecialty().getDisplayName().equalsIgnoreCase(specialtyFilter);
            boolean matchName = nameFilter == null || nameFilter.isEmpty()
                    || d.getName().toLowerCase().contains(nameFilter.toLowerCase());
            if (matchSpec && matchName) result.add(d);
        }
        return result;
    }

    public int getTotalAppointmentsCount()   { return appointments.size(); }
    public int getPendingAppointmentsCount() {
        return (int) appointments.stream().filter(a -> a.getStatus() == AppointmentStatus.PENDING ||
                a.getStatus() == AppointmentStatus.CONFIRMED).count();
    }
}
