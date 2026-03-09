package medisphere.ui;

import medisphere.models.User;
import medisphere.patterns.facade.AppointmentFacade;
import medisphere.patterns.observer.EmailNotification;
import medisphere.patterns.observer.NotificationService;
import medisphere.patterns.observer.SystemNotification;

import javax.swing.*;
import java.awt.*;

/**
 * Top-level JFrame — hosts a CardLayout that switches between
 * Login, Register, and the three role dashboards.
 */
public class MainFrame extends JFrame {

    public static final String CARD_LOGIN    = "LOGIN";
    public static final String CARD_REGISTER = "REGISTER";
    public static final String CARD_PATIENT  = "PATIENT";
    public static final String CARD_DOCTOR   = "DOCTOR";
    public static final String CARD_ADMIN    = "ADMIN";

    private final CardLayout     cardLayout  = new CardLayout();
    private final JPanel         cardPanel   = new JPanel(cardLayout);

    // Shared application services
    private final NotificationService notifier;
    private final AppointmentFacade   facade;

    // Panels (lazy-initialised on login)
    private PatientDashboard  patientDashboard;
    private DoctorDashboard   doctorDashboard;
    private AdminDashboard    adminDashboard;

    public MainFrame() {
        // ── Wire up Observer pattern ─────────────────────────
        notifier = new NotificationService();
        notifier.registerObserver(new SystemNotification());
        notifier.registerObserver(new EmailNotification());

        // ── Wire up Facade (Structural) ──────────────────────
        facade = new AppointmentFacade(notifier);

        // ── Frame configuration ──────────────────────────────
        setTitle("MediSphere – Smart Healthcare Appointment System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 780));
        setPreferredSize(new Dimension(1440, 860));
        setLocationRelativeTo(null);

        // ── Build login / register panels ────────────────────
        LoginPanel    loginPanel    = new LoginPanel(this);
        RegisterPanel registerPanel = new RegisterPanel(this);

        cardPanel.add(loginPanel,    CARD_LOGIN);
        cardPanel.add(registerPanel, CARD_REGISTER);

        add(cardPanel);
        pack();
        setLocationRelativeTo(null);
        cardLayout.show(cardPanel, CARD_LOGIN);
    }

    /** Called after successful authentication. Builds and shows the correct dashboard. */
    public void loginSuccess(User user) {
        switch (user.getRole()) {
            case "Patient":
                patientDashboard = new PatientDashboard(this, user, facade);
                cardPanel.add(patientDashboard, CARD_PATIENT);
                cardLayout.show(cardPanel, CARD_PATIENT);
                break;
            case "Doctor":
                doctorDashboard = new DoctorDashboard(this, user, facade);
                cardPanel.add(doctorDashboard, CARD_DOCTOR);
                cardLayout.show(cardPanel, CARD_DOCTOR);
                break;
            case "Admin":
                adminDashboard = new AdminDashboard(this, user, facade);
                cardPanel.add(adminDashboard, CARD_ADMIN);
                cardLayout.show(cardPanel, CARD_ADMIN);
                break;
        }
    }

    /** Navigate to a named card */
    public void showCard(String card) {
        cardLayout.show(cardPanel, card);
    }

    /** Logout — remove dashboards and return to login */
    public void logout() {
        if (patientDashboard != null) { cardPanel.remove(patientDashboard); patientDashboard = null; }
        if (doctorDashboard  != null) { cardPanel.remove(doctorDashboard);  doctorDashboard  = null; }
        if (adminDashboard   != null) { cardPanel.remove(adminDashboard);   adminDashboard   = null; }
        cardLayout.show(cardPanel, CARD_LOGIN);
    }

    public AppointmentFacade getFacade()   { return facade; }
    public NotificationService getNotifier(){ return notifier; }
}
