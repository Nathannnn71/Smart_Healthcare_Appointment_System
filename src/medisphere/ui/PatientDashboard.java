package medisphere.ui;

import medisphere.data.DataStore;
import medisphere.models.*;
import medisphere.patterns.facade.AppointmentFacade;
import medisphere.patterns.strategy.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PatientDashboard extends JPanel {

    private final MainFrame        frame;
    private final Patient          patient;
    private final AppointmentFacade facade;
    private final DataStore        store = DataStore.getInstance();

    private JPanel contentArea;
    private CardLayout contentLayout;
    private JLabel notifBadge;
    private JPanel homeStatsPanel;
    private JPanel homeApptList;
    private String activeNav = "HOME";

    // Nav card names
    private static final String NAV_HOME   = "HOME";
    private static final String NAV_SEARCH = "SEARCH";
    private static final String NAV_APPTS  = "APPOINTMENTS";
    private static final String NAV_NOTIF  = "NOTIFICATIONS";
    private static final String NAV_PROFILE= "PROFILE";

    public PatientDashboard(MainFrame frame, User user, AppointmentFacade facade) {
        this.frame   = frame;
        this.patient = (Patient) user;
        this.facade  = facade;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);

        add(buildSidebar(),  BorderLayout.WEST);
        add(buildTopBar(),   BorderLayout.NORTH);

        contentLayout = new CardLayout();
        contentArea   = new JPanel(contentLayout);
        contentArea.setBackground(UITheme.BG);
        contentArea.add(buildHomePanel(),          NAV_HOME);
        contentArea.add(buildSearchPanel(),        NAV_SEARCH);
        contentArea.add(buildAppointmentsPanel(),  NAV_APPTS);
        contentArea.add(buildNotificationsPanel(), NAV_NOTIF);
        contentArea.add(buildProfilePanel(),       NAV_PROFILE);
        add(contentArea, BorderLayout.CENTER);

        contentLayout.show(contentArea, NAV_HOME);
    }

    // ── Sidebar ───────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(UITheme.SIDEBAR_BG);
        side.setPreferredSize(new Dimension(230, 0));
        side.setBorder(new EmptyBorder(20,0,20,0));

        // Avatar
        JPanel avatar = buildAvatar();
        avatar.setAlignmentX(CENTER_ALIGNMENT);
        side.add(avatar);
        side.add(Box.createVerticalStrut(6));

        JLabel nameL = new JLabel(patient.getName(), SwingConstants.CENTER);
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameL.setForeground(Color.WHITE);
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        side.add(nameL);

        JLabel roleL = new JLabel("Patient", SwingConstants.CENTER);
        roleL.setFont(UITheme.FONT_SMALL);
        roleL.setForeground(new Color(100,140,200));
        roleL.setAlignmentX(CENTER_ALIGNMENT);
        side.add(roleL);
        side.add(Box.createVerticalStrut(28));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50,70,100));
        sep.setMaximumSize(new Dimension(180, 1));
        sep.setAlignmentX(CENTER_ALIGNMENT);
        side.add(sep);
        side.add(Box.createVerticalStrut(16));

        // Nav items
        addNavItem(side, "\uD83C\uDFE0", "Home",           NAV_HOME);
        addNavItem(side, "\uD83D\uDD0D", "Find Doctors",   NAV_SEARCH);
        addNavItem(side, "\uD83D\uDCC5", "Appointments",   NAV_APPTS);
        addNavItem(side, "\uD83D\uDD14", "Notifications",  NAV_NOTIF);
        addNavItem(side, "\uD83D\uDC64", "My Profile",     NAV_PROFILE);

        side.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("\u2190  Logout");
        logoutBtn.setFont(UITheme.FONT_BODY);
        logoutBtn.setForeground(new Color(200,100,100));
        logoutBtn.setBorderPainted(false); logoutBtn.setContentAreaFilled(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setAlignmentX(CENTER_ALIGNMENT);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) frame.logout();
        });
        side.add(logoutBtn);
        side.add(Box.createVerticalStrut(10));

        return side;
    }

    private JPanel buildAvatar() {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.PRIMARY);
                g2.fillOval(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(64,64));
        p.setMaximumSize(new Dimension(64,64));
        JLabel l = new JLabel(patient.getAvatarInitial());
        l.setFont(new Font("Segoe UI", Font.BOLD, 26));
        l.setForeground(Color.WHITE);
        p.add(l);
        return p;
    }

    private JButton[] navButtons = new JButton[5];
    private int navIdx = 0;

    private void addNavItem(JPanel parent, String icon, String label, String card) {
        JButton btn = new JButton(icon + "  " + label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = card.equals(activeNav);
                if (active) {
                    g2.setColor(new Color(26,115,232,60));
                    g2.fillRoundRect(10,2,getWidth()-20,getHeight()-4,8,8);
                    g2.setColor(UITheme.PRIMARY);
                    g2.fillRoundRect(0,(getHeight()-28)/2,4,28,4,4);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255,255,255,15));
                    g2.fillRoundRect(10,2,getWidth()-20,getHeight()-4,8,8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", card.equals(activeNav) ? Font.BOLD : Font.PLAIN, 13));
        btn.setForeground(card.equals(activeNav) ? Color.WHITE : new Color(160,180,220));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false); btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setBorder(new EmptyBorder(8,20,8,20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            activeNav = card;
            refreshNav();
            if (card.equals(NAV_HOME))   refreshHome();
            if (card.equals(NAV_NOTIF))  refreshNotifications();
            if (card.equals(NAV_APPTS))  refreshAppointments();
            contentLayout.show(contentArea, card);
        });
        if (navIdx < navButtons.length) navButtons[navIdx++] = btn;
        parent.add(btn);
        parent.add(Box.createVerticalStrut(4));
    }

    private void refreshNav() {
        navIdx = 0;
        for (JButton b : navButtons) if (b != null) { b.repaint(); }
    }

    // ── Top bar ───────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, UITheme.BORDER),
            new EmptyBorder(12,30,12,30)));

        JLabel title = new JLabel("Patient Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UITheme.TEXT_PRIMARY);

        // Notification bell
        JPanel bellPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        bellPanel.setOpaque(false);
        long unread = store.countUnread(patient.getUserId());
        JButton bell = new JButton("\uD83D\uDD14");
        bell.setFont(new Font("Dialog", Font.PLAIN, 18));
        bell.setBorderPainted(false); bell.setContentAreaFilled(false); bell.setFocusPainted(false);
        bell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bell.addActionListener(e -> { activeNav=NAV_NOTIF; refreshNav(); refreshNotifications(); contentLayout.show(contentArea, NAV_NOTIF); });

        notifBadge = new JLabel(unread > 0 ? String.valueOf(unread) : "");
        notifBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        notifBadge.setForeground(Color.WHITE);
        notifBadge.setBackground(UITheme.DANGER);
        notifBadge.setOpaque(true);
        notifBadge.setBorder(new EmptyBorder(1,5,1,5));
        notifBadge.setVisible(unread > 0);

        JLabel greeting = new JLabel("Hello, " + patient.getName().split(" ")[0] + "!");
        greeting.setFont(UITheme.FONT_BODY);
        greeting.setForeground(UITheme.TEXT_SECONDARY);

        bellPanel.add(greeting);
        bellPanel.add(bell);
        bellPanel.add(notifBadge);

        bar.add(title, BorderLayout.WEST);
        bar.add(bellPanel, BorderLayout.EAST);
        return bar;
    }

    // ── HOME panel ────────────────────────────────────────────
    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        // Welcome header
        JPanel welcome = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,new Color(0x1A73E8),getWidth(),0,new Color(0x00ACC1));
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.dispose();
            }
        };
        welcome.setOpaque(false);
        welcome.setLayout(new BorderLayout());
        welcome.setBorder(new EmptyBorder(24,28,24,28));
        welcome.setPreferredSize(new Dimension(0,110));

        JLabel wlabel = new JLabel("Good day, " + patient.getName() + "! \uD83D\uDC4B");
        wlabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        wlabel.setForeground(Color.WHITE);
        JLabel wsub = new JLabel("Your health is our priority. Manage your appointments below.");
        wsub.setFont(UITheme.FONT_BODY); wsub.setForeground(new Color(200,230,255));

        JPanel wtext = new JPanel(new GridLayout(2,1,0,4));
        wtext.setOpaque(false); wtext.add(wlabel); wtext.add(wsub);
        welcome.add(wtext, BorderLayout.CENTER);

        JButton bookNow = UITheme.successButton("\u002B  Book Appointment");
        bookNow.setBackground(Color.WHITE);
        bookNow.setForeground(UITheme.PRIMARY);
        bookNow.addActionListener(e -> { activeNav=NAV_SEARCH; refreshNav(); contentLayout.show(contentArea,NAV_SEARCH); });
        welcome.add(bookNow, BorderLayout.EAST);

        // Stats and upcoming list are dynamic and refreshed when Home is shown
        homeStatsPanel = new JPanel(new GridLayout(1,4,16,0));
        homeStatsPanel.setOpaque(false);

        // Recent appointments
        JLabel secTitle = new JLabel("Upcoming Appointments");
        secTitle.setFont(UITheme.FONT_HEADER); secTitle.setForeground(UITheme.TEXT_PRIMARY);

        homeApptList = new JPanel();
        homeApptList.setLayout(new BoxLayout(homeApptList, BoxLayout.Y_AXIS));
        homeApptList.setOpaque(false);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.add(Box.createVerticalStrut(20));
        center.add(homeStatsPanel);
        center.add(Box.createVerticalStrut(28));
        center.add(secTitle);
        center.add(Box.createVerticalStrut(14));
        center.add(homeApptList);

        panel.add(welcome, BorderLayout.NORTH);
        panel.add(center,  BorderLayout.CENTER);

        refreshHome();
        return panel;
    }

    private void refreshHome() {
        if (homeStatsPanel == null || homeApptList == null) return;

        List<Appointment> all = facade.getPatientAppointments(patient.getUserId());
        long upcoming = all.stream()
                .filter(a -> a.isUpcoming() && a.getStatus() != AppointmentStatus.CANCELLED)
                .count();
        long completed = all.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .count();
        long cancelled = all.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.CANCELLED)
                .count();

        homeStatsPanel.removeAll();
        homeStatsPanel.add(UITheme.statCard("\uD83D\uDCC5", String.valueOf(all.size()),  "Total Appointments", UITheme.PRIMARY));
        homeStatsPanel.add(UITheme.statCard("\u23F0",        String.valueOf(upcoming),    "Upcoming",           UITheme.WARN));
        homeStatsPanel.add(UITheme.statCard("\u2705",        String.valueOf(completed),   "Completed",          UITheme.ACCENT));
        homeStatsPanel.add(UITheme.statCard("\u274C",        String.valueOf(cancelled),   "Cancelled",          UITheme.DANGER));
        homeStatsPanel.revalidate();
        homeStatsPanel.repaint();

        homeApptList.removeAll();
        List<Appointment> upcomingList = all.stream()
                .filter(a -> a.isUpcoming() && a.getStatus() != AppointmentStatus.CANCELLED)
                .limit(3)
                .collect(java.util.stream.Collectors.toList());

        if (upcomingList.isEmpty()) {
            JLabel empty = new JLabel("No upcoming appointments.  Click 'Book Appointment' to get started.");
            empty.setFont(UITheme.FONT_BODY);
            empty.setForeground(UITheme.TEXT_MUTED);
            empty.setBorder(new EmptyBorder(20,0,20,0));
            homeApptList.add(empty);
        } else {
            for (Appointment a : upcomingList) homeApptList.add(buildApptRow(a));
        }
        homeApptList.revalidate();
        homeApptList.repaint();
    }

    private JPanel buildApptRow(Appointment a) {
        Doctor doc = (Doctor) store.getUserById(a.getDoctorId());
        JPanel card = UITheme.card(12);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(12,0));
        card.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.ShadowBorder(), new EmptyBorder(14,18,14,18)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));

        // Left: doc name & spec
        JPanel left = new JPanel(new GridLayout(2,1,0,2));
        left.setOpaque(false);
        JLabel dname = new JLabel(doc != null ? doc.getName() : "Unknown Doctor");
        dname.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dname.setForeground(UITheme.TEXT_PRIMARY);
        JLabel spec = new JLabel(doc != null ? doc.getSpecialization().getName() : "");
        spec.setFont(UITheme.FONT_SMALL); spec.setForeground(UITheme.TEXT_SECONDARY);
        left.add(dname); left.add(spec);

        // Center: date/time
        JLabel dateL = new JLabel("\uD83D\uDCC5  " + a.getFormattedDateTime());
        dateL.setFont(UITheme.FONT_BODY); dateL.setForeground(UITheme.TEXT_SECONDARY);

        // Right: status badge + action buttons
        Color sc = statusColor(a.getStatus());
        JLabel badge = UITheme.statusBadge(a.getStatus().getLabel(), sc);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        right.setOpaque(false);
        right.add(badge);

        card.add(left,  BorderLayout.WEST);
        card.add(dateL, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(card, BorderLayout.CENTER);
        wrap.setBorder(new EmptyBorder(0,0,10,0));
        return wrap;
    }

    // ── SEARCH / BOOK panel ───────────────────────────────────
    private JTextField searchField;
    private JComboBox<String> specFilterCombo;
    private JPanel doctorListPanel;

    private JPanel buildSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        JLabel title = new JLabel("Find a Specialist");
        title.setFont(UITheme.FONT_TITLE); title.setForeground(UITheme.TEXT_PRIMARY);

        // Search bar row
        JPanel searchRow = new JPanel(new BorderLayout(10,0));
        searchRow.setOpaque(false);
        searchRow.setBorder(new EmptyBorder(16,0,16,0));

        searchField = new JTextField();
        searchField.setFont(UITheme.FONT_INPUT);
        searchField.setToolTipText("Search by doctor name or specialization...");
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.RoundedBorder(8, UITheme.BORDER), new EmptyBorder(10,14,10,14)));
        searchField.setBackground(Color.WHITE);

        String[] specs = new String[Specialization.values().length + 1];
        specs[0] = "All Specializations";
        for (int i=0; i<Specialization.values().length; i++) specs[i+1] = Specialization.values()[i].getName();
        specFilterCombo = new JComboBox<>(specs);
        specFilterCombo.setFont(UITheme.FONT_INPUT);
        specFilterCombo.setPreferredSize(new Dimension(200,42));

        JButton searchBtn = UITheme.primaryButton("\uD83D\uDD0D  Search");
        searchBtn.addActionListener(e -> refreshDoctorList());
        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { if (e.getKeyCode()==KeyEvent.VK_ENTER) refreshDoctorList(); }
        });

        // Recommendation buttons
        JButton symptomBtn = UITheme.secondaryButton("\uD83E\uDDE0 Symptom-Based");
        JButton historyBtn = UITheme.secondaryButton("\uD83D\uDCC8 History-Based");
        symptomBtn.addActionListener(e -> showRecommendationDialog(new SymptomBasedStrategy()));
        historyBtn.addActionListener(e -> {
            RecommendationContext ctx = new RecommendationContext(new HistoryBasedStrategy());
            showRecommendedDoctors(ctx.getRecommendations(patient), "History-Based Recommendation");
        });

        searchRow.add(searchField,    BorderLayout.CENTER);
        searchRow.add(specFilterCombo,BorderLayout.EAST);

        JPanel topRow = new JPanel(new BorderLayout(10,0));
        topRow.setOpaque(false);
        topRow.add(searchRow, BorderLayout.CENTER);
        JPanel btnRow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        btnRow2.setOpaque(false);
        btnRow2.add(searchBtn);
        btnRow2.add(symptomBtn);
        btnRow2.add(historyBtn);
        topRow.add(btnRow2, BorderLayout.EAST);

        // Doctor list
        doctorListPanel = new JPanel();
        doctorListPanel.setLayout(new BoxLayout(doctorListPanel, BoxLayout.Y_AXIS));
        doctorListPanel.setBackground(UITheme.BG);
        JScrollPane scroll = new JScrollPane(doctorListPanel);
        UITheme.flatScrollPane(scroll);

        panel.add(title,   BorderLayout.NORTH);
        panel.add(topRow,  BorderLayout.CENTER);
        panel.add(scroll,  BorderLayout.SOUTH);

        // Layout fix
        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(title,  BorderLayout.NORTH);
        north.add(topRow, BorderLayout.CENTER);
        panel.removeAll();
        panel.add(north,  BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        refreshDoctorList();
        return panel;
    }

    private void refreshDoctorList() {
        String kw = searchField != null ? searchField.getText().trim() : "";
        Specialization spec = null;
        if (specFilterCombo != null && specFilterCombo.getSelectedIndex() > 0) {
            spec = Specialization.values()[specFilterCombo.getSelectedIndex()-1];
        }
        List<Doctor> docs = facade.searchDoctors(kw, spec);
        showDoctorList(docs);
    }

    private void showRecommendedDoctors(List<Doctor> docs, String label) {
        if (doctorListPanel == null) return;
        doctorListPanel.removeAll();
        JLabel hdr = new JLabel("\uD83D\uDCA1  " + label);
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 14));
        hdr.setForeground(UITheme.PRIMARY);
        hdr.setBorder(new EmptyBorder(8,0,12,0));
        hdr.setAlignmentX(LEFT_ALIGNMENT);
        doctorListPanel.add(hdr);
        for (Doctor d : docs) doctorListPanel.add(buildDoctorCard(d));
        doctorListPanel.revalidate(); doctorListPanel.repaint();
    }

    private void showDoctorList(List<Doctor> docs) {
        if (doctorListPanel == null) return;
        doctorListPanel.removeAll();
        if (docs.isEmpty()) {
            JLabel empty = new JLabel("No doctors found. Try a different search.");
            empty.setFont(UITheme.FONT_BODY); empty.setForeground(UITheme.TEXT_MUTED);
            empty.setBorder(new EmptyBorder(24,0,0,0));
            empty.setAlignmentX(LEFT_ALIGNMENT);
            doctorListPanel.add(empty);
        } else {
            for (Doctor d : docs) doctorListPanel.add(buildDoctorCard(d));
        }
        doctorListPanel.revalidate(); doctorListPanel.repaint();
    }

    private JPanel buildDoctorCard(Doctor doc) {
        JPanel card = UITheme.card(12);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(16,0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        card.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.ShadowBorder(), new EmptyBorder(16,18,16,18)));

        // Avatar
        JPanel av = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,UITheme.PRIMARY,getWidth(),getHeight(),UITheme.SECONDARY);
                g2.setPaint(gp); g2.fillOval(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        av.setOpaque(false); av.setPreferredSize(new Dimension(56,56));
        JLabel avL = new JLabel(doc.getAvatarInitial());
        avL.setFont(new Font("Segoe UI", Font.BOLD, 22)); avL.setForeground(Color.WHITE);
        av.add(avL);

        // Info
        JPanel info = new JPanel(new GridLayout(4,1,0,2));
        info.setOpaque(false);
        JLabel name = new JLabel(doc.getName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14)); name.setForeground(UITheme.TEXT_PRIMARY);
        JLabel spec = new JLabel(doc.getSpecialization().getName() + " — " + doc.getSpecialization().getDescription());
        spec.setFont(UITheme.FONT_SMALL); spec.setForeground(UITheme.TEXT_SECONDARY);
        JLabel qual = new JLabel(doc.getQualification() + "  |  " + doc.getExperienceYears() + " yrs exp");
        qual.setFont(UITheme.FONT_SMALL); qual.setForeground(UITheme.TEXT_MUTED);
        JLabel rating = new JLabel(doc.getRatingStars() + "  " + String.format("%.1f", doc.getRating()) + "  (" + doc.getTotalReviews() + " reviews)");
        rating.setFont(UITheme.FONT_SMALL); rating.setForeground(new Color(0xD97706));
        info.add(name); info.add(spec); info.add(qual); info.add(rating);

        // Right: fee + book
        JPanel right = new JPanel(new GridLayout(2,1,0,6));
        right.setOpaque(false);
        JLabel fee = new JLabel(doc.getFormattedFee(), SwingConstants.RIGHT);
        fee.setFont(new Font("Segoe UI", Font.BOLD, 15)); fee.setForeground(UITheme.PRIMARY);
        JButton book = UITheme.primaryButton("Book Now");
        book.addActionListener(e -> showBookDialog(doc));
        right.add(fee); right.add(book);

        card.add(av,    BorderLayout.WEST);
        card.add(info,  BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false); wrap.setBorder(new EmptyBorder(0,0,10,0));
        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    private void showRecommendationDialog(RecommendationStrategy strategy) {
        JPanel dlg = new JPanel(new BorderLayout(0,10));
        dlg.setBorder(new EmptyBorder(10,10,10,10));
        JLabel lbl = new JLabel("Describe your symptoms:");
        lbl.setFont(UITheme.FONT_LABEL);
        JTextArea ta = new JTextArea(3,30);
        ta.setFont(UITheme.FONT_INPUT);
        ta.setBorder(new EmptyBorder(8,10,8,10));
        ta.setText(patient.getCurrentSymptoms());
        ta.setLineWrap(true); ta.setWrapStyleWord(true);
        dlg.add(lbl,                     BorderLayout.NORTH);
        dlg.add(new JScrollPane(ta),     BorderLayout.CENTER);

        int res = JOptionPane.showConfirmDialog(frame, dlg,
            "Symptom-Based Recommendation", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            patient.setCurrentSymptoms(ta.getText().trim());
            RecommendationContext ctx = new RecommendationContext(strategy);
            showRecommendedDoctors(ctx.getRecommendations(patient), "Recommended for: \"" + ta.getText().trim() + "\"");
            activeNav = NAV_SEARCH; refreshNav();
            contentLayout.show(contentArea, NAV_SEARCH);
        }
    }

    private void showBookDialog(Doctor doc) {
        JDialog dlg = new JDialog(frame, "Book Appointment with " + doc.getName(), true);
        dlg.setSize(480,420);
        dlg.setLocationRelativeTo(frame);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(24,28,24,28));
        form.setBackground(Color.WHITE);

        JLabel title = new JLabel("Book Appointment");
        title.setFont(UITheme.FONT_HEADER); title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(LEFT_ALIGNMENT);
        JLabel docName = new JLabel("with " + doc.getName() + " (" + doc.getSpecialization().getName() + ")");
        docName.setFont(UITheme.FONT_BODY); docName.setForeground(UITheme.TEXT_SECONDARY);
        docName.setAlignmentX(LEFT_ALIGNMENT);

        // Date picker
        JLabel dateL = UITheme.sectionLabel("Select Date");
        dateL.setAlignmentX(LEFT_ALIGNMENT);
        String[] dates = new String[14];
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy");
        for (int i=0; i<14; i++) dates[i] = LocalDate.now().plusDays(i+1).format(dtf);
        JComboBox<String> dateCb = new JComboBox<>(dates);
        dateCb.setFont(UITheme.FONT_INPUT); dateCb.setAlignmentX(LEFT_ALIGNMENT);
        dateCb.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));

        // Time slots
        JLabel timeL = UITheme.sectionLabel("Select Time Slot");
        timeL.setAlignmentX(LEFT_ALIGNMENT);
        String[] times = {"09:00 AM","10:00 AM","11:00 AM","02:00 PM","03:00 PM","04:00 PM"};
        JComboBox<String> timeCb = new JComboBox<>(times);
        timeCb.setFont(UITheme.FONT_INPUT); timeCb.setAlignmentX(LEFT_ALIGNMENT);
        timeCb.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));

        // Symptoms
        JLabel sympL = UITheme.sectionLabel("Describe Symptoms / Reason for Visit");
        sympL.setAlignmentX(LEFT_ALIGNMENT);
        JTextArea sympTa = new JTextArea(3,20);
        sympTa.setFont(UITheme.FONT_INPUT); sympTa.setLineWrap(true); sympTa.setWrapStyleWord(true);
        sympTa.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.RoundedBorder(8,UITheme.BORDER), new EmptyBorder(8,10,8,10)));
        JScrollPane sympScroll = new JScrollPane(sympTa);
        sympScroll.setAlignmentX(LEFT_ALIGNMENT);
        sympScroll.setBorder(BorderFactory.createEmptyBorder());
        sympScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));

        JLabel errL = new JLabel(" ");
        errL.setFont(UITheme.FONT_SMALL); errL.setForeground(UITheme.DANGER);
        errL.setAlignmentX(LEFT_ALIGNMENT);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setOpaque(false);
        JButton cancel = UITheme.secondaryButton("Cancel");
        JButton confirm = UITheme.primaryButton("Confirm Booking");
        cancel.addActionListener(e -> dlg.dispose());
        confirm.addActionListener(e -> {
            String symp = sympTa.getText().trim();
            if (symp.isEmpty()) {
                errL.setText("Please describe your symptoms/reason.");
                return;
            }

            try {
                int dayIdx = dateCb.getSelectedIndex();
                LocalDate date = LocalDate.now().plusDays(dayIdx + 1);
                String tStr = (String) timeCb.getSelectedItem();

                java.time.format.DateTimeFormatter tf2 =
                    java.time.format.DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);
                LocalTime time = LocalTime.parse(tStr, tf2);

                int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Confirm appointment with " + doc.getName() + " on " + date + " at " + tStr + "?",
                    "Confirm Appointment",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (choice != JOptionPane.YES_OPTION) return;

                Appointment appt = facade.bookAppointment(
                    patient.getUserId(), doc.getUserId(), date, time, symp
                );

                if (appt == null) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "Unable to book appointment. Please try another slot.",
                        "Booking Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                dlg.dispose();
                JOptionPane.showMessageDialog(
                    frame,
                    "Appointment booked successfully.\n\nDate & Time: " + appt.getFormattedDateTime()
                        + "\nStatus: " + appt.getStatus().getLabel(),
                    "Booking Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );

                updateNotifBadge();
                refreshHome();
                refreshAppointments();
                activeNav = NAV_APPTS;
                refreshNav();
                contentLayout.show(contentArea, NAV_APPTS);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Booking failed: " + ex.getMessage(),
                    "Booking Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        btnRow.add(cancel); btnRow.add(confirm);

        form.add(title); form.add(Box.createVerticalStrut(2)); form.add(docName);
        form.add(Box.createVerticalStrut(20));
        form.add(dateL); form.add(Box.createVerticalStrut(5)); form.add(dateCb);
        form.add(Box.createVerticalStrut(14));
        form.add(timeL); form.add(Box.createVerticalStrut(5)); form.add(timeCb);
        form.add(Box.createVerticalStrut(14));
        form.add(sympL); form.add(Box.createVerticalStrut(5)); form.add(sympScroll);
        form.add(Box.createVerticalStrut(6)); form.add(errL);
        form.add(btnRow);

        dlg.add(form, BorderLayout.CENTER);
        dlg.setVisible(true);
    }

    // ── APPOINTMENTS panel ────────────────────────────────────
    private JPanel apptListPanel;

    private JPanel buildAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        JLabel title = new JLabel("My Appointments");
        title.setFont(UITheme.FONT_TITLE); title.setForeground(UITheme.TEXT_PRIMARY);

        apptListPanel = new JPanel();
        apptListPanel.setLayout(new BoxLayout(apptListPanel, BoxLayout.Y_AXIS));
        apptListPanel.setBackground(UITheme.BG);

        JScrollPane scroll = new JScrollPane(apptListPanel);
        UITheme.flatScrollPane(scroll);

        panel.add(title,  BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        refreshAppointments();
        return panel;
    }

    private void refreshAppointments() {
        if (apptListPanel == null) return;
        apptListPanel.removeAll();
        apptListPanel.add(Box.createVerticalStrut(16));

        List<Appointment> appts = facade.getPatientAppointments(patient.getUserId());
        if (appts.isEmpty()) {
            JLabel empty = new JLabel("You have no appointments yet.");
            empty.setFont(UITheme.FONT_BODY); empty.setForeground(UITheme.TEXT_MUTED);
            apptListPanel.add(empty);
        } else {
            for (Appointment a : appts) apptListPanel.add(buildFullApptCard(a));
        }
        apptListPanel.revalidate(); apptListPanel.repaint();
    }

    private JPanel buildFullApptCard(Appointment a) {
        Doctor doc = (Doctor) store.getUserById(a.getDoctorId());
        JPanel card = UITheme.card(12);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(14,0));
        card.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.ShadowBorder(), new EmptyBorder(16,18,16,18)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        // Left colored bar
        JPanel bar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(statusColor(a.getStatus())); g2.fillRoundRect(0,0,6,getHeight(),4,4);
                g2.dispose();
            }
        };
        bar.setOpaque(false); bar.setPreferredSize(new Dimension(6,0));

        // Info
        JPanel info = new JPanel(new GridLayout(4,1,0,3));
        info.setOpaque(false);
        JLabel dname = new JLabel(doc!=null?doc.getName():"Unknown");
        dname.setFont(new Font("Segoe UI", Font.BOLD, 14)); dname.setForeground(UITheme.TEXT_PRIMARY);
        JLabel spec = new JLabel(doc!=null ? doc.getSpecialization().getName() : "");
        spec.setFont(UITheme.FONT_SMALL); spec.setForeground(UITheme.TEXT_SECONDARY);
        JLabel dt = new JLabel("\uD83D\uDCC5  " + a.getFormattedDateTime());
        dt.setFont(UITheme.FONT_BODY); dt.setForeground(UITheme.TEXT_SECONDARY);
        JLabel symp = new JLabel("Symptoms: " + a.getSymptoms());
        symp.setFont(UITheme.FONT_SMALL); symp.setForeground(UITheme.TEXT_MUTED);
        info.add(dname); info.add(spec); info.add(dt); info.add(symp);

        // Right: badge + actions
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        JLabel badge = UITheme.statusBadge(a.getStatus().getLabel(), statusColor(a.getStatus()));
        badge.setAlignmentX(RIGHT_ALIGNMENT);
        right.add(badge);
        right.add(Box.createVerticalStrut(8));

        if (a.isUpcoming() && a.getStatus() != AppointmentStatus.CANCELLED && a.getStatus() != AppointmentStatus.COMPLETED) {
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0));
            actions.setOpaque(false);
            JButton reschedule = UITheme.warnButton("Reschedule");
            JButton cancel     = UITheme.dangerButton("Cancel");
            reschedule.addActionListener(e -> showRescheduleDialog(a));
            cancel.addActionListener(e -> {
                int c = JOptionPane.showConfirmDialog(frame,"Cancel this appointment?","Confirm",JOptionPane.YES_NO_OPTION);
                if (c == JOptionPane.YES_OPTION) {
                    facade.cancelAppointment(a.getAppointmentId(), patient.getUserId());
                    updateNotifBadge();
                    refreshHome();
                    refreshAppointments();
                }
            });
            actions.add(reschedule); actions.add(cancel);
            right.add(actions);
        }

        card.add(bar,   BorderLayout.WEST);
        card.add(info,  BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false); wrap.setBorder(new EmptyBorder(0,0,10,0));
        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    private void showRescheduleDialog(Appointment a) {
        String[] dates = new String[14];
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy");
        for (int i=0; i<14; i++) dates[i] = LocalDate.now().plusDays(i+1).format(dtf);
        JComboBox<String> dateCb = new JComboBox<>(dates);
        String[] times = {"09:00 AM","10:00 AM","11:00 AM","02:00 PM","03:00 PM","04:00 PM"};
        JComboBox<String> timeCb = new JComboBox<>(times);

        JPanel p = new JPanel(new GridLayout(4,1,0,8));
        p.add(new JLabel("New Date:")); p.add(dateCb);
        p.add(new JLabel("New Time:")); p.add(timeCb);

        int res = JOptionPane.showConfirmDialog(frame, p, "Reschedule Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            LocalDate date = LocalDate.now().plusDays(dateCb.getSelectedIndex()+1);
            String tStr = (String) timeCb.getSelectedItem();
            java.time.format.DateTimeFormatter tf2 =
                java.time.format.DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);
            LocalTime time = LocalTime.parse(tStr, tf2);
            facade.rescheduleAppointment(a.getAppointmentId(), date, time, patient.getUserId());
            updateNotifBadge();
            refreshHome();
            refreshAppointments();
            JOptionPane.showMessageDialog(frame,"Appointment rescheduled successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ── NOTIFICATIONS panel ───────────────────────────────────
    private JPanel notifListPanel;

    private JPanel buildNotificationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        JLabel title = new JLabel("Notifications");
        title.setFont(UITheme.FONT_TITLE); title.setForeground(UITheme.TEXT_PRIMARY);

        notifListPanel = new JPanel();
        notifListPanel.setLayout(new BoxLayout(notifListPanel, BoxLayout.Y_AXIS));
        notifListPanel.setBackground(UITheme.BG);

        JScrollPane scroll = new JScrollPane(notifListPanel);
        UITheme.flatScrollPane(scroll);

        JButton clearBtn = UITheme.secondaryButton("Mark all as read");
        clearBtn.addActionListener(e -> {
            store.getNotificationsForUser(patient.getUserId()).forEach(n -> n.setRead(true));
            updateNotifBadge();
            refreshNotifications();
        });

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title,    BorderLayout.WEST);
        top.add(clearBtn, BorderLayout.EAST);

        panel.add(top,    BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshNotifications() {
        if (notifListPanel == null) return;
        notifListPanel.removeAll();
        notifListPanel.add(Box.createVerticalStrut(16));
        List<Notification> notifs = store.getNotificationsForUser(patient.getUserId());
        if (notifs.isEmpty()) {
            JLabel empty = new JLabel("No notifications yet.");
            empty.setFont(UITheme.FONT_BODY); empty.setForeground(UITheme.TEXT_MUTED);
            notifListPanel.add(empty);
        } else {
            for (Notification n : notifs) {
                n.setRead(true);
                notifListPanel.add(buildNotifCard(n));
            }
        }
        updateNotifBadge();
        notifListPanel.revalidate(); notifListPanel.repaint();
    }

    private JPanel buildNotifCard(Notification n) {
        JPanel card = UITheme.card(10);
        card.setBackground(n.isRead() ? Color.WHITE : new Color(0xEFF6FF));
        card.setLayout(new BorderLayout(10,0));
        card.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.ShadowBorder(), new EmptyBorder(12,16,12,16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        String icon = n.getChannel().equals("EMAIL") ? "\uD83D\uDCE7" : "\uD83D\uDD14";
        JLabel iconL = new JLabel(icon);
        iconL.setFont(new Font("Dialog", Font.PLAIN, 20));

        JPanel info = new JPanel(new GridLayout(2,1,0,3));
        info.setOpaque(false);
        JLabel tit = new JLabel(n.getTitle());
        tit.setFont(new Font("Segoe UI", Font.BOLD, 13)); tit.setForeground(UITheme.TEXT_PRIMARY);
        JLabel msg = new JLabel(n.getMessage().split("\n")[0]);
        msg.setFont(UITheme.FONT_SMALL); msg.setForeground(UITheme.TEXT_SECONDARY);
        info.add(tit); info.add(msg);

        JLabel time = new JLabel(n.getFormattedTime());
        time.setFont(UITheme.FONT_SMALL); time.setForeground(UITheme.TEXT_MUTED);

        card.add(iconL, BorderLayout.WEST);
        card.add(info,  BorderLayout.CENTER);
        card.add(time,  BorderLayout.EAST);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false); wrap.setBorder(new EmptyBorder(0,0,8,0));
        wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    // ── PROFILE panel ─────────────────────────────────────────
    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        JLabel title = new JLabel("My Profile");
        title.setFont(UITheme.FONT_TITLE); title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0,0,20,0));

        JPanel form = UITheme.card(14);
        form.setBackground(Color.WHITE);
        form.setLayout(new GridLayout(0,2,20,14));
        form.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.ShadowBorder(), new EmptyBorder(28,28,28,28)));

        addProfileRow(form, "Full Name",     patient.getName());
        addProfileRow(form, "Email",         patient.getEmail());
        addProfileRow(form, "Phone",         patient.getPhone());
        addProfileRow(form, "Date of Birth", patient.getDateOfBirth());
        addProfileRow(form, "Blood Type",    patient.getBloodType());
        addProfileRow(form, "Allergies",     patient.getAllergies());
        addProfileRow(form, "Role",          patient.getRole());
        addProfileRow(form, "Member Since",  patient.getFormattedJoinDate());

        JPanel center = new JPanel(new BorderLayout(0,20));
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);

        panel.add(title,  BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private void addProfileRow(JPanel p, String label, String value) {
        JLabel l = new JLabel(label);
        l.setFont(UITheme.FONT_LABEL); l.setForeground(UITheme.TEXT_SECONDARY);
        JLabel v = new JLabel(value != null && !value.isEmpty() ? value : "—");
        v.setFont(new Font("Segoe UI", Font.BOLD, 13)); v.setForeground(UITheme.TEXT_PRIMARY);
        p.add(l); p.add(v);
    }

    // ── Helpers ───────────────────────────────────────────────
    private void updateNotifBadge() {
        long unread = store.countUnread(patient.getUserId());
        if (notifBadge != null) {
            notifBadge.setText(unread > 0 ? String.valueOf(unread) : "");
            notifBadge.setVisible(unread > 0);
        }
    }

    private Color statusColor(AppointmentStatus s) {
        switch (s) {
            case CONFIRMED:   return UITheme.ACCENT;
            case CANCELLED:   return UITheme.DANGER;
            case COMPLETED:   return UITheme.PRIMARY;
            case RESCHEDULED: return UITheme.PURPLE;
            default:          return UITheme.WARN;
        }
    }
}
