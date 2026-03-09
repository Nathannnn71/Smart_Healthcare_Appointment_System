package com.medisphere.pattern;

import com.medisphere.model.*;

/**
 * =========================================================
 * DESIGN PATTERN 1: FACTORY METHOD (Creational)
 * =========================================================
 * Purpose: Encapsulates the creation of different User types
 * (Patient, Doctor, Administrator) without exposing the
 * instantiation logic to the client.
 *
 * Participants:
 *  - UserFactory       : Creator (provides factory methods)
 *  - User              : Abstract Product
 *  - Patient/Doctor/Administrator : Concrete Products
 * =========================================================
 */
public class UserFactory {

    // Factory method for creating Patient
    public static Patient createPatient(String name, String email, String password,
                                         String phone, int age, String bloodType) {
        Patient p = new Patient(name, email, password, phone, age, bloodType);
        System.out.println("[Factory] Created Patient: " + p.getName());
        return p;
    }

    // Factory method for creating Doctor
    public static Doctor createDoctor(String name, String email, String password,
                                       String phone, Specialty specialty,
                                       String qualification, int experience,
                                       double rating, String fee, String bio,
                                       String hospital, String hours) {
        Doctor d = new Doctor(name, email, password, phone, specialty,
                qualification, experience, rating, fee, bio, hospital, hours);
        System.out.println("[Factory] Created Doctor: Dr. " + d.getName()
                + " (" + specialty.getDisplayName() + ")");
        return d;
    }

    // Factory method for creating Administrator
    public static Administrator createAdministrator(String name, String email,
                                                     String password, String phone,
                                                     String adminLevel, String department) {
        Administrator a = new Administrator(name, email, password, phone, adminLevel, department);
        System.out.println("[Factory] Created Administrator: " + a.getName());
        return a;
    }

    // Generic factory method based on role string
    public static User createUser(String role, String name, String email,
                                   String password, String phone) {
        switch (role.toUpperCase()) {
            case "PATIENT":
                return createPatient(name, email, password, phone, 25, "O+");
            case "DOCTOR":
                return createDoctor(name, email, password, phone, Specialty.GENERAL_PRACTICE,
                        "MBBS", 5, 4.0, "RM 150", "General practitioner", "Hospital KL", "9AM-5PM");
            case "ADMIN":
                return createAdministrator(name, email, password, phone, "Level 1", "Operations");
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
