package com.medisphere;

import com.medisphere.model.*;
import com.medisphere.pattern.*;
import com.medisphere.ui.LoginFrame;
import javax.swing.*;
import java.time.*;
import java.awt.*;

/**
 * ╔══════════════════════════════════════════════════════════════════════════╗
 * ║         MediSphere – Smart Healthcare Appointment System                ║
 * ╠══════════════════════════════════════════════════════════════════════════╣
 * ║  Design Patterns:                                                        ║
 * ║  1. Factory Method (Creational)  – UserFactory                          ║
 * ║  2. Singleton      (Creational)  – DataStore, AppointmentManager        ║
 * ║  3. Observer       (Behavioral)  – AppointmentSubject + Observers       ║
 * ║  4. Strategy       (Behavioral)  – RecommendationStrategy               ║
 * ║  5. Facade         (Structural)  – HealthcareFacade                     ║
 * ╚══════════════════════════════════════════════════════════════════════════╝
 */
public class Main {

    public static void main(String[] args) {
        // ── System Look and Feel ─────────────────────────────────────────────
        try {
            // Try to use Nimbus for a more modern look
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("Panel.background", new Color(0xF0F4F8));
            UIManager.put("ScrollPane.background", new Color(0xF0F4F8));
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 12));
        } catch (Exception e) {
            System.out.println("[Main] Using default look and feel.");
        }

        // ── Seed sample data ─────────────────────────────────────────────────
        seedData();

        // ── Launch UI ─────────────────────────────────────────────────────────
        SwingUtilities.invokeLater(() -> {
            System.out.println("\n=== MediSphere Starting ===");
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }

    /** Populates the system with demo doctors, patients, admin, and appointments. */
    private static void seedData() {
        DataStore ds = DataStore.getInstance();
        AppointmentManager am = AppointmentManager.getInstance();

        System.out.println("\n[Main] Seeding sample data...");

        // ── ADMIN ──────────────────────────────────────────────────────────────
        Administrator admin = UserFactory.createAdministrator(
                "Sarah Lee", "admin@demo.com", "demo123", "+60 3-1234 5678",
                "Level 1", "Operations");
        ds.addUser(admin);

        // ── DOCTORS ───────────────────────────────────────────────────────────
        Doctor[] doctors = {
            UserFactory.createDoctor(
                "James Tan", "doctor@demo.com", "demo123", "+60 12-345 6789",
                Specialty.CARDIOLOGY, "MBBS, MRCP, MD (Cardiology)", 15, 4.8,
                "RM 300", "Consultant cardiologist specializing in heart failure and coronary artery disease.",
                "Pantai Hospital KL", "Mon-Fri: 9AM - 5PM"),
            UserFactory.createDoctor(
                "Priya Nair", "priya@demo.com", "demo123", "+60 12-888 7766",
                Specialty.NEUROLOGY, "MBBS, MD (Neurology)", 10, 4.7,
                "RM 280", "Specialist in neurological disorders including epilepsy and migraine management.",
                "Sunway Medical Centre", "Mon, Wed, Fri: 9AM - 4PM"),
            UserFactory.createDoctor(
                "Ahmad Razif", "ahmad@demo.com", "demo123", "+60 11-222 3344",
                Specialty.ORTHOPEDICS, "MBBS, MS (Orthopedics)", 12, 4.6,
                "RM 250", "Expert in joint replacement and sports injury rehabilitation.",
                "KPJ Ampang Puteri", "Tue, Thu: 8AM - 3PM"),
            UserFactory.createDoctor(
                "Mei Lin Chong", "meilin@demo.com", "demo123", "+60 12-567 8901",
                Specialty.DERMATOLOGY, "MBBS, MMed (Dermatology)", 8, 4.9,
                "RM 220", "Specializes in acne, eczema, psoriasis, and aesthetic dermatology.",
                "Gleneagles Hospital PJ", "Mon-Fri: 10AM - 6PM"),
            UserFactory.createDoctor(
                "Rajan Pillai", "rajan@demo.com", "demo123", "+60 16-123 4567",
                Specialty.GENERAL_PRACTICE, "MBBS, FRACGP", 6, 4.5,
                "RM 80", "General practitioner providing comprehensive primary healthcare services.",
                "Klinik Fajar Baru", "Mon-Sat: 8AM - 8PM"),
            UserFactory.createDoctor(
                "Nurul Hana", "nurul@demo.com", "demo123", "+60 13-456 7890",
                Specialty.PEDIATRICS, "MBBS, MD (Pediatrics)", 9, 4.7,
                "RM 160", "Caring pediatrician dedicated to the health and wellbeing of children 0-18 years.",
                "Hospital Universiti Malaya", "Mon-Fri: 9AM - 5PM"),
            UserFactory.createDoctor(
                "David Lim", "david@demo.com", "demo123", "+60 17-678 9012",
                Specialty.OPHTHALMOLOGY, "MBBS, MMed (Ophthalmology)", 11, 4.6,
                "RM 200", "Eye specialist for cataract, glaucoma, and refractive surgeries.",
                "Tun Hussein Onn Eye Hospital", "Tue-Sat: 9AM - 4PM"),
            UserFactory.createDoctor(
                "Kavitha Raj", "kavitha@demo.com", "demo123", "+60 18-789 0123",
                Specialty.PSYCHIATRY, "MBBS, MMed (Psychiatry)", 7, 4.5,
                "RM 250", "Mental health specialist providing compassionate care for anxiety, depression, and mood disorders.",
                "Hospital Bahagia Ulu Kinta", "Mon, Wed, Fri: 10AM - 5PM")
        };

        for (Doctor d : doctors) ds.addUser(d);

        // ── PATIENTS ──────────────────────────────────────────────────────────
        Patient patient1 = UserFactory.createPatient(
                "Ali Hassan", "patient@demo.com", "demo123", "+60 12-123 4567", 32, "O+");
        patient1.addSymptom("chest pain");
        patient1.addSymptom("headache");
        ds.addUser(patient1);

        Patient patient2 = UserFactory.createPatient(
                "Sarah Wong", "sarah@demo.com", "demo123", "+60 11-999 8888", 27, "A+");
        patient2.addSymptom("skin rash");
        ds.addUser(patient2);

        Patient patient3 = UserFactory.createPatient(
                "Raj Kumar", "raj@demo.com", "demo123", "+60 16-777 6655", 45, "B+");
        patient3.addSymptom("joint pain");
        ds.addUser(patient3);

        // ── SAMPLE APPOINTMENTS ───────────────────────────────────────────────
        String docId0 = doctors[0].getUserId(); // Cardiologist
        String docId1 = doctors[1].getUserId(); // Neurologist
        String docId4 = doctors[4].getUserId(); // GP
        String p1Id   = patient1.getUserId();
        String p2Id   = patient2.getUserId();
        String p3Id   = patient3.getUserId();

        // Future appointments
        am.bookAppointment(p1Id, docId0, LocalDate.now().plusDays(3), LocalTime.of(10, 0), "Chest pain follow-up");
        am.bookAppointment(p1Id, docId1, LocalDate.now().plusDays(7), LocalTime.of(14, 30), "Recurring headaches");
        am.bookAppointment(p2Id, doctors[3].getUserId(), LocalDate.now().plusDays(2), LocalTime.of(11, 0), "Skin rash evaluation");
        am.bookAppointment(p3Id, doctors[2].getUserId(), LocalDate.now().plusDays(5), LocalTime.of(9, 30), "Knee joint pain");

        // Past appointments (mark as completed)
        Appointment past1 = am.bookAppointment(p1Id, docId4, LocalDate.now().minusDays(10), LocalTime.of(9, 0), "General checkup");
        if (past1 != null) { past1.setStatus(AppointmentStatus.COMPLETED); patient1.addPastDoctorId(docId4); }

        Appointment past2 = am.bookAppointment(p2Id, docId4, LocalDate.now().minusDays(5), LocalTime.of(15, 0), "Fever and cold");
        if (past2 != null) { past2.setStatus(AppointmentStatus.COMPLETED); patient2.addPastDoctorId(docId4); }

        System.out.println("[Main] Data seeding complete!");
        System.out.println("[Main] Doctors: " + ds.getAllDoctors().size());
        System.out.println("[Main] Patients: " + ds.getAllPatients().size());
        System.out.println("[Main] Appointments: " + ds.getAllAppointments().size());
        System.out.println("\nDemo Login Credentials:");
        System.out.println("  Patient  → patient@demo.com / demo123");
        System.out.println("  Doctor   → doctor@demo.com  / demo123");
        System.out.println("  Admin    → admin@demo.com   / demo123");
        System.out.println("===========================================\n");
    }
}
