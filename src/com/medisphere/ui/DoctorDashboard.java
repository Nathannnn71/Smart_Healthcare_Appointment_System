package com.medisphere.ui;

import com.medisphere.model.*;
import com.medisphere.pattern.HealthcareFacade;
import com.medisphere.ui.components.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class DoctorDashboard extends JFrame {
    private final Doctor doctor;
    private final HealthcareFacade facade = HealthcareFacade.getInstance();
    private JPanel contentArea;
    private static final String[] NAV_ICONS   = {"🏠", "📅", "👥", "👤"};
    private static final String[] NAV_LABELS  = {"Dashboard", "My Schedule", "Patients", "Profile"};

    public DoctorDashboard(Doctor doctor) {
        this.doctor = doctor;
        setTitle("MediSphere – Doctor Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        buildUI();
        showPanel(0);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_PAGE);
        setContentPane(root);
        root.add(buildSidebar(), BorderLayout.WEST);
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BG_PAGE);
        main.add(buildTopBar(), BorderLayout.NORTH);
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UITheme.BG_PAGE);
        contentArea.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        main.add(contentArea, BorderLayout.CENTER);
        root.add(main, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sb = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(0x1B3A5C), 0, getHeight(), new Color(0x0D2137)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setPreferredSize(new Dimension(UITheme.SIDEBAR_W, 0));

        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 20));
        logo.setOpaque(false);
        JLabel logoIcon = new JLabel("⚕"); logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoIcon.setForeground(UITheme.ACCENT_LIGHT);
        JPanel lt = new JPanel(); lt.setOpaque(false); lt.setLayout(new BoxLayout(lt, BoxLayout.Y_AXIS));
        JLabel ln = new JLabel("MediSphere"); ln.setFont(new Font("Segoe UI", Font.BOLD, 18)); ln.setForeground(Color.WHITE);
        JLabel ls = new JLabel("Doctor Portal"); ls.setFont(UITheme.FONT_SMALL); ls.setForeground(new Color(255,255,255,120));
        lt.add(ln); lt.add(ls);
        logo.add(logoIcon); logo.add(lt);
        sb.add(logo);
        sb.add(navDivider());

        for (int i = 0; i < NAV_LABELS.length; i++) {
            final int idx = i;
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
            item.setOpaque(false);
            item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            JLabel ic = new JLabel(NAV_ICONS[i]); ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16)); ic.setForeground(Color.WHITE);
            JLabel lbl = new JLabel(NAV_LABELS[i]); lbl.setFont(UITheme.FONT_SIDEBAR_ITEM); lbl.setForeground(new Color(255,255,255,180));
            item.add(ic); item.add(lbl);
            item.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { showPanel(idx); }
                @Override public void mouseEntered(MouseEvent e) { item.setBackground(UITheme.SIDEBAR_HOVER); }
                @Override public void mouseExited(MouseEvent e)  { item.setBackground(new Color(0,0,0,0)); }
            });
            sb.add(item);
        }

        sb.add(Box.createVerticalGlue());
        sb.add(navDivider());

        JPanel uInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        uInfo.setOpaque(false);
        uInfo.add(buildAvatar(doctor.getName(), UITheme.ACCENT, 36));
        JPanel np = new JPanel(); np.setOpaque(false); np.setLayout(new BoxLayout(np, BoxLayout.Y_AXIS));
        JLabel un = new JLabel("Dr. " + (doctor.getName().length() > 14 ? doctor.getName().substring(0, 12) + "…" : doctor.getName()));
        un.setFont(UITheme.FONT_BOLD); un.setForeground(Color.WHITE);
        JLabel ur = new JLabel(doctor.getSpecialty().getDisplayName());
        ur.setFont(UITheme.FONT_SMALL); ur.setForeground(new Color(255,255,255,140));
        np.add(un); np.add(ur); uInfo.add(np);
        sb.add(uInfo);

        JPanel lRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        lRow.setOpaque(false);
        RoundedButton lb = new RoundedButton("🚪  Logout", RoundedButton.Style.OUTLINE);
        lb.setFont(UITheme.FONT_SMALL); lb.setMaximumSize(new Dimension(180, 32));
        lb.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        lRow.add(lb); sb.add(lRow);
        sb.add(Box.createVerticalStrut(10));
        return sb;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setPreferredSize(new Dimension(0, UITheme.TOPBAR_H));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(0, 24, 0, 24)));
        JLabel title = new JLabel("Doctor Dashboard");
        title.setFont(UITheme.FONT_H2); title.setForeground(UITheme.TEXT_DARK);
        bar.add(title, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        right.setOpaque(false);
        JLabel today = new JLabel("📅 " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")));
        today.setFont(UITheme.FONT_BODY); today.setForeground(UITheme.TEXT_MED);
        right.add(today);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private void showPanel(int idx) {
        contentArea.removeAll();
        JPanel p;
        switch (idx) {
            case 0: p = buildHome(); break;
            case 1: p = buildSchedule(); break;
            case 2: p = buildPatients(); break;
            default: p = buildProfile(); break;
        }
        contentArea.add(p, BorderLayout.CENTER);
        contentArea.revalidate(); contentArea.repaint();
    }

    private JPanel buildHome() {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); p.setOpaque(false);

        List<Appointment> allApts = facade.getDoctorAppointments(doctor.getUserId());
        long today = allApts.stream().filter(a -> a.getDate().isEqual(LocalDate.now())).count();
        long upcoming = allApts.stream().filter(Appointment::isUpcoming).count();
        long total = allApts.size();

        p.add(sectionHeader("Overview", "Dr. " + doctor.getName() + " · " + doctor.getSpecialty().getDisplayName()));
        p.add(Box.createVerticalStrut(16));

        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(statCard("📅", String.valueOf(today), "Today", UITheme.PRIMARY, new Color(0xE3F2FD)));
        stats.add(statCard("⏳", String.valueOf(upcoming), "Upcoming", UITheme.WARNING, new Color(0xFFF3E0)));
        stats.add(statCard("✓", String.valueOf(total), "Total Apts", UITheme.SUCCESS, new Color(0xE8F5E9)));
        stats.add(statCard("⭐", String.format("%.1f", doctor.getRating()), "Rating", UITheme.ACCENT, new Color(0xE0F7FA)));
        p.add(stats);
        p.add(Box.createVerticalStrut(24));

        p.add(sectionHeader("Today's Appointments", null));
        p.add(Box.createVerticalStrut(12));

        List<Appointment> todayApts = allApts.stream()
                .filter(a -> a.getDate().isEqual(LocalDate.now()))
                .sorted((a, b) -> a.getTime().compareTo(b.getTime()))
                .collect(Collectors.toList());

        if (todayApts.isEmpty()) {
            p.add(emptyState("📅", "No Appointments Today", "Your schedule is clear for today."));
        } else {
            for (Appointment a : todayApts) {
                p.add(buildAptRow(a)); p.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(p);
        scroll.setBorder(null); scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        JPanel w = new JPanel(new BorderLayout()); w.setOpaque(false); w.add(scroll); return w;
    }

    private JPanel buildSchedule() {
        JPanel p = new JPanel(new BorderLayout(0, 16)); p.setOpaque(false);
        p.add(sectionHeader("Appointment Schedule", "All your upcoming and past appointments"), BorderLayout.NORTH);

        List<Appointment> apts = facade.getDoctorAppointments(doctor.getUserId());

        // Build table
        String[] cols = {"ID", "Patient", "Date", "Time", "Specialty", "Reason", "Status"};
        Object[][] data = new Object[apts.size()][cols.length];
        for (int i = 0; i < apts.size(); i++) {
            Appointment a = apts.get(i);
            data[i] = new Object[]{a.getAppointmentId(), a.getPatientName(),
                    a.getFormattedDate(), a.getFormattedTime(),
                    a.getSpecialty().getDisplayName(), a.getReason(),
                    a.getStatus().getDisplayName()};
        }

        JTable table = new JTable(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(36);
        table.getTableHeader().setFont(UITheme.FONT_BOLD);
        table.getTableHeader().setBackground(UITheme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(UITheme.BORDER);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionBackground(new Color(0xE3F2FD));
        table.setSelectionForeground(UITheme.TEXT_DARK);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildPatients() {
        JPanel p = new JPanel(new BorderLayout(0, 16)); p.setOpaque(false);
        p.add(sectionHeader("My Patients", "Patients who have booked appointments with you"), BorderLayout.NORTH);

        List<Appointment> apts = facade.getDoctorAppointments(doctor.getUserId());
        Map<String, Appointment> seen = new LinkedHashMap<>();
        for (Appointment a : apts) seen.putIfAbsent(a.getPatientId(), a);

        JPanel list = new JPanel(); list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS)); list.setOpaque(false);
        if (seen.isEmpty()) {
            list.add(emptyState("👥", "No Patients Yet", "Patients will appear here after they book with you."));
        } else {
            for (Appointment a : seen.values()) {
                RoundedPanel card = new RoundedPanel(10, Color.WHITE, true);
                card.setLayout(new BorderLayout(12, 0));
                card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

                JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                left.setOpaque(false);
                left.add(buildAvatar(a.getPatientName(), UITheme.PRIMARY_LIGHT, 40));
                JPanel info = new JPanel(); info.setOpaque(false); info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                JLabel nm = new JLabel(a.getPatientName()); nm.setFont(UITheme.FONT_H3); nm.setForeground(UITheme.TEXT_DARK);
                JLabel dt = new JLabel("Last visit: " + a.getFormattedDate() + "  ·  Reason: " + a.getReason());
                dt.setFont(UITheme.FONT_SMALL); dt.setForeground(UITheme.TEXT_LIGHT);
                info.add(nm); info.add(dt); left.add(info);
                card.add(left, BorderLayout.CENTER);

                list.add(card); list.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null); scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildProfile() {
        JPanel p = new JPanel(new BorderLayout(0, 16)); p.setOpaque(false);
        p.add(sectionHeader("My Profile", "Your professional information"), BorderLayout.NORTH);

        RoundedPanel card = new RoundedPanel(12, Color.WHITE, true);
        card.setBorder(BorderFactory.createEmptyBorder(30, 36, 30, 36));
        card.setLayout(new BorderLayout(20, 0));

        JPanel left = new JPanel(); left.setOpaque(false); left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setPreferredSize(new Dimension(180, 0));
        JPanel av = buildAvatar(doctor.getName(), UITheme.fromHex(doctor.getSpecialty().getColor()), 80);
        av.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(Box.createVerticalStrut(10));
        left.add(av);
        left.add(Box.createVerticalStrut(10));
        JLabel stars = new JLabel(doctor.getStarRating()); stars.setFont(UITheme.FONT_SMALL);
        stars.setForeground(new Color(0xFFA000)); stars.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(stars);
        card.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new GridBagLayout());
        right.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        String[][] fields = {
                {"Name", "Dr. " + doctor.getName()},
                {"Specialty", doctor.getSpecialty().getDisplayName()},
                {"Qualification", doctor.getQualification()},
                {"Experience", doctor.getExperienceYears() + " years"},
                {"Hospital", doctor.getHospitalAffiliation()},
                {"Consultation Hours", doctor.getConsultationHours()},
                {"Fee", doctor.getConsultationFee()},
                {"Email", doctor.getEmail()},
                {"Phone", doctor.getPhone()},
                {"Doctor ID", doctor.getUserId()}
        };
        for (int i = 0; i < fields.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            JLabel l = new JLabel(fields[i][0] + ":"); l.setFont(UITheme.FONT_BOLD); l.setForeground(UITheme.TEXT_MED);
            right.add(l, gbc);
            gbc.gridx = 1;
            JLabel v = new JLabel(fields[i][1]); v.setFont(UITheme.FONT_BODY); v.setForeground(UITheme.TEXT_DARK);
            right.add(v, gbc);
        }
        card.add(right, BorderLayout.CENTER);

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT)); wrap.setOpaque(false); wrap.add(card);
        p.add(wrap, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildAptRow(Appointment a) {
        Color sc = UITheme.fromHex(a.getStatus().getHexColor());
        RoundedPanel card = new RoundedPanel(8, Color.WHITE, true);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));

        JPanel info = new JPanel(); info.setOpaque(false); info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel top = new JLabel("🕐 " + a.getFormattedTime() + "  ·  " + a.getPatientName());
        top.setFont(UITheme.FONT_H3); top.setForeground(UITheme.TEXT_DARK);
        JLabel bot = new JLabel("Reason: " + a.getReason() + "  ·  " + a.getAppointmentId());
        bot.setFont(UITheme.FONT_SMALL); bot.setForeground(UITheme.TEXT_LIGHT);
        info.add(top); info.add(Box.createVerticalStrut(3)); info.add(bot);
        card.add(info, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JLabel sl = new JLabel(a.getStatus().getDisplayName());
        sl.setFont(UITheme.FONT_SMALL); sl.setForeground(sc);
        sl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(sc, 1, true),
                BorderFactory.createEmptyBorder(3, 9, 3, 9)));

        RoundedButton completeBtn = new RoundedButton("Mark Complete", RoundedButton.Style.SUCCESS);
        completeBtn.setFont(UITheme.FONT_SMALL); completeBtn.setPreferredSize(new Dimension(130, 28));
        completeBtn.addActionListener(e -> {
            a.setStatus(AppointmentStatus.COMPLETED);
            showPanel(0);
        });
        right.add(sl);
        if (a.getStatus() == AppointmentStatus.CONFIRMED) right.add(completeBtn);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    // ── Helpers ──
    private JPanel buildAvatar(String name, Color color, int size) {
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
                        name.split(" ").length > 1 ? "" + name.split(" ")[0].charAt(0) + name.split(" ")[1].charAt(0) : "" + name.charAt(0);
                g2.setFont(new Font("Segoe UI", Font.BOLD, size / 3));
                g2.setColor(color);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(init.toUpperCase(), (size - fm.stringWidth(init)) / 2, (size + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        av.setOpaque(false); av.setPreferredSize(new Dimension(size, size));
        av.setMinimumSize(new Dimension(size, size)); av.setMaximumSize(new Dimension(size, size));
        return av;
    }

    private JPanel statCard(String icon, String val, String lbl, Color color, Color bg) {
        RoundedPanel c = new RoundedPanel(12, bg, false);
        c.setLayout(new BorderLayout());
        c.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel ic = new JLabel(icon); ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JPanel tp = new JPanel(); tp.setOpaque(false); tp.setLayout(new BoxLayout(tp, BoxLayout.Y_AXIS));
        JLabel v = new JLabel(val); v.setFont(new Font("Segoe UI", Font.BOLD, 28)); v.setForeground(color);
        JLabel l2 = new JLabel(lbl); l2.setFont(UITheme.FONT_SMALL); l2.setForeground(UITheme.TEXT_LIGHT);
        tp.add(v); tp.add(l2); c.add(ic, BorderLayout.WEST); c.add(tp, BorderLayout.CENTER); return c;
    }

    private JPanel sectionHeader(String title, String sub) {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); p.setOpaque(false);
        JLabel t = new JLabel(title); t.setFont(UITheme.FONT_H1); t.setForeground(UITheme.TEXT_DARK); p.add(t);
        if (sub != null) { JLabel s = new JLabel(sub); s.setFont(UITheme.FONT_BODY); s.setForeground(UITheme.TEXT_LIGHT); p.add(s); }
        return p;
    }

    private JPanel emptyState(String icon, String title, String desc) {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        JLabel ic = new JLabel(icon); ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48)); ic.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel t = new JLabel(title); t.setFont(UITheme.FONT_H2); t.setForeground(UITheme.TEXT_MED); t.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel d = new JLabel(desc); d.setFont(UITheme.FONT_BODY); d.setForeground(UITheme.TEXT_LIGHT); d.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(ic); p.add(Box.createVerticalStrut(8)); p.add(t); p.add(Box.createVerticalStrut(4)); p.add(d);
        return p;
    }

    private JPanel navDivider() {
        JPanel div = new JPanel(); div.setOpaque(false);
        div.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        div.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 20)));
        return div;
    }
}
