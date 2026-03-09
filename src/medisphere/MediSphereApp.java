package medisphere;

import medisphere.ui.MainFrame;

import javax.swing.*;

/**
 * ============================================================
 *  MediSphere – Smart Healthcare Appointment System
 * ============================================================
 *
 *  Design Patterns Applied:
 *  ────────────────────────────────────────────────────────────
 *  1. SINGLETON   (Creational)  — DataStore
 *     Single in-memory data store shared across the entire app.
 *
 *  2. FACTORY METHOD (Creational) — UserFactory
 *     Creates Patient / Doctor / Admin without exposing new().
 *
 *  3. OBSERVER   (Behavioral)  — NotificationService
 *     Appointment events broadcast to Email & System observers.
 *
 *  4. STRATEGY   (Behavioral)  — RecommendationContext
 *     Swappable recommendation algorithms (Symptom / History).
 *
 *  5. FACADE     (Structural)  — AppointmentFacade
 *     Unified API over booking, cancelling, rescheduling logic.
 *  ────────────────────────────────────────────────────────────
 *
 *  OOP Pillars:
 *  • Encapsulation  — private fields with controlled getters/setters
 *  • Inheritance    — Patient, Doctor, Admin extend abstract User
 *  • Polymorphism   — getRole(), getRoleColor() overridden per type
 *  • Abstraction    — User abstract class + Strategy/Observer interfaces
 *  ────────────────────────────────────────────────────────────
 */
public class MediSphereApp {

    public static void main(String[] args) {
        // Apply a modern Nimbus look-and-feel if available
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            // Override Nimbus defaults for a cleaner look
            UIManager.put("nimbusBase",           new java.awt.Color(0x1A73E8));
            UIManager.put("nimbusBlueGrey",       new java.awt.Color(0xF4F7FF));
            UIManager.put("control",              new java.awt.Color(0xF4F7FF));
            UIManager.put("text",                 new java.awt.Color(0x1A2B4A));
            UIManager.put("ScrollBar.thumbHighlight", new java.awt.Color(0xBBD0FF));
        } catch (Exception e) {
            // Fall back to default LaF
        }

        // Ensure UI runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║  MediSphere Healthcare System v1.0   ║");
            System.out.println("║  Design Patterns: Singleton, Factory,║");
            System.out.println("║  Observer, Strategy, Facade          ║");
            System.out.println("╚══════════════════════════════════════╝");

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
