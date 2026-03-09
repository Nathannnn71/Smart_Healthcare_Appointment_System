package com.medisphere.ui;

import com.medisphere.model.*;
import com.medisphere.pattern.*;
import com.medisphere.ui.components.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientDashboard extends JFrame {
    private final Patient patient;
    private final HealthcareFacade facade = HealthcareFacade.getInstance();
    private JPanel contentArea;
    private JLabel[] navLabels;
    private int activeNav = 0;
    private static final String[] NAV_ICONS = {"🏠", "🔍", "📅", "⭐", "🔔", "👤"};
    private static final String[] NAV_LABELS = {"Dashboard", "Find Doctors", "My Appointments", "Recommendations", "Notifications", "My Profile"};

    public PatientDashboard(Patient patient) {
        this.patient = patient;
        setTitle("MediSphere – Patient Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1180, 740);
        setMinimumSize(new Dimension(1000, 640));
        setLocationRelativeTo(null);
        buildUI();
        showPanel(0);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_PAGE);
        setContentPane(root);
        root.add(buildSidebar(), BorderLayout.WEST);
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(UITheme.BG_PAGE);
        main.add(buildTopBar(), BorderLayout.NORTH);
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UITheme.BG_PAGE);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        main.add(contentArea, BorderLayout.CENTER);
        root.add(main, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0D2137), 0, getHeight(), new Color(0x0A1929));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(UITheme.SIDEBAR_W, 0));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 20));
        logoPanel.setOpaque(false);
        JLabel logoIcon = new JLabel("⚕");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoIcon.setForeground(UITheme.ACCENT_LIGHT);
        JPanel logoText = new JPanel();
        logoText.setOpaque(false);
        logoText.setLayout(new BoxLayout(logoText, BoxLayout.Y_AXIS));
        JLabel logoName = new JLabel("MediSphere");
        logoName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoName.setForeground(Color.WHITE);
        JLabel logoSub = new JLabel("Healthcare Portal");
        logoSub.setFont(UITheme.FONT_SMALL);
        logoSub.setForeground(new Color(255,255,255,120));
        logoText.add(logoName); logoText.add(logoSub);
        logoPanel.add(logoIcon); logoPanel.add(logoText);
        sidebar.add(logoPanel);

        // Divider
        sidebar.add(sidebarDivider());

        // Nav items
        navLabels = new JLabel[NAV_LABELS.length];
        for (int i = 0; i < NAV_LABELS.length; i++) {
            final int idx = i;
            JPanel item = buildNavItem(i);
            item.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { showPanel(idx); }
                @Override public void mouseEntered(MouseEvent e) {
                    if (activeNav != idx) item.setBackground(UITheme.SIDEBAR_HOVER);
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (activeNav != idx) item.setBackground(new Color(0, 0, 0, 0));
                }
            });
            sidebar.add(item);
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(sidebarDivider());

        // User info at bottom
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        userInfo.setOpaque(false);
        JPanel avatar = buildAvatarCircle(patient.getName(), UITheme.ACCENT, 36);
        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        JLabel uName = new JLabel(patient.getName().length() > 16 ? patient.getName().substring(0, 14) + "…" : patient.getName());
        uName.setFont(UITheme.FONT_BOLD);
        uName.setForeground(Color.WHITE);
        JLabel uRole = new JLabel("Patient");
        uRole.setFont(UITheme.FONT_SMALL);
        uRole.setForeground(new Color(255,255,255,140));
        namePanel.add(uName); namePanel.add(uRole);
        userInfo.add(avatar); userInfo.add(namePanel);
        sidebar.add(userInfo);

        // Logout button
        JPanel logoutRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        logoutRow.setOpaque(false);
        RoundedButton logoutBtn = new RoundedButton("🚪  Logout", RoundedButton.Style.OUTLINE);
        logoutBtn.setForeground(new Color(255,255,255,200));
        logoutBtn.setFont(UITheme.FONT_SMALL);
        logoutBtn.setMaximumSize(new Dimension(180, 32));
        logoutBtn.addActionListener(e -> logout());
        logoutRow.add(logoutBtn);
        sidebar.add(logoutRow);
        sidebar.add(Box.createVerticalStrut(10));
        return sidebar;
    }

    private JPanel buildNavItem(int idx) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        item.setOpaque(false);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel iconLbl = new JLabel(NAV_ICONS[idx]);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLbl.setForeground(Color.WHITE);

        navLabels[idx] = new JLabel(NAV_LABELS[idx]);
        navLabels[idx].setFont(UITheme.FONT_SIDEBAR_ITEM);
        navLabels[idx].setForeground(new Color(255,255,255,180));

        item.add(iconLbl);
        item.add(navLabels[idx]);
        return item;
    }

    private void updateNavHighlight(int idx) {
        // Reset all
        Container sidebar = (Container) ((JPanel) getContentPane().getComponent(0));
        for (Component c : sidebar.getComponents()) {
            if (c instanceof JPanel) ((JPanel) c).setBackground(new Color(0, 0, 0, 0));
        }
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setPreferredSize(new Dimension(0, UITheme.TOPBAR_H));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(0, 24, 0, 24)));

        JLabel pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(UITheme.FONT_H2);
        pageTitle.setForeground(UITheme.TEXT_DARK);
        bar.add(pageTitle, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        right.setOpaque(false);

        int notifCount = patient.getNotifications().size();
        JLabel notifBell = new JLabel("🔔" + (notifCount > 0 ? " (" + notifCount + ")" : ""));
        notifBell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        notifBell.setForeground(notifCount > 0 ? UITheme.WARNING_LIGHT : UITheme.TEXT_LIGHT);
        notifBell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        notifBell.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { showPanel(4); }
        });

        JLabel welcome = new JLabel("Hi, " + patient.getName().split(" ")[0] + " 👋");
        welcome.setFont(UITheme.FONT_BODY);
        welcome.setForeground(UITheme.TEXT_MED);

        right.add(notifBell);
        right.add(welcome);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private void showPanel(int idx) {
        activeNav = idx;
        contentArea.removeAll();
        JPanel panel;
        switch (idx) {
            case 0: panel = buildHomeDashboard(); break;
            case 1: panel = buildFindDoctorsPanel(); break;
            case 2: panel = buildMyAppointmentsPanel(); break;
            case 3: panel = buildRecommendationsPanel(); break;
            case 4: panel = buildNotificationsPanel(); break;
            default: panel = buildProfilePanel(); break;
        }
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ======= HOME DASHBOARD =======
    private JPanel buildHomeDashboard() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        List<Appointment> allApts = facade.getPatientAppointments(patient.getUserId());
        long upcoming = allApts.stream().filter(Appointment::isUpcoming).count();
        long completed = allApts.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED || a.getStatus() == AppointmentStatus.CONFIRMED).count();
        long cancelled = allApts.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count();

        // Section title
        p.add(sectionHeader("Overview", "Welcome back, " + patient.getName().split(" ")[0] + "!"));
        p.add(Box.createVerticalStrut(16));

        // Stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        statsRow.add(statCard("📅", String.valueOf(upcoming), "Upcoming", UITheme.PRIMARY, new Color(0xE3F2FD)));
        statsRow.add(statCard("✓", String.valueOf(completed), "Completed", UITheme.SUCCESS, new Color(0xE8F5E9)));
        statsRow.add(statCard("✗", String.valueOf(cancelled), "Cancelled", UITheme.ERROR, new Color(0xFFEBEE)));
        statsRow.add(statCard("👨‍⚕️", String.valueOf(facade.getTotalDoctors()), "Doctors Available", UITheme.ACCENT, new Color(0xE0F7FA)));
        p.add(statsRow);
        p.add(Box.createVerticalStrut(24));

        // Upcoming appointments
        p.add(sectionHeader("Upcoming Appointments", null));
        p.add(Box.createVerticalStrut(12));

        List<Appointment> upcomingList = allApts.stream()
                .filter(Appointment::isUpcoming)
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(java.util.stream.Collectors.toList());

        if (upcomingList.isEmpty()) {
            p.add(emptyState("📅", "No Upcoming Appointments", "Book your first appointment below."));
        } else {
            JPanel aptCards = new JPanel();
            aptCards.setLayout(new BoxLayout(aptCards, BoxLayout.Y_AXIS));
            aptCards.setOpaque(false);
            for (int i = 0; i < Math.min(3, upcomingList.size()); i++) {
                aptCards.add(buildAppointmentCard(upcomingList.get(i), true));
                aptCards.add(Box.createVerticalStrut(10));
            }
            p.add(aptCards);
        }

        p.add(Box.createVerticalStrut(20));

        // Quick Actions
        p.add(sectionHeader("Quick Actions", null));
        p.add(Box.createVerticalStrut(12));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setOpaque(false);
        RoundedButton bookBtn = new RoundedButton("+ Book Appointment");
        bookBtn.addActionListener(e -> showPanel(1));
        RoundedButton viewBtn = new RoundedButton("View All Appointments", RoundedButton.Style.SECONDARY);
        viewBtn.addActionListener(e -> showPanel(2));
        RoundedButton recBtn = new RoundedButton("Get Recommendations", RoundedButton.Style.OUTLINE);
        recBtn.addActionListener(e -> showPanel(3));
        actions.add(bookBtn); actions.add(viewBtn); actions.add(recBtn);
        p.add(actions);

        JScrollPane scroll = new JScrollPane(p);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scroll);
        return wrapper;
    }

    // ======= FIND DOCTORS =======
    private JPanel buildFindDoctorsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setOpaque(false);

        p.add(sectionHeader("Find Doctors", "Search and filter specialists"), BorderLayout.NORTH);

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        searchBar.setOpaque(false);
        StyledTextField searchField = new StyledTextField("Search by doctor name...", 20);
        searchField.setPreferredSize(new Dimension(240, 38));
        String[] specs = new String[Specialty.values().length + 1];
        specs[0] = "All Specialties";
        for (int i = 0; i < Specialty.values().length; i++) specs[i + 1] = Specialty.values()[i].getDisplayName();
        JComboBox<String> specFilter = new JComboBox<>(specs);
        specFilter.setFont(UITheme.FONT_INPUT);
        specFilter.setPreferredSize(new Dimension(180, 38));
        RoundedButton searchBtn = new RoundedButton("Search");
        searchBtn.setPreferredSize(new Dimension(100, 38));

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);

        Runnable doSearch = () -> {
            resultsPanel.removeAll();
            String name = searchField.getText().trim();
            String spec = specFilter.getSelectedIndex() == 0 ? "" : (String) specFilter.getSelectedItem();
            List<Doctor> docs = facade.searchDoctors(spec, name);
            if (docs.isEmpty()) {
                resultsPanel.add(emptyState("🔍", "No Doctors Found", "Try different search criteria."));
            } else {
                JPanel grid = new JPanel(new GridLayout(0, 2, 14, 14));
                grid.setOpaque(false);
                for (Doctor d : docs) grid.add(buildDoctorCard(d));
                resultsPanel.add(grid);
            }
            resultsPanel.revalidate(); resultsPanel.repaint();
        };

        searchBtn.addActionListener(e -> doSearch.run());
        searchField.addActionListener(e -> doSearch.run());
        specFilter.addActionListener(e -> doSearch.run());

        searchBar.add(searchField);
        searchBar.add(specFilter);
        searchBar.add(searchBtn);

        doSearch.run();

        JScrollPane scroll = new JScrollPane(resultsPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(searchBar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // ======= MY APPOINTMENTS =======
    private JPanel buildMyAppointmentsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setOpaque(false);
        p.add(sectionHeader("My Appointments", "Manage your appointment history"), BorderLayout.NORTH);

        List<Appointment> apts = facade.getPatientAppointments(patient.getUserId());

        JPanel aptList = new JPanel();
        aptList.setLayout(new BoxLayout(aptList, BoxLayout.Y_AXIS));
        aptList.setOpaque(false);

        if (apts.isEmpty()) {
            aptList.add(emptyState("📅", "No Appointments Yet", "Book your first appointment from Find Doctors."));
        } else {
            for (Appointment a : apts) {
                aptList.add(buildAppointmentCard(a, true));
                aptList.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(aptList);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    // ======= RECOMMENDATIONS =======
    private JPanel buildRecommendationsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setOpaque(false);
        p.add(sectionHeader("Doctor Recommendations", "Personalized suggestions based on your health profile"), BorderLayout.NORTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        top.setOpaque(false);

        // Strategy selector
        JLabel stratLabel = new JLabel("Recommendation Mode:");
        stratLabel.setFont(UITheme.FONT_LABEL);
        stratLabel.setForeground(UITheme.TEXT_MED);
        JComboBox<String> stratBox = new JComboBox<>(new String[]{"Symptom-Based", "History-Based"});
        stratBox.setFont(UITheme.FONT_INPUT);
        stratBox.setPreferredSize(new Dimension(180, 36));

        // Symptom input
        StyledTextField symptomField = new StyledTextField("e.g. headache, chest pain...", 20);
        symptomField.setPreferredSize(new Dimension(240, 36));
        RoundedButton addSymBtn = new RoundedButton("Add Symptom", RoundedButton.Style.SECONDARY);
        addSymBtn.setPreferredSize(new Dimension(130, 36));

        top.add(stratLabel); top.add(stratBox);
        top.add(Box.createHorizontalStrut(20));
        top.add(new JLabel("Symptom:") {{setFont(UITheme.FONT_LABEL); setForeground(UITheme.TEXT_MED);}});
        top.add(symptomField); top.add(addSymBtn);

        JPanel recResults = new JPanel();
        recResults.setLayout(new BoxLayout(recResults, BoxLayout.Y_AXIS));
        recResults.setOpaque(false);

        Runnable loadRec = () -> {
            recResults.removeAll();
            int stratIdx = stratBox.getSelectedIndex();
            if (stratIdx == 0)
                facade.setRecommendationStrategy(new SymptomBasedRecommendation());
            else
                facade.setRecommendationStrategy(new HistoryBasedRecommendation());

            List<Doctor> recs = facade.getRecommendations(patient);

            String stratName = facade.getCurrentStrategy().getStrategyName();
            String stratDesc = facade.getCurrentStrategy().getStrategyDescription();
            RoundedPanel infoBox = new RoundedPanel(8, new Color(0xE3F2FD), false);
            infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
            infoBox.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
            infoBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
            JLabel sn = new JLabel("Strategy: " + stratName);
            sn.setFont(UITheme.FONT_BOLD); sn.setForeground(UITheme.PRIMARY);
            JLabel sd = new JLabel(stratDesc);
            sd.setFont(UITheme.FONT_SMALL); sd.setForeground(UITheme.TEXT_MED);
            infoBox.add(sn); infoBox.add(sd);
            recResults.add(infoBox);
            recResults.add(Box.createVerticalStrut(14));

            if (recs.isEmpty()) {
                recResults.add(emptyState("⭐", "No Recommendations", "Add your symptoms to get personalized recommendations."));
            } else {
                JPanel grid = new JPanel(new GridLayout(0, 2, 14, 14));
                grid.setOpaque(false);
                for (Doctor d : recs) grid.add(buildDoctorCard(d));
                recResults.add(grid);
            }
            recResults.revalidate(); recResults.repaint();
        };

        addSymBtn.addActionListener(e -> {
            String sym = symptomField.getText().trim();
            if (!sym.isEmpty()) { patient.addSymptom(sym); symptomField.setText(""); loadRec.run(); }
        });
        stratBox.addActionListener(e -> loadRec.run());
        loadRec.run();

        JScrollPane scroll = new JScrollPane(recResults);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(top, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // ======= NOTIFICATIONS =======
    private JPanel buildNotificationsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setOpaque(false);
        p.add(sectionHeader("Notifications", patient.getNotifications().size() + " notification(s)"), BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        if (patient.getNotifications().isEmpty()) {
            list.add(emptyState("🔔", "No Notifications", "You're all caught up!"));
        } else {
            for (String notif : patient.getNotifications()) {
                RoundedPanel card = new RoundedPanel(8, Color.WHITE, false);
                card.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 12));
                card.setBorderColor(UITheme.BORDER);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
                JLabel lbl = new JLabel(notif);
                lbl.setFont(UITheme.FONT_BODY);
                lbl.setForeground(UITheme.TEXT_DARK);
                card.add(lbl);
                list.add(card);
                list.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    // ======= PROFILE =======
    private JPanel buildProfilePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setOpaque(false);
        p.add(sectionHeader("My Profile", "Manage your personal information"), BorderLayout.NORTH);

        RoundedPanel card = new RoundedPanel(12, Color.WHITE, true);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(30, 36, 30, 36));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Avatar
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarPanel.setOpaque(false);
        avatarPanel.add(buildAvatarCircle(patient.getName(), UITheme.PRIMARY, 72));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(avatarPanel, gbc);

        String[][] fields = {
                {"Full Name", patient.getName()},
                {"Email", patient.getEmail()},
                {"Phone", patient.getPhone()},
                {"Age", String.valueOf(patient.getAge())},
                {"Blood Type", patient.getBloodType()},
                {"Patient ID", patient.getUserId()}
        };

        gbc.gridwidth = 1;
        for (int i = 0; i < fields.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1; gbc.fill = GridBagConstraints.NONE;
            JLabel lbl = new JLabel(fields[i][0] + ":");
            lbl.setFont(UITheme.FONT_BOLD);
            lbl.setForeground(UITheme.TEXT_MED);
            card.add(lbl, gbc);
            gbc.gridx = 1;
            JLabel val = new JLabel(fields[i][1]);
            val.setFont(UITheme.FONT_BODY);
            val.setForeground(UITheme.TEXT_DARK);
            card.add(val, gbc);
        }

        JPanel cardWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardWrapper.setOpaque(false);
        cardWrapper.add(card);
        p.add(cardWrapper, BorderLayout.CENTER);
        return p;
    }

    // ======= BOOK APPOINTMENT DIALOG =======
    private void openBookDialog(Doctor doctor) {
        JDialog dlg = new JDialog(this, "Book Appointment – Dr. " + doctor.getName(), true);
        dlg.setSize(520, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        form.setBackground(Color.WHITE);

        // Doctor info header
        RoundedPanel docInfo = new RoundedPanel(8, new Color(0xE3F2FD), false);
        docInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        docInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        JPanel av = buildAvatarCircle(doctor.getName(), UITheme.fromHex(doctor.getSpecialty().getColor()), 40);
        JPanel docDetails = new JPanel();
        docDetails.setOpaque(false);
        docDetails.setLayout(new BoxLayout(docDetails, BoxLayout.Y_AXIS));
        JLabel dName = new JLabel("Dr. " + doctor.getName());
        dName.setFont(UITheme.FONT_H3); dName.setForeground(UITheme.TEXT_DARK);
        JLabel dSpec = new JLabel(doctor.getSpecialty().getDisplayName() + " · " + doctor.getHospitalAffiliation());
        dSpec.setFont(UITheme.FONT_SMALL); dSpec.setForeground(UITheme.TEXT_LIGHT);
        docDetails.add(dName); docDetails.add(dSpec);
        docInfo.add(av); docInfo.add(docDetails);
        docInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(docInfo);
        form.add(Box.createVerticalStrut(20));

        // Date picker
        addFormLabel(form, "Appointment Date");
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpin = new JSpinner(dateModel);
        JSpinner.DateEditor dateEd = new JSpinner.DateEditor(dateSpin, "dd/MM/yyyy");
        dateSpin.setEditor(dateEd);
        dateSpin.setFont(UITheme.FONT_INPUT);
        dateSpin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        dateSpin.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(dateSpin);
        form.add(Box.createVerticalStrut(14));

        // Time slots
        addFormLabel(form, "Available Time Slots");
        JPanel slotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        slotPanel.setOpaque(false);
        slotPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ButtonGroup slotGroup = new ButtonGroup();
        final String[] selectedTime = {null};

        LocalDate initDate = LocalDate.now().plusDays(1);
        List<TimeSlot> slots = facade.getAvailableSlots(doctor.getUserId(), initDate);
        for (TimeSlot slot : slots) {
            if (slot.isAvailable()) {
                JToggleButton tb = new JToggleButton(slot.getDisplayTime());
                tb.setFont(UITheme.FONT_SMALL);
                tb.setBackground(new Color(0xE8F5E9));
                tb.setForeground(UITheme.SUCCESS);
                tb.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.SUCCESS_LIGHT, 1, true),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
                tb.setFocusPainted(false);
                tb.addActionListener(e -> selectedTime[0] = slot.getDisplayTime());
                slotGroup.add(tb);
                slotPanel.add(tb);
            }
        }

        JScrollPane slotScroll = new JScrollPane(slotPanel);
        slotScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        slotScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        slotScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(slotScroll);
        form.add(Box.createVerticalStrut(14));

        // Reason
        addFormLabel(form, "Reason for Visit");
        JTextArea reasonArea = new JTextArea(3, 20);
        reasonArea.setFont(UITheme.FONT_INPUT);
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        reasonArea.setLineWrap(true);
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        reasonScroll.setBorder(null);
        reasonScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        reasonScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(reasonScroll);
        form.add(Box.createVerticalStrut(18));

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        RoundedButton cancel = new RoundedButton("Cancel", RoundedButton.Style.GHOST);
        RoundedButton confirm = new RoundedButton("Confirm Booking");
        cancel.setPreferredSize(new Dimension(100, 38));
        confirm.setPreferredSize(new Dimension(150, 38));

        cancel.addActionListener(e -> dlg.dispose());
        confirm.addActionListener(e -> {
            if (selectedTime[0] == null) {
                JOptionPane.showMessageDialog(dlg, "Please select a time slot.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String reason = reasonArea.getText().trim();
            if (reason.isEmpty()) reason = "General consultation";

            java.util.Date d = (java.util.Date) dateSpin.getValue();
            LocalDate ld = d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalTime lt = LocalTime.parse(selectedTime[0], DateTimeFormatter.ofPattern("hh:mm a"));

            Appointment apt = facade.bookAppointment(patient.getUserId(), doctor.getUserId(), ld, lt, reason);
            if (apt != null) {
                dlg.dispose();
                JOptionPane.showMessageDialog(this,
                        "✓ Appointment Booked!\n\nID: " + apt.getAppointmentId() +
                        "\nDoctor: Dr. " + doctor.getName() +
                        "\nDate: " + apt.getFormattedDateTime() +
                        "\n\nConfirmation sent to " + patient.getEmail(),
                        "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
                showPanel(2);
            }
        });

        btnRow.add(cancel); btnRow.add(confirm);
        form.add(btnRow);

        dlg.add(new JScrollPane(form) {{ setBorder(null); }});
        dlg.setVisible(true);
    }

    // ======= HELPER UI BUILDERS =======
    private JPanel buildDoctorCard(Doctor d) {
        RoundedPanel card = new RoundedPanel(12, Color.WHITE, true);
        card.setLayout(new BorderLayout(0, 0));
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Top: avatar + name + specialty
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        top.setOpaque(false);
        Color specColor = UITheme.fromHex(d.getSpecialty().getColor());
        JPanel av = buildAvatarCircle(d.getName(), specColor, 48);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("Dr. " + d.getName());
        name.setFont(UITheme.FONT_H3);
        name.setForeground(UITheme.TEXT_DARK);

        JPanel specBadge = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        specBadge.setOpaque(false);
        JLabel specLbl = new JLabel(d.getSpecialty().getIcon() + " " + d.getSpecialty().getDisplayName());
        specLbl.setFont(UITheme.FONT_SMALL);
        specLbl.setForeground(specColor);
        specLbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(specColor, 1, true),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        specBadge.add(specLbl);

        JLabel hospital = new JLabel("🏥 " + d.getHospitalAffiliation());
        hospital.setFont(UITheme.FONT_SMALL);
        hospital.setForeground(UITheme.TEXT_LIGHT);

        info.add(name);
        info.add(Box.createVerticalStrut(3));
        info.add(specBadge);
        info.add(Box.createVerticalStrut(2));
        info.add(hospital);
        top.add(av); top.add(info);
        card.add(top, BorderLayout.NORTH);

        // Mid: rating, exp, fee
        JPanel mid = new JPanel(new GridLayout(1, 3, 6, 0));
        mid.setOpaque(false);
        mid.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        mid.add(miniStat("⭐ " + String.format("%.1f", d.getRating()), d.getReviewCount() + " reviews"));
        mid.add(miniStat("🎓 " + d.getExperienceYears() + " yrs", "Experience"));
        mid.add(miniStat("💰 " + d.getConsultationFee(), "Fee"));
        card.add(mid, BorderLayout.CENTER);

        // Bottom: availability + book button
        JPanel bot = new JPanel(new BorderLayout());
        bot.setOpaque(false);
        JLabel hours = new JLabel("🕐 " + d.getConsultationHours());
        hours.setFont(UITheme.FONT_SMALL);
        hours.setForeground(UITheme.TEXT_LIGHT);
        RoundedButton bookBtn = new RoundedButton("Book Now");
        bookBtn.setPreferredSize(new Dimension(110, 32));
        bookBtn.addActionListener(e -> openBookDialog(d));
        bot.add(hours, BorderLayout.WEST);
        bot.add(bookBtn, BorderLayout.EAST);
        card.add(bot, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildAppointmentCard(Appointment a, boolean withActions) {
        Color statusColor = UITheme.fromHex(a.getStatus().getHexColor());
        RoundedPanel card = new RoundedPanel(10, Color.WHITE, true);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Left: color bar
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(statusColor);
                g.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(4, 0));
        card.add(bar, BorderLayout.WEST);

        // Center: info
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel top = new JLabel("Dr. " + a.getDoctorName() + "  ·  " + a.getSpecialty().getDisplayName());
        top.setFont(UITheme.FONT_H3);
        top.setForeground(UITheme.TEXT_DARK);
        JLabel dtl = new JLabel("📅 " + a.getFormattedDateTime() + "   🆔 " + a.getAppointmentId());
        dtl.setFont(UITheme.FONT_SMALL);
        dtl.setForeground(UITheme.TEXT_LIGHT);
        info.add(top);
        info.add(Box.createVerticalStrut(3));
        info.add(dtl);
        card.add(info, BorderLayout.CENTER);

        // Right: status + actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JLabel statusLbl = new JLabel(a.getStatus().getDisplayName());
        statusLbl.setFont(UITheme.FONT_SMALL);
        statusLbl.setForeground(statusColor);
        statusLbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(statusColor, 1, true),
                BorderFactory.createEmptyBorder(3, 9, 3, 9)));
        right.add(statusLbl);

        if (withActions && a.isUpcoming()) {
            RoundedButton cancelBtn = new RoundedButton("Cancel", RoundedButton.Style.DANGER);
            cancelBtn.setFont(UITheme.FONT_SMALL);
            cancelBtn.setPreferredSize(new Dimension(80, 28));
            cancelBtn.addActionListener(e -> {
                int res = JOptionPane.showConfirmDialog(this,
                        "Cancel appointment with Dr. " + a.getDoctorName() + "?",
                        "Confirm Cancel", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    facade.cancelAppointment(a.getAppointmentId());
                    showPanel(2);
                }
            });
            right.add(cancelBtn);
        }
        card.add(right, BorderLayout.EAST);
        return card;
    }

    // ── Utility components ──────────────────────────────────────
    private JPanel buildAvatarCircle(String name, Color color, int size) {
        JPanel av = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
                g2.fillOval(0, 0, size, size);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(1, 1, size-2, size-2);
                String init = name.isEmpty() ? "?" :
                        name.split(" ").length > 1
                        ? "" + name.split(" ")[0].charAt(0) + name.split(" ")[1].charAt(0)
                        : "" + name.charAt(0);
                g2.setFont(new Font("Segoe UI", Font.BOLD, size / 3));
                g2.setColor(color);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(init.toUpperCase(),
                        (size - fm.stringWidth(init)) / 2,
                        (size + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        av.setOpaque(false);
        av.setPreferredSize(new Dimension(size, size));
        av.setMinimumSize(new Dimension(size, size));
        av.setMaximumSize(new Dimension(size, size));
        return av;
    }

    private JPanel statCard(String icon, String value, String label, Color color, Color bg) {
        RoundedPanel card = new RoundedPanel(12, bg, false);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel textP = new JPanel();
        textP.setOpaque(false);
        textP.setLayout(new BoxLayout(textP, BoxLayout.Y_AXIS));
        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valLbl.setForeground(color);
        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(UITheme.FONT_SMALL);
        lblLbl.setForeground(UITheme.TEXT_LIGHT);
        textP.add(valLbl); textP.add(lblLbl);

        card.add(iconLbl, BorderLayout.WEST);
        card.add(textP, BorderLayout.CENTER);
        return card;
    }

    private JPanel miniStat(String value, String label) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel v = new JLabel(value);
        v.setFont(UITheme.FONT_BOLD);
        v.setForeground(UITheme.TEXT_DARK);
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_SMALL);
        l.setForeground(UITheme.TEXT_LIGHT);
        p.add(v); p.add(l);
        return p;
    }

    private JPanel sectionHeader(String title, String subtitle) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_H1);
        t.setForeground(UITheme.TEXT_DARK);
        p.add(t);
        if (subtitle != null && !subtitle.isEmpty()) {
            JLabel s = new JLabel(subtitle);
            s.setFont(UITheme.FONT_BODY);
            s.setForeground(UITheme.TEXT_LIGHT);
            p.add(s);
        }
        return p;
    }

    private JPanel emptyState(String icon, String title, String desc) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        JLabel ic = new JLabel(icon);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        ic.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel t = new JLabel(title);
        t.setFont(UITheme.FONT_H2);
        t.setForeground(UITheme.TEXT_MED);
        t.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel d = new JLabel(desc);
        d.setFont(UITheme.FONT_BODY);
        d.setForeground(UITheme.TEXT_LIGHT);
        d.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(ic); p.add(Box.createVerticalStrut(8)); p.add(t);
        p.add(Box.createVerticalStrut(4)); p.add(d);
        return p;
    }

    private JPanel sidebarDivider() {
        JPanel div = new JPanel();
        div.setOpaque(false);
        div.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        div.setPreferredSize(new Dimension(0, 1));
        div.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 20)));
        return div;
    }

    private void addFormLabel(JPanel form, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_MED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lbl);
        form.add(Box.createVerticalStrut(5));
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
