package com.medisphere.ui;

import com.medisphere.model.Patient;
import com.medisphere.pattern.HealthcareFacade;
import com.medisphere.ui.components.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterFrame extends JFrame {
    private StyledTextField nameField, emailField, phoneField, ageField;
    private StyledPasswordField passField, confirmPassField;
    private JComboBox<String> bloodTypeBox;
    private JLabel statusLabel;
    private final HealthcareFacade facade = HealthcareFacade.getInstance();
    private final LoginFrame loginFrame;

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setTitle("MediSphere – Create Account");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 680);
        setMinimumSize(new Dimension(500, 600));
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UITheme.BG_PAGE);
        setContentPane(root);

        RoundedPanel card = new RoundedPanel(UITheme.CARD_RADIUS, Color.WHITE, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(36, 44, 36, 44));
        card.setPreferredSize(new Dimension(460, 580));

        // Header
        JLabel icon = new JLabel("⚕");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        icon.setForeground(UITheme.PRIMARY);
        icon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Create Your Account");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_DARK);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Join MediSphere as a Patient");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_LIGHT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Two-column row helper
        JPanel nameEmailRow = twoColRow(
                labeledField("Full Name", nameField = new StyledTextField("John Doe", 15)),
                labeledField("Email Address", emailField = new StyledTextField("john@email.com", 15))
        );
        nameEmailRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameEmailRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        JPanel phoneAgeRow = twoColRow(
                labeledField("Phone Number", phoneField = new StyledTextField("+60 12-345 6789", 12)),
                labeledField("Age", ageField = new StyledTextField("25", 5))
        );
        phoneAgeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        phoneAgeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        // Blood type
        bloodTypeBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodTypeBox.setFont(UITheme.FONT_INPUT);
        bloodTypeBox.setBackground(Color.WHITE);
        JPanel bloodRow = labeledComponent("Blood Type", bloodTypeBox);
        bloodRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        bloodRow.setMaximumSize(new Dimension(200, 72));

        // Passwords
        passField = new StyledPasswordField("Minimum 6 characters", 20);
        confirmPassField = new StyledPasswordField("Re-enter password", 20);
        JPanel passRow = twoColRow(
                labeledField("Password", passField),
                labeledField("Confirm Password", confirmPassField)
        );
        passRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        passRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(UITheme.FONT_SMALL);
        statusLabel.setForeground(UITheme.ERROR_LIGHT);
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton regBtn = new RoundedButton("Create Account");
        regBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        regBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        regBtn.addActionListener(e -> doRegister());

        JPanel loginRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        loginRow.setOpaque(false);
        loginRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel already = new JLabel("Already have an account? ");
        already.setFont(UITheme.FONT_BODY);
        already.setForeground(UITheme.TEXT_MED);
        JLabel loginLink = new JLabel("Sign in");
        loginLink.setFont(UITheme.FONT_BODY);
        loginLink.setForeground(UITheme.PRIMARY);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { goBack(); }
            @Override public void mouseEntered(MouseEvent e) { loginLink.setText("<html><u>Sign in</u></html>"); }
            @Override public void mouseExited(MouseEvent e)  { loginLink.setText("Sign in"); }
        });
        loginRow.add(already); loginRow.add(loginLink);

        card.add(icon);
        card.add(Box.createVerticalStrut(4));
        card.add(title);
        card.add(Box.createVerticalStrut(2));
        card.add(sub);
        card.add(Box.createVerticalStrut(24));
        card.add(nameEmailRow);
        card.add(Box.createVerticalStrut(12));
        card.add(phoneAgeRow);
        card.add(Box.createVerticalStrut(12));
        card.add(bloodRow);
        card.add(Box.createVerticalStrut(12));
        card.add(passRow);
        card.add(Box.createVerticalStrut(8));
        card.add(statusLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(regBtn);
        card.add(Box.createVerticalStrut(16));
        card.add(loginRow);

        root.add(card);
    }

    private JPanel twoColRow(JPanel left, JPanel right) {
        JPanel row = new JPanel(new GridLayout(1, 2, 12, 0));
        row.setOpaque(false);
        row.add(left); row.add(right);
        return row;
    }

    private JPanel labeledField(String label, JComponent field) {
        return labeledComponent(label, field);
    }

    private JPanel labeledComponent(String label, JComponent comp) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_MED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        p.add(lbl);
        p.add(Box.createVerticalStrut(5));
        p.add(comp);
        return p;
    }

    private void doRegister() {
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String ageStr = ageField.getText().trim();
        String pass  = new String(passField.getPassword());
        String conf  = new String(confirmPassField.getPassword());
        String blood = (String) bloodTypeBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Please fill in all required fields."); return;
        }
        if (!email.contains("@")) {
            statusLabel.setText("Please enter a valid email address."); return;
        }
        if (pass.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters."); return;
        }
        if (!pass.equals(conf)) {
            statusLabel.setText("Passwords do not match."); return;
        }
        int age = 25;
        try { age = Integer.parseInt(ageStr); } catch (NumberFormatException ignored) {}

        Patient p = facade.registerPatient(name, email, pass, phone, age, blood);
        if (p == null) {
            statusLabel.setText("Email already registered. Please sign in."); return;
        }

        JOptionPane.showMessageDialog(this,
                "Account created successfully! Welcome to MediSphere, " + name + "!",
                "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        goBack();
    }

    private void goBack() {
        dispose();
        loginFrame.setVisible(true);
    }
}
