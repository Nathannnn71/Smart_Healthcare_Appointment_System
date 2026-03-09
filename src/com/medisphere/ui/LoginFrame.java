package com.medisphere.ui;

import com.medisphere.model.*;
import com.medisphere.pattern.HealthcareFacade;
import com.medisphere.ui.components.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginFrame extends JFrame {

    private StyledTextField emailField;
    private StyledPasswordField passwordField;
    private JLabel statusLabel;
    private final HealthcareFacade facade = HealthcareFacade.getInstance();

    public LoginFrame() {
        setTitle("MediSphere – Smart Healthcare Appointment System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 620);
        setMinimumSize(new Dimension(880, 580));
        setLocationRelativeTo(null);
        setResizable(true);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(UITheme.BG_PAGE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(root);

        // Left panel (hero / branding)
        root.add(buildHeroPanel(), BorderLayout.WEST);
        // Right panel (login form)
        root.add(buildFormPanel(), BorderLayout.CENTER);
    }

    private JPanel buildHeroPanel() {
        JPanel hero = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0D47A1), getWidth(), getHeight(), new Color(0x006064));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(-60, -60, 280, 280);
                g2.fillOval(getWidth() - 120, getHeight() - 120, 240, 240);
                g2.setColor(new Color(255, 255, 255, 10));
                g2.fillOval(50, getHeight() / 2, 200, 200);

                // Medical cross
                int cx = getWidth() / 2, cy = 180, arm = 40, thick = 16;
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillRoundRect(cx - thick/2, cy - arm, thick, arm * 2, 6, 6);
                g2.fillRoundRect(cx - arm, cy - thick/2, arm * 2, thick, 6, 6);
            }
        };
        hero.setPreferredSize(new Dimension(420, 0));

        // Content overlaid on the painted panel
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        JLabel logoLbl = new JLabel("⚕");
        logoLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        logoLbl.setForeground(Color.WHITE);
        logoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLbl = new JLabel("MediSphere");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLbl = new JLabel("<html><center>Smart Healthcare<br>Appointment System</center></html>");
        subtitleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLbl.setForeground(new Color(255, 255, 255, 200));
        subtitleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(Box.createVerticalGlue());
        content.add(logoLbl);
        content.add(Box.createVerticalStrut(10));
        content.add(titleLbl);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitleLbl);
        content.add(Box.createVerticalStrut(50));

        // Feature bullets
        String[] features = {"📅  Easy Appointment Booking", "🔍  Find Specialists Instantly", "🔔  Smart Reminders & Alerts", "👨‍⚕  Trusted Medical Network"};
        for (String f : features) {
            JLabel fl = new JLabel(f);
            fl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fl.setForeground(new Color(255, 255, 255, 185));
            fl.setAlignmentX(Component.LEFT_ALIGNMENT);
            fl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            content.add(fl);
        }
        content.add(Box.createVerticalGlue());

        hero.setLayout(new BorderLayout());
        hero.add(content, BorderLayout.CENTER);
        return hero;
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(UITheme.BG_PAGE);

        RoundedPanel card = new RoundedPanel(UITheme.CARD_RADIUS, Color.WHITE, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 48, 40, 48));
        card.setPreferredSize(new Dimension(400, 480));

        JLabel heading = new JLabel("Welcome Back");
        heading.setFont(UITheme.FONT_TITLE);
        heading.setForeground(UITheme.TEXT_DARK);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subheading = new JLabel("Sign in to your MediSphere account");
        subheading.setFont(UITheme.FONT_BODY);
        subheading.setForeground(UITheme.TEXT_LIGHT);
        subheading.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Form fields
        JLabel emailLbl = new JLabel("Email Address");
        emailLbl.setFont(UITheme.FONT_LABEL);
        emailLbl.setForeground(UITheme.TEXT_MED);
        emailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = new StyledTextField("you@example.com", 20);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(UITheme.FONT_LABEL);
        passLbl.setForeground(UITheme.TEXT_MED);
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new StyledPasswordField("Enter your password", 20);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.ERROR_LIGHT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Login button
        RoundedButton loginBtn = new RoundedButton("Sign In");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        loginBtn.addActionListener(e -> doLogin());
        passwordField.addActionListener(e -> doLogin());

        // Register link
        JPanel regRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        regRow.setOpaque(false);
        regRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel noAcc = new JLabel("Don't have an account? ");
        noAcc.setFont(UITheme.FONT_BODY);
        noAcc.setForeground(UITheme.TEXT_MED);
        JLabel regLink = new JLabel("Register here");
        regLink.setFont(UITheme.FONT_BODY);
        regLink.setForeground(UITheme.PRIMARY);
        regLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { openRegister(); }
            @Override public void mouseEntered(MouseEvent e) { regLink.setText("<html><u>Register here</u></html>"); }
            @Override public void mouseExited(MouseEvent e)  { regLink.setText("Register here"); }
        });
        regRow.add(noAcc); regRow.add(regLink);

        // Demo credentials hint
        RoundedPanel demoBox = new RoundedPanel(8, new Color(0xEBF5FB), false);
        demoBox.setLayout(new BoxLayout(demoBox, BoxLayout.Y_AXIS));
        demoBox.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        demoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        JLabel demoTitle = new JLabel("Demo Accounts");
        demoTitle.setFont(UITheme.FONT_BOLD);
        demoTitle.setForeground(UITheme.PRIMARY);
        String[] demos = {"patient@demo.com / demo123  (Patient)", "doctor@demo.com / demo123  (Doctor)", "admin@demo.com / demo123  (Admin)"};
        demoBox.add(demoTitle);
        for (String d : demos) {
            JLabel dl = new JLabel(d);
            dl.setFont(UITheme.FONT_SMALL);
            dl.setForeground(UITheme.TEXT_MED);
            demoBox.add(dl);
        }

        card.add(heading);
        card.add(Box.createVerticalStrut(4));
        card.add(subheading);
        card.add(Box.createVerticalStrut(28));
        card.add(emailLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(16));
        card.add(passLbl);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(6));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(16));
        card.add(regRow);
        card.add(Box.createVerticalStrut(20));
        card.add(demoBox);

        outer.add(card);
        return outer;
    }

    private void doLogin() {
        String email = emailField.getText().trim();
        String pass  = new String(passwordField.getPassword());
        if (email.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Please enter your email and password.");
            return;
        }
        User user = facade.login(email, pass);
        if (user == null) {
            statusLabel.setText("Invalid email or password. Please try again.");
            passwordField.setText("");
            return;
        }
        statusLabel.setText(" ");
        openDashboard(user);
    }

    private void openDashboard(User user) {
        dispose();
        SwingUtilities.invokeLater(() -> {
            JFrame dash;
            if (user instanceof com.medisphere.model.Patient)
                dash = new PatientDashboard((com.medisphere.model.Patient) user);
            else if (user instanceof com.medisphere.model.Doctor)
                dash = new DoctorDashboard((com.medisphere.model.Doctor) user);
            else
                dash = new AdminDashboard((com.medisphere.model.Administrator) user);
            dash.setVisible(true);
        });
    }

    private void openRegister() {
        dispose();
        new RegisterFrame(this).setVisible(true);
    }
}
