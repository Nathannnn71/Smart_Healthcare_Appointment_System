package medisphere.ui;

import medisphere.data.DataStore;
import medisphere.models.Specialization;
import medisphere.patterns.factory.UserFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RegisterPanel extends JPanel {

    private final MainFrame frame;
    private JTextField  nameField, emailField, phoneField, dobField;
    private JPasswordField passField, confirmPassField;
    private JComboBox<String> roleCombo;
    private JComboBox<Specialization> specCombo;
    private JTextField qualField, expField, feeField;
    private JPanel     doctorExtras;
    private JLabel     msgLabel;

    public RegisterPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20,40,20,40));
        JLabel logo = new JLabel("\u2665  MediSphere  –  Create Account");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(UITheme.PRIMARY);
        header.add(logo, BorderLayout.WEST);

        JButton backBtn = UITheme.secondaryButton("\u2190  Back to Login");
        backBtn.addActionListener(e -> frame.showCard(MainFrame.CARD_LOGIN));
        header.add(backBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Scrollable form
        JPanel content = buildForm();
        JScrollPane scroll = new JScrollPane(content);
        UITheme.flatScrollPane(scroll);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UITheme.BG);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                new UITheme.ShadowBorder(), new EmptyBorder(40,50,40,50)));
        form.setMaximumSize(new Dimension(600, 9999));
        form.setPreferredSize(new Dimension(600, 700));

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Fill in the details below to register.");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        // Role selector
        JLabel roleLabel = UITheme.sectionLabel("Register as");
        roleLabel.setAlignmentX(LEFT_ALIGNMENT);
        roleCombo = new JComboBox<>(new String[]{"Patient","Doctor"});
        styleCombo(roleCombo);
        roleCombo.setAlignmentX(LEFT_ALIGNMENT);
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        // Common fields
        nameField = styledField("Full Name");
        emailField = styledField("Email Address");
        phoneField = styledField("Phone Number (e.g. 0123456789)");
        dobField   = styledField("Date of Birth (dd-MM-yyyy)  [Patient]");
        passField  = new JPasswordField(); styleInput(passField);
        confirmPassField = new JPasswordField(); styleInput(confirmPassField);

        // Doctor extra fields
        specCombo  = new JComboBox<>(Specialization.values());
        styleCombo(specCombo);
        qualField  = styledField("Qualification (e.g. MBBS, Sp.JP)");
        expField   = styledField("Years of Experience");
        feeField   = styledField("Consultation Fee (RM)");

        doctorExtras = new JPanel();
        doctorExtras.setLayout(new BoxLayout(doctorExtras, BoxLayout.Y_AXIS));
        doctorExtras.setOpaque(false);
        doctorExtras.setAlignmentX(LEFT_ALIGNMENT);
        addLabeledField(doctorExtras, "Specialization",        specCombo);
        addLabeledField(doctorExtras, "Qualification",         qualField);
        addLabeledField(doctorExtras, "Years of Experience",   expField);
        addLabeledField(doctorExtras, "Consultation Fee (RM)", feeField);

        roleCombo.addActionListener(e -> {
            boolean isDoctor = "Doctor".equals(roleCombo.getSelectedItem());
            doctorExtras.setVisible(isDoctor);
            dobField.setVisible(!isDoctor);
            form.revalidate(); form.repaint();
        });
        doctorExtras.setVisible(false);

        msgLabel = new JLabel(" ");
        msgLabel.setFont(UITheme.FONT_SMALL);
        msgLabel.setForeground(UITheme.DANGER);
        msgLabel.setAlignmentX(LEFT_ALIGNMENT);

        JButton registerBtn = UITheme.primaryButton("Create Account");
        registerBtn.setAlignmentX(LEFT_ALIGNMENT);
        registerBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        registerBtn.addActionListener(e -> register());

        // Assemble
        form.add(title);
        form.add(Box.createVerticalStrut(4));
        form.add(sub);
        form.add(Box.createVerticalStrut(24));
        addLabeledField(form, "Register as",    roleCombo);
        addLabeledField(form, "Full Name",       nameField);
        addLabeledField(form, "Email",           emailField);
        addLabeledField(form, "Phone",           phoneField);
        addLabeledField(form, "Date of Birth",   dobField);
        addLabeledField(form, "Password",        passField);
        addLabeledField(form, "Confirm Password",confirmPassField);
        form.add(doctorExtras);
        form.add(Box.createVerticalStrut(8));
        form.add(msgLabel);
        form.add(Box.createVerticalStrut(16));
        form.add(registerBtn);

        wrapper.add(form);
        return wrapper;
    }

    private void addLabeledField(JPanel parent, String label, JComponent field) {
        JLabel lbl = UITheme.sectionLabel(label);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(5));
        parent.add(field);
        parent.add(Box.createVerticalStrut(14));
    }

    private JTextField styledField(String hint) {
        JTextField tf = new JTextField();
        tf.setToolTipText(hint);
        styleInput(tf);
        return tf;
    }

    private void styleInput(JComponent c) {
        c.setFont(UITheme.FONT_INPUT);
        c.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.RoundedBorder(8, UITheme.BORDER),
            new EmptyBorder(9,13,9,13)));
        c.setBackground(new Color(0xF8FAFF));
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(UITheme.FONT_INPUT);
        cb.setBorder(new EmptyBorder(4,8,4,8));
        cb.setBackground(new Color(0xF8FAFF));
    }

    private void register() {
        String role  = (String) roleCombo.getSelectedItem();
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String pass  = new String(passField.getPassword());
        String conf  = new String(confirmPassField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            showMsg("Please fill in all required fields.", UITheme.DANGER); return;
        }
        if (!pass.equals(conf)) {
            showMsg("Passwords do not match.", UITheme.DANGER); return;
        }
        if (pass.length() < 6) {
            showMsg("Password must be at least 6 characters.", UITheme.DANGER); return;
        }
        if (DataStore.getInstance().findUserByEmail(email) != null) {
            showMsg("Email already registered. Please login.", UITheme.DANGER); return;
        }

        try {
            if ("Patient".equals(role)) {
                String dob = dobField.getText().trim();
                UserFactory.createPatient(name, email, pass, phone, dob, "Unknown");
            } else {
                Specialization spec = (Specialization) specCombo.getSelectedItem();
                String qual = qualField.getText().trim();
                int exp = Integer.parseInt(expField.getText().trim());
                double fee = Double.parseDouble(feeField.getText().trim());
                UserFactory.createDoctor(name, email, pass, phone, spec, qual, exp, fee);
            }
            showMsg("Registration successful! You can now log in.", UITheme.ACCENT);
            Timer t = new Timer(1800, e -> frame.showCard(MainFrame.CARD_LOGIN));
            t.setRepeats(false); t.start();
        } catch (NumberFormatException ex) {
            showMsg("Please enter valid numeric values for experience/fee.", UITheme.DANGER);
        }
    }

    private void showMsg(String msg, Color color) {
        msgLabel.setText(msg);
        msgLabel.setForeground(color);
    }
}
