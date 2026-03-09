package medisphere.ui;

import medisphere.data.DataStore;
import medisphere.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * Attractive split-screen login panel.
 * Left  – deep-blue branded hero area with feature highlights.
 * Right – clean white form panel.
 */
public class LoginPanel extends JPanel {

    private final MainFrame frame;
    private JTextField     emailField;
    private JPasswordField passwordField;
    private JLabel         errorLabel;

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftHero(), buildRightForm());
        split.setDividerSize(0);
        split.setEnabled(false);
        split.setResizeWeight(0.45);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    // ── Left hero panel ──────────────────────────────────────
    private JPanel buildLeftHero() {
        JPanel hero = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0, new Color(0x0D47A1),
                                                     getWidth(), getHeight(), new Color(0x00ACC1));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());

                // Decorative circles
                g2.setColor(new Color(255,255,255,20));
                g2.fill(new Ellipse2D.Float(-80,-80,300,300));
                g2.fill(new Ellipse2D.Float(getWidth()-150, getHeight()-150, 300,300));
                g2.setColor(new Color(255,255,255,10));
                g2.fill(new Ellipse2D.Float(getWidth()/2-200, getHeight()/2-200, 400,400));
                g2.dispose();
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8,50,8,50);

        // Logo + brand
        JLabel logo = new JLabel("\u2665 MediSphere", SwingConstants.LEFT);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logo.setForeground(Color.WHITE);
        gbc.gridy = 0; gbc.insets = new Insets(0,50,6,50);
        hero.add(logo, gbc);

        JLabel tagline = new JLabel("Smart Healthcare Appointment System");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tagline.setForeground(new Color(180,210,255));
        gbc.gridy = 1; gbc.insets = new Insets(0,50,40,50);
        hero.add(tagline, gbc);

        // Feature bullets
        String[][] features = {
            {"\uD83D\uDCBB", "Book Appointments Online", "No more phone queues"},
            {"\uD83D\uDC68\u200D\u2695\uFE0F", "Find Specialists", "Search by field or symptom"},
            {"\uD83D\uDD14", "Smart Reminders", "Email & in-app notifications"},
            {"\uD83D\uDCCA", "Health Dashboard", "Track your appointment history"}
        };
        gbc.insets = new Insets(6,50,6,50);
        for (int i = 0; i < features.length; i++) {
            gbc.gridy = 2 + i;
            hero.add(featureRow(features[i][0], features[i][1], features[i][2]), gbc);
        }

        // Bottom note
        JLabel note = new JLabel("Trusted by patients and doctors across Malaysia");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        note.setForeground(new Color(150,190,240));
        gbc.gridy = 6; gbc.insets = new Insets(40,50,0,50);
        hero.add(note, gbc);

        return hero;
    }

    private JPanel featureRow(String icon, String title, String sub) {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setOpaque(false);

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Dialog", Font.PLAIN, 24));
        iconLbl.setPreferredSize(new Dimension(40,40));
        iconLbl.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel iconCircle = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255,40));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.dispose();
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(48,48));
        iconCircle.add(iconLbl);

        JPanel text = new JPanel(new GridLayout(2,1,0,2));
        text.setOpaque(false);
        JLabel t1 = new JLabel(title);
        t1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t1.setForeground(Color.WHITE);
        JLabel t2 = new JLabel(sub);
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        t2.setForeground(new Color(160,200,240));
        text.add(t1); text.add(t2);

        row.add(iconCircle, BorderLayout.WEST);
        row.add(text,       BorderLayout.CENTER);
        return row;
    }

    // ── Right form panel ─────────────────────────────────────
    private JPanel buildRightForm() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(0xFAFCFF));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                new UITheme.ShadowBorder(),
                new EmptyBorder(50,50,50,50)));
        form.setMaximumSize(new Dimension(460, 600));
        form.setPreferredSize(new Dimension(460, 580));

        // Title
        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to your MediSphere account");
        subtitle.setFont(UITheme.FONT_BODY);
        subtitle.setForeground(UITheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Email field
        JLabel emailLabel = UITheme.sectionLabel("Email Address");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField = new JTextField();
        emailField.setFont(UITheme.FONT_INPUT);
        styleInputField(emailField);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Password field
        JLabel passLabel = UITheme.sectionLabel("Password");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField = new JPasswordField();
        passwordField.setFont(UITheme.FONT_INPUT);
        styleInputField(passwordField);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UITheme.FONT_SMALL);
        errorLabel.setForeground(UITheme.DANGER);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Login button
        JButton loginBtn = UITheme.primaryButton("Sign In");
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        loginBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 48));
        loginBtn.addActionListener(e -> attemptLogin());

        // Demo credentials info
        JPanel demoPanel = buildDemoBox();
        demoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Register link
        JPanel regRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        regRow.setOpaque(false);
        regRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel regLbl = new JLabel("Don't have an account? ");
        regLbl.setFont(UITheme.FONT_BODY);
        regLbl.setForeground(UITheme.TEXT_SECONDARY);
        JButton regBtn = new JButton("Register here");
        regBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        regBtn.setForeground(UITheme.PRIMARY);
        regBtn.setBorderPainted(false); regBtn.setContentAreaFilled(false);
        regBtn.setFocusPainted(false);
        regBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regBtn.addActionListener(e -> frame.showCard(MainFrame.CARD_REGISTER));
        regRow.add(regLbl); regRow.add(regBtn);

        // Keyboard shortcut
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) attemptLogin();
            }
        });

        // Assemble form
        form.add(title);
        form.add(Box.createVerticalStrut(4));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(32));
        form.add(emailLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(emailField);
        form.add(Box.createVerticalStrut(18));
        form.add(passLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(passwordField);
        form.add(Box.createVerticalStrut(6));
        form.add(errorLabel);
        form.add(Box.createVerticalStrut(18));
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(24));
        form.add(demoPanel);
        form.add(Box.createVerticalStrut(24));
        form.add(regRow);

        outer.add(form);
        return outer;
    }

    private void styleInputField(JComponent field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.RoundedBorder(8, UITheme.BORDER),
            new EmptyBorder(10,14,10,14)));
        field.setBackground(new Color(0xF8FAFF));
    }

    private JPanel buildDemoBox() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.PRIMARY_LIGHT);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(12,14,12,14));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel hdr = new JLabel("\uD83D\uDCA1  Demo Credentials");
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hdr.setForeground(UITheme.PRIMARY_DARK);
        hdr.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(hdr);
        p.add(Box.createVerticalStrut(8));

        String[][] creds = {
            {"Patient :", "ali@email.com", "patient123"},
            {"Doctor  :", "ahmad@medisphere.my", "doc123"},
            {"Admin   :", "admin@medisphere.my", "admin123"}
        };
        for (String[] c : creds) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            row.setOpaque(false);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel role = new JLabel(c[0]);
            role.setFont(new Font("Segoe UI", Font.BOLD, 11));
            role.setForeground(UITheme.TEXT_PRIMARY);
            JLabel email = new JLabel(c[1] + " / " + c[2]);
            email.setFont(new Font("Segoe UI Mono", Font.PLAIN, 11));
            email.setForeground(UITheme.TEXT_SECONDARY);

            // Quick fill button
            JButton fill = new JButton("Use");
            fill.setFont(new Font("Segoe UI", Font.BOLD, 10));
            fill.setForeground(UITheme.PRIMARY);
            fill.setBorderPainted(false); fill.setContentAreaFilled(false);
            fill.setFocusPainted(false);
            fill.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            final String em = c[1], pw = c[2];
            fill.addActionListener(e -> { emailField.setText(em); passwordField.setText(pw); });

            row.add(role); row.add(email); row.add(fill);
            p.add(row);
        }
        return p;
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String pass  = new String(passwordField.getPassword());
        if (email.isEmpty() || pass.isEmpty()) {
            showError("Please enter your email and password.");
            return;
        }
        User user = DataStore.getInstance().findUserByEmail(email);
        if (user == null || !user.authenticate(email, pass)) {
            showError("Invalid email or password. Please try again.");
            shakeField(emailField);
            return;
        }
        if (!user.isActive()) {
            showError("Your account has been deactivated. Contact admin.");
            return;
        }
        errorLabel.setText(" ");
        frame.loginSuccess(user);
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
    }

    private void shakeField(JComponent c) {
        Point orig = c.getLocation();
        Timer timer = new Timer(40, null);
        int[] step = {0};
        int[] offsets = {-8,8,-6,6,-4,4,-2,2,0};
        timer.addActionListener(e -> {
            if (step[0] < offsets.length) {
                c.setLocation(orig.x + offsets[step[0]++], orig.y);
            } else {
                c.setLocation(orig);
                timer.stop();
            }
        });
        timer.start();
    }
}
