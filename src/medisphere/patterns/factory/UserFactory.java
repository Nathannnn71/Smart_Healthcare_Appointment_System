package medisphere.patterns.factory;

import medisphere.data.DataStore;
import medisphere.models.*;

/**
 * ============================================================
 * DESIGN PATTERN 2 — FACTORY METHOD (Creational)
 * ============================================================
 * Centralises the creation of User objects (Patient, Doctor,
 * Admin).  Callers request a user by type without knowing the
 * concrete class details, promoting the Open/Closed Principle.
 */
public class UserFactory {

    public enum UserType { PATIENT, DOCTOR, ADMIN }

    /**
     * Creates and registers a Patient.
     * @param name        Full name
     * @param email       Login email
     * @param password    Hashed/plain password
     * @param phone       Contact number
     * @param dateOfBirth dd-MM-yyyy
     * @param bloodType   A+, O-, etc.
     * @return new Patient (already added to DataStore)
     */
    public static Patient createPatient(String name, String email, String password,
                                        String phone, String dateOfBirth, String bloodType) {
        DataStore store = DataStore.getInstance();
        String id = store.nextId("P");
        Patient patient = new Patient(id, name, email, password, phone, dateOfBirth, bloodType);
        store.addUser(patient);
        return patient;
    }

    /**
     * Creates and registers a Doctor.
     */
    public static Doctor createDoctor(String name, String email, String password,
                                      String phone, Specialization specialization,
                                      String qualification, int experienceYears,
                                      double consultationFee) {
        DataStore store = DataStore.getInstance();
        String id = store.nextId("D");
        Doctor doctor = new Doctor(id, name, email, password, phone,
                                   specialization, qualification, experienceYears, consultationFee);
        store.addUser(doctor);
        return doctor;
    }

    /**
     * Creates and registers an Admin.
     */
    public static Admin createAdmin(String name, String email, String password,
                                    String phone, String department) {
        DataStore store = DataStore.getInstance();
        String id = store.nextId("A");
        Admin admin = new Admin(id, name, email, password, phone, department, "ADMIN");
        store.addUser(admin);
        return admin;
    }

    /**
     * Convenience factory method — determines type from a string role.
     * Used during registration when user selects their role.
     */
    public static User createUser(String userType, Object... params) {
        if (userType.equals("Patient")) {
            return createPatient(
                (String) params[0], (String) params[1], (String) params[2],
                (String) params[3], (String) params[4], (String) params[5]);
        } else if (userType.equals("Doctor")) {
            return createDoctor(
                (String) params[0], (String) params[1], (String) params[2],
                (String) params[3], (Specialization) params[4],
                (String) params[5], (Integer) params[6], (Double) params[7]);
        } else if (userType.equals("Admin")) {
            return createAdmin(
                (String) params[0], (String) params[1], (String) params[2],
                (String) params[3], (String) params[4]);
        } else {
            throw new IllegalArgumentException(
                "Unknown userType: " + userType +
                ". Valid values are: Patient, Doctor, Admin");
        }
    }
}
