package com.medisphere.ui;

import com.medisphere.model.*;
import com.medisphere.pattern.*;
import com.medisphere.ui.components.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private final Administrator admin;
    private final HealthcareFacade facade = HealthcareFacade.getInstance();
    private JPanel contentArea;
    private static final String[] NAV_ICONS  = {"🏠", "👥", "📅", "👨‍⚕️", "📊"};
    private static final String[] NAV_LABELS = {"Overview", "Manage Users", "All Appointments", "Doctors", "Reports"};

    public AdminDashboard(Administrator admin) {
        this.admin = admin;
        setTitle("MediSphere – Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 740);
        setMinimumSize(new Dimension(960, 620));
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
                g2.setPaint(new GradientPaint(0, 0, new Color(0x1A237E), 0, getHeight(), new Color(0x0D1B3E)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setPreferredSize(new Dimension(UITheme.SIDEBAR_W, 0));

        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 20)); logo.setOpaque(false);
        JLabel li = new JLabel("⚕"); li.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28)); li.setForeground(UITheme.ACCENT_LIGHT);
        JPanel lt = new JPanel(); lt.setOpaque(false); lt.setLayout(new BoxLayout(lt, BoxLayout.Y_AXIS));
        JLabel ln = new JLabel("MediSphere"); ln.setFont(new Font("Segoe UI", Font.BOLD, 18)); ln.setForeground(Color.WHITE);
        JLabel ls = new JLabel("Admin Panel"); ls.setFont(UITheme.FONT_SMALL); ls.setForeground(new Color(255,255,255,120));
        lt.add(ln); lt.add(ls); logo.add(li); logo.add(lt);
        sb.add(logo); sb.add(divider());

        for (int i = 0; i < NAV_LABELS.length; i++) {
            final int idx = i;
            JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
            item.setOpaque(false); item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            JLabel ic = new JLabel(NAV_ICONS[i]); ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16)); ic.setForeground(Color.WHITE);
            JLabel lb = new JLabel(NAV_LABELS[i]); lb.setFont(UITheme.FONT_SIDEBAR_ITEM); lb.setForeground(new Color(255,255,255,180));
            item.add(ic); item.add(lb);
            item.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { showPanel(idx); }
                @Override public void mouseEntered(MouseEvent e) { item.setBackground(UITheme.SIDEBAR_HOVER); }
                @Override public void mouseExited(MouseEvent e)  { item.setBackground(new Color(0,0,0,0)); }
            });
            sb.add(item);
        }
        sb.add(Box.createVerticalGlue());
        sb.add(divider());
        JPanel uInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12)); uInfo.setOpaque(false);
        JPanel av = buildAvatar(admin.getName(), new Color(0xFFD54F), 36);
        JPanel np = new JPanel(); np.setOpaque(false); np.setLayout(new BoxLayout(np, BoxLayout.Y_AXIS));
        JLabel un = new JLabel(admin.getName().length() > 14 ? admin.getName().substring(0,12)+"…" : admin.getName());
        un.setFont(UITheme.FONT_BOLD); un.setForeground(Color.WHITE);
        JLabel ur = new JLabel("Administrator"); ur.setFont(UITheme.FONT_SMALL); ur.setForeground(new Color(255,255,255,140));
        np.add(un); np.add(ur); uInfo.add(av); uInfo.add(np);
        sb.add(uInfo);
        JPanel lr = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8)); lr.setOpaque(false);
        RoundedButton lb2 = new RoundedButton("🚪  Logout", RoundedButton.Style.OUTLINE);
        lb2.setFont(UITheme.FONT_SMALL); lb2.setMaximumSize(new Dimension(180,32));
        lb2.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        lr.add(lb2); sb.add(lr); sb.add(Box.createVerticalStrut(10));
        return sb;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE); bar.setPreferredSize(new Dimension(0, UITheme.TOPBAR_H));
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0,UITheme.BORDER),
                BorderFactory.createEmptyBorder(0,24,0,24)));
        JLabel t = new JLabel("System Administration");
        t.setFont(UITheme.FONT_H2); t.setForeground(UITheme.TEXT_DARK);
        bar.add(t, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10)); right.setOpaque(false);
        JLabel badge = new JLabel("⚙ " + admin.getAdminLevel() + " · " + admin.getDepartment());
        badge.setFont(UITheme.FONT_SMALL); badge.setForeground(UITheme.TEXT_MED);
        right.add(badge); bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private void showPanel(int idx) {
        contentArea.removeAll();
        JPanel p;
        switch (idx) {
            case 0: p = buildOverview(); break;
            case 1: p = buildUsersPanel(); break;
            case 2: p = buildAllAppointments(); break;
            case 3: p = buildDoctorsPanel(); break;
            default: p = buildReportsPanel(); break;
        }
        contentArea.add(p, BorderLayout.CENTER); contentArea.revalidate(); contentArea.repaint();
    }

    private JPanel buildOverview() {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); p.setOpaque(false);
        p.add(sectionHeader("System Overview", "MediSphere Healthcare Management Platform"));
        p.add(Box.createVerticalStrut(16));

        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setOpaque(false); stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(statCard("👨‍⚕️", String.valueOf(facade.getTotalDoctors()), "Total Doctors", UITheme.PRIMARY, new Color(0xE3F2FD)));
        stats.add(statCard("👤", String.valueOf(facade.getTotalPatients()), "Total Patients", UITheme.SUCCESS, new Color(0xE8F5E9)));
        stats.add(statCard("📅", String.valueOf(facade.getTotalAppointments()), "Appointments", UITheme.ACCENT, new Color(0xE0F7FA)));
        stats.add(statCard("⏳", String.valueOf(facade.getPendingAppointments()), "Active/Pending", UITheme.WARNING, new Color(0xFFF3E0)));
        p.add(stats); p.add(Box.createVerticalStrut(24));

        p.add(sectionHeader("Recent Appointments", null));
        p.add(Box.createVerticalStrut(12));

        List<Appointment> recent = facade.getAllAppointments();
        for (int i = 0; i < Math.min(5, recent.size()); i++) {
            p.add(buildAptRow(recent.get(i))); p.add(Box.createVerticalStrut(8));
        }
        if (recent.isEmpty()) p.add(emptyState("📅", "No Appointments Yet", ""));

        JScrollPane scroll = new JScrollPane(p);
        scroll.setBorder(null); scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        JPanel w = new JPanel(new BorderLayout()); w.setOpaque(false); w.add(scroll); return w;
    }

    private JPanel buildUsersPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16)); p.setOpaque(false);
        p.add(sectionHeader("User Management", "All registered users in the system"), BorderLayout.NORTH);

        List<User> users = facade.getDataStore().getAllUsers();
        String[] cols = {"ID", "Name", "Email", "Phone", "Role"};
        Object[][] data = new Object[users.size()][cols.length];
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            String role = u instanceof Doctor ? "Doctor" : u instanceof Patient ? "Patient" : "Admin";
            data[i] = new Object[]{u.getUserId(), u.getName(), u.getEmail(), u.getPhone(), role};
        }

        JTable t = buildStyledTable(data, cols);
        JScrollPane scroll = new JScrollPane(t);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildAllAppointments() {
        JPanel p = new JPanel(new BorderLayout(0, 16)); p.setOpaque(false);
        p.add(sectionHeader("All Appointments", "System-wide appointment log"), BorderLayout.NORTH);

        List<Appointment> apts = facade.getAllAppointments();
        String[] cols = {"ID", "Patient", "Doctor", "Specialty", "Date", "Time", "Status"};
        Object[][] data = new Object[apts.size()][cols.length];
        for (int i = 0; i < apts.size(); i++) {
            Appointment a = apts.get(i);
            data[i] = new Object[]{a.getAppointmentId(), a.getPatientName(), "Dr. " + a.getDoctorName(),
                    a.getSpecialty().getDisplayName(), a.getFormattedDate(), a.getFormattedTime(),
                    a.getStatus().getDisplayName()};
        }

        JTable t = buildStyledTable(data, cols);
        // Color rows by status
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, focus, row, col);
                if (!sel) {
                    String status = (String) tbl.getValueAt(row, 6);
                    if ("Cancelled".equals(status))   c.setBackground(new Color(0xFFF8F8));
                    else if ("Confirmed".equals(status)) c.setBackground(new Color(0xF8FFF8));
                    else c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(t);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildDoctorsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16)); p.setOpaque(false);
        p.add(sectionHeader("Doctors Registry", "All registered medical professionals"), BorderLayout.NORTH);

        List<Doctor> docs = facade.getAllDoctors();
        JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setOpaque(false);
        for (Doctor d : docs) {
            RoundedPanel card = new RoundedPanel(10, Color.WHITE, true);
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
            Color sc = UITheme.fromHex(d.getSpecialty().getColor());
            JPanel av = buildAvatar(d.getName(), sc, 44); av.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel name = new JLabel("Dr. " + d.getName()); name.setFont(UITheme.FONT_H3); name.setForeground(UITheme.TEXT_DARK); name.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel spec = new JLabel(d.getSpecialty().getIcon() + " " + d.getSpecialty().getDisplayName()); spec.setFont(UITheme.FONT_SMALL); spec.setForeground(sc); spec.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel hosp = new JLabel("🏥 " + d.getHospitalAffiliation()); hosp.setFont(UITheme.FONT_SMALL); hosp.setForeground(UITheme.TEXT_LIGHT); hosp.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel rating = new JLabel("⭐ " + String.format("%.1f", d.getRating())); rating.setFont(UITheme.FONT_SMALL); rating.setForeground(new Color(0xFFA000)); rating.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(av); card.add(Box.createVerticalStrut(6)); card.add(name); card.add(spec); card.add(hosp); card.add(rating);
            grid.add(card);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null); scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildReportsPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16)); p.setOpaque(false);
        p.add(sectionHeader("System Reports", "Analytics and design pattern usage"), BorderLayout.NORTH);

        JPanel content = new JPanel(); content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS)); content.setOpaque(false);

        // Design Patterns Summary
        String[][] patterns = {
                {"1", "Factory Method", "Creational", "Creates Patient, Doctor, Admin objects without exposing instantiation logic. Used in UserFactory.createUser()."},
                {"2", "Singleton", "Creational", "DataStore & AppointmentManager have single instances via getInstance(). Ensures consistent data across the app."},
                {"3", "Observer", "Behavioral", "AppointmentSubject notifies EmailNotificationObserver & InAppNotificationObserver on booking/cancellation events."},
                {"4", "Strategy", "Behavioral", "SymptomBasedRecommendation & HistoryBasedRecommendation are interchangeable algorithms set via setRecommendationStrategy()."},
                {"5", "Facade", "Structural", "HealthcareFacade provides a unified interface to login, search, book, recommend, hiding subsystem complexity from UI."}
        };

        content.add(sectionHeader("Design Patterns Implemented", null));
        content.add(Box.createVerticalStrut(12));
        for (String[] row : patterns) {
            RoundedPanel card = new RoundedPanel(10, Color.WHITE, true);
            card.setLayout(new BorderLayout(12, 0));
            card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

            JPanel left = new JPanel(); left.setOpaque(false); left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
            JLabel name = new JLabel(row[0] + ". " + row[1]); name.setFont(UITheme.FONT_H3); name.setForeground(UITheme.TEXT_DARK);
            JLabel desc = new JLabel("<html><body style='width:600px'>" + row[3] + "</body></html>");
            desc.setFont(UITheme.FONT_SMALL); desc.setForeground(UITheme.TEXT_LIGHT);
            left.add(name); left.add(desc);
            card.add(left, BorderLayout.CENTER);

            Color badgeColor = "Creational".equals(row[2]) ? UITheme.PRIMARY :
                               "Behavioral".equals(row[2]) ? UITheme.SUCCESS : UITheme.ACCENT;
            JLabel badge = new JLabel(row[2]);
            badge.setFont(UITheme.FONT_SMALL); badge.setForeground(badgeColor);
            badge.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(badgeColor, 1, true),
                    BorderFactory.createEmptyBorder(3, 9, 3, 9)));
            JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            badgeWrap.setOpaque(false); badgeWrap.add(badge);
            card.add(badgeWrap, BorderLayout.EAST);
            content.add(card); content.add(Box.createVerticalStrut(8));
        }

        content.add(Box.createVerticalStrut(20));
        content.add(sectionHeader("Email Notification Log", "Observer Pattern Activity"));
        content.add(Box.createVerticalStrut(10));
        List<String> emails = EmailNotificationObserver.getEmailLog();
        if (emails.isEmpty()) {
            content.add(emptyState("📧", "No Emails Sent Yet", "Book or cancel an appointment to trigger notifications."));
        } else {
            for (String email : emails) {
                JLabel el = new JLabel("<html>" + email + "</html>");
                el.setFont(UITheme.FONT_SMALL); el.setForeground(UITheme.TEXT_MED);
                el.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0,0,1,0, UITheme.BORDER),
                        BorderFactory.createEmptyBorder(6,0,6,0)));
                el.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                content.add(el);
            }
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null); scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    // ── Helpers ──────────────────────────────────────────────────
    private JTable buildStyledTable(Object[][] data, String[] cols) {
        JTable t = new JTable(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        t.setFont(UITheme.FONT_BODY);
        t.setRowHeight(34);
        t.getTableHeader().setFont(UITheme.FONT_BOLD);
        t.getTableHeader().setBackground(new Color(0x1A237E));
        t.getTableHeader().setForeground(Color.WHITE);
        t.setGridColor(UITheme.BORDER);
        t.setSelectionBackground(new Color(0xE8EAF6));
        t.setSelectionForeground(UITheme.TEXT_DARK);
        t.setShowGrid(true);
        return t;
    }

    private JPanel buildAptRow(Appointment a) {
        Color sc = UITheme.fromHex(a.getStatus().getHexColor());
        RoundedPanel card = new RoundedPanel(8, Color.WHITE, true);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JPanel info = new JPanel(); info.setOpaque(false); info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel t1 = new JLabel(a.getPatientName() + "  →  Dr. " + a.getDoctorName() + "  ·  " + a.getSpecialty().getDisplayName());
        t1.setFont(UITheme.FONT_H3); t1.setForeground(UITheme.TEXT_DARK);
        JLabel t2 = new JLabel("📅 " + a.getFormattedDateTime() + "  ·  " + a.getAppointmentId());
        t2.setFont(UITheme.FONT_SMALL); t2.setForeground(UITheme.TEXT_LIGHT);
        info.add(t1); info.add(t2); card.add(info, BorderLayout.CENTER);
        JLabel sl = new JLabel(a.getStatus().getDisplayName());
        sl.setFont(UITheme.FONT_SMALL); sl.setForeground(sc);
        sl.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(sc, 1, true), BorderFactory.createEmptyBorder(3,9,3,9)));
        JPanel rp = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12)); rp.setOpaque(false); rp.add(sl);
        card.add(rp, BorderLayout.EAST); return card;
    }

    private JPanel buildAvatar(String name, Color color, int size) {
        JPanel av = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
                g2.fillOval(0, 0, size, size);
                g2.setColor(color); g2.setStroke(new BasicStroke(2)); g2.drawOval(1, 1, size-2, size-2);
                String init = name.isEmpty() ? "?" :
                        name.split(" ").length > 1 ? ""+name.split(" ")[0].charAt(0)+name.split(" ")[1].charAt(0) : ""+name.charAt(0);
                g2.setFont(new Font("Segoe UI", Font.BOLD, size/3)); g2.setColor(color);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(init.toUpperCase(), (size-fm.stringWidth(init))/2, (size+fm.getAscent()-fm.getDescent())/2);
            }
        };
        av.setOpaque(false); av.setPreferredSize(new Dimension(size,size));
        av.setMinimumSize(new Dimension(size,size)); av.setMaximumSize(new Dimension(size,size)); return av;
    }

    private JPanel statCard(String icon, String val, String lbl, Color color, Color bg) {
        RoundedPanel c = new RoundedPanel(12, bg, false); c.setLayout(new BorderLayout()); c.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
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
        p.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        JLabel ic = new JLabel(icon); ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36)); ic.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel t = new JLabel(title); t.setFont(UITheme.FONT_H2); t.setForeground(UITheme.TEXT_MED); t.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel d = new JLabel(desc); d.setFont(UITheme.FONT_BODY); d.setForeground(UITheme.TEXT_LIGHT); d.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(ic); p.add(Box.createVerticalStrut(6)); p.add(t); p.add(Box.createVerticalStrut(3)); p.add(d); return p;
    }

    private JPanel divider() {
        JPanel d = new JPanel(); d.setOpaque(false); d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        d.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(255,255,255,20))); return d;
    }
}
