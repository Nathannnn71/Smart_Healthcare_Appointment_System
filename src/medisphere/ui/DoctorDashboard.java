package medisphere.ui;

import medisphere.data.DataStore;
import medisphere.models.*;
import medisphere.patterns.facade.AppointmentFacade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DoctorDashboard extends JPanel {

    private final MainFrame        frame;
    private final Doctor           doctor;
    private final AppointmentFacade facade;
    private final DataStore        store = DataStore.getInstance();

    private CardLayout contentLayout;
    private JPanel     contentArea;
    private JPanel     scheduleList;
    private String     activeNav = "HOME";

    public DoctorDashboard(MainFrame frame, User user, AppointmentFacade facade) {
        this.frame  = frame;
        this.doctor = (Doctor) user;
        this.facade = facade;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        add(buildSidebar(),  BorderLayout.WEST);
        add(buildTopBar(),   BorderLayout.NORTH);

        contentLayout = new CardLayout();
        contentArea   = new JPanel(contentLayout);
        contentArea.setBackground(UITheme.BG);
        contentArea.add(buildHomePanel(),    "HOME");
        contentArea.add(buildSchedulePanel(),"SCHEDULE");
        contentArea.add(buildProfilePanel(), "PROFILE");
        add(contentArea, BorderLayout.CENTER);
        contentLayout.show(contentArea, "HOME");
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(UITheme.SIDEBAR_BG);
        side.setPreferredSize(new Dimension(230, 0));
        side.setBorder(new EmptyBorder(20,0,20,0));

        // Avatar
        JPanel av = buildAvatar();
        av.setAlignmentX(CENTER_ALIGNMENT);
        side.add(av);
        side.add(Box.createVerticalStrut(6));

        JLabel nameL = new JLabel(doctor.getName(), SwingConstants.CENTER);
        nameL.setFont(new Font("Segoe UI", Font.BOLD, 13)); nameL.setForeground(Color.WHITE);
        nameL.setAlignmentX(CENTER_ALIGNMENT);
        JLabel specL = new JLabel(doctor.getSpecialization().getName(), SwingConstants.CENTER);
        specL.setFont(UITheme.FONT_SMALL); specL.setForeground(new Color(100,200,150));
        specL.setAlignmentX(CENTER_ALIGNMENT);
        side.add(nameL); side.add(specL);
        side.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50,70,100));
        sep.setMaximumSize(new Dimension(180,1));
        sep.setAlignmentX(CENTER_ALIGNMENT);
        side.add(sep); side.add(Box.createVerticalStrut(16));

        addNavItem(side, "\uD83C\uDFE0", "Dashboard", "HOME");
        addNavItem(side, "\uD83D\uDDC3",  "My Schedule","SCHEDULE");
        addNavItem(side, "\uD83D\uDC68\u200D\u2695\uFE0F", "Profile","PROFILE");

        side.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("\u2190  Logout");
        logoutBtn.setFont(UITheme.FONT_BODY); logoutBtn.setForeground(new Color(200,100,100));
        logoutBtn.setBorderPainted(false); logoutBtn.setContentAreaFilled(false);
        logoutBtn.setFocusPainted(false); logoutBtn.setAlignmentX(CENTER_ALIGNMENT);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(frame,"Logout?","Confirm",JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) frame.logout();
        });
        side.add(logoutBtn); side.add(Box.createVerticalStrut(10));
        return side;
    }

    private JPanel buildAvatar() {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,UITheme.ACCENT,getWidth(),getHeight(),UITheme.SECONDARY);
                g2.setPaint(gp); g2.fillOval(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        p.setOpaque(false); p.setPreferredSize(new Dimension(64,64)); p.setMaximumSize(new Dimension(64,64));
        JLabel l = new JLabel(doctor.getAvatarInitial());
        l.setFont(new Font("Segoe UI", Font.BOLD, 26)); l.setForeground(Color.WHITE);
        p.add(l);
        return p;
    }

    private JButton[] navBtns = new JButton[5];
    private int navIdx = 0;

    private void addNavItem(JPanel parent, String icon, String label, String card) {
        JButton btn = new JButton(icon + "  " + label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = card.equals(activeNav);
                if (active) {
                    g2.setColor(new Color(34,197,94,60));
                    g2.fillRoundRect(10,2,getWidth()-20,getHeight()-4,8,8);
                    g2.setColor(UITheme.ACCENT);
                    g2.fillRoundRect(0,(getHeight()-28)/2,4,28,4,4);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255,255,255,15));
                    g2.fillRoundRect(10,2,getWidth()-20,getHeight()-4,8,8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", card.equals(activeNav)?Font.BOLD:Font.PLAIN, 13));
        btn.setForeground(card.equals(activeNav)?Color.WHITE:new Color(160,180,220));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false); btn.setContentAreaFilled(false); btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        btn.setBorder(new EmptyBorder(8,20,8,20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            activeNav = card;
            for (JButton b : navBtns) if (b!=null) b.repaint();
            if (card.equals("SCHEDULE")) refreshSchedule();
            contentLayout.show(contentArea, card);
        });
        if (navIdx < navBtns.length) navBtns[navIdx++] = btn;
        parent.add(btn); parent.add(Box.createVerticalStrut(4));
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, UITheme.BORDER),
            new EmptyBorder(12,30,12,30)));
        JLabel title = new JLabel("Doctor Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18)); title.setForeground(UITheme.TEXT_PRIMARY);
        JLabel greeting = new JLabel("Welcome, Dr. " + doctor.getName().split(" ")[1]);
        greeting.setFont(UITheme.FONT_BODY); greeting.setForeground(UITheme.TEXT_SECONDARY);
        bar.add(title, BorderLayout.WEST); bar.add(greeting, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        // Header
        JPanel welcome = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,new Color(0x047857),getWidth(),0,new Color(0x0891B2));
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16); g2.dispose();
            }
        };
        welcome.setOpaque(false); welcome.setLayout(new BorderLayout());
        welcome.setBorder(new EmptyBorder(24,28,24,28)); welcome.setPreferredSize(new Dimension(0,110));

        JLabel wl = new JLabel("Good day, " + doctor.getName() + "  \uD83D\uDC68\u200D\u2695\uFE0F");
        wl.setFont(new Font("Segoe UI", Font.BOLD, 22)); wl.setForeground(Color.WHITE);
        JLabel ws = new JLabel(doctor.getSpecialization().getName() + " — " + doctor.getSpecialization().getDescription());
        ws.setFont(UITheme.FONT_BODY); ws.setForeground(new Color(180,240,220));
        JPanel wt = new JPanel(new GridLayout(2,1,0,4)); wt.setOpaque(false); wt.add(wl); wt.add(ws);
        welcome.add(wt, BorderLayout.CENTER);

        // Stats
        List<Appointment> all = facade.getDoctorAppointments(doctor.getUserId());
        long todayCount = all.stream().filter(a -> a.getDate().equals(java.time.LocalDate.now())).count();
        long upcoming   = all.stream().filter(a -> a.isUpcoming() && a.getStatus()!=AppointmentStatus.CANCELLED).count();
        long completed  = all.stream().filter(a -> a.getStatus()==AppointmentStatus.COMPLETED).count();

        JPanel stats = new JPanel(new GridLayout(1,4,16,0));
        stats.setOpaque(false);
        stats.add(UITheme.statCard("\uD83D\uDCC5", String.valueOf(all.size()),     "Total Appointments", UITheme.PRIMARY));
        stats.add(UITheme.statCard("\uD83D\uDCC6", String.valueOf(todayCount),     "Today",              UITheme.SECONDARY));
        stats.add(UITheme.statCard("\u23F0",        String.valueOf(upcoming),       "Upcoming",           UITheme.WARN));
        stats.add(UITheme.statCard("\u2705",        String.valueOf(completed),      "Completed",          UITheme.ACCENT));

        // Today's schedule
        JLabel secL = new JLabel("Today's Appointments");
        secL.setFont(UITheme.FONT_HEADER); secL.setForeground(UITheme.TEXT_PRIMARY);

        JPanel todayList = new JPanel();
        todayList.setLayout(new BoxLayout(todayList, BoxLayout.Y_AXIS));
        todayList.setOpaque(false);

        List<Appointment> todayAppts = all.stream()
                .filter(a -> a.getDate().equals(java.time.LocalDate.now()))
                .collect(java.util.stream.Collectors.toList());

        if (todayAppts.isEmpty()) {
            JLabel empty = new JLabel("No appointments scheduled for today.");
            empty.setFont(UITheme.FONT_BODY); empty.setForeground(UITheme.TEXT_MUTED);
            empty.setBorder(new EmptyBorder(16,0,0,0));
            todayList.add(empty);
        } else {
            for (Appointment a : todayAppts) todayList.add(buildApptRow(a));
        }

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.add(Box.createVerticalStrut(20));
        center.add(stats);
        center.add(Box.createVerticalStrut(28));
        center.add(secL);
        center.add(Box.createVerticalStrut(14));
        center.add(todayList);

        panel.add(welcome, BorderLayout.NORTH);
        panel.add(center,  BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildApptRow(Appointment a) {
        Patient pat = (Patient) store.getUserById(a.getPatientId());
        JPanel card = UITheme.card(10);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout(12,0));
        card.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.ShadowBorder(), new EmptyBorder(12,16,12,16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel info = new JPanel(new GridLayout(2,1,0,2)); info.setOpaque(false);
        JLabel pname = new JLabel(pat!=null?pat.getName():"Unknown Patient");
        pname.setFont(new Font("Segoe UI", Font.BOLD, 13)); pname.setForeground(UITheme.TEXT_PRIMARY);
        JLabel symp  = new JLabel("Symptoms: " + a.getSymptoms());
        symp.setFont(UITheme.FONT_SMALL); symp.setForeground(UITheme.TEXT_SECONDARY);
        info.add(pname); info.add(symp);

        JLabel dt = new JLabel(a.getFormattedTime());
        dt.setFont(UITheme.FONT_BODY); dt.setForeground(UITheme.PRIMARY);

        Color sc = statusColor(a.getStatus());
        JLabel badge = UITheme.statusBadge(a.getStatus().getLabel(), sc);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); right.setOpaque(false);
        right.add(dt); right.add(badge);

        // Complete button
        if (a.getStatus()==AppointmentStatus.CONFIRMED || a.getStatus()==AppointmentStatus.PENDING) {
            JButton done = UITheme.successButton("Complete");
            done.addActionListener(e -> {
                String notes = JOptionPane.showInputDialog(frame,"Doctor's notes (optional):","");
                facade.completeAppointment(a.getAppointmentId(), doctor.getUserId(), notes==null?"":notes);
                JOptionPane.showMessageDialog(frame,"Appointment marked as completed.","Done",JOptionPane.INFORMATION_MESSAGE);
            });
            right.add(done);
        }

        card.add(info, BorderLayout.CENTER); card.add(right, BorderLayout.EAST);
        JPanel wrap = new JPanel(new BorderLayout()); wrap.setOpaque(false);
        wrap.setBorder(new EmptyBorder(0,0,8,0)); wrap.add(card, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel buildSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        JLabel title = new JLabel("My Schedule");
        title.setFont(UITheme.FONT_TITLE); title.setForeground(UITheme.TEXT_PRIMARY);

        scheduleList = new JPanel();
        scheduleList.setLayout(new BoxLayout(scheduleList, BoxLayout.Y_AXIS));
        scheduleList.setBackground(UITheme.BG);

        JScrollPane scroll = new JScrollPane(scheduleList);
        UITheme.flatScrollPane(scroll);

        panel.add(title,  BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        refreshSchedule();
        return panel;
    }

    private void refreshSchedule() {
        if (scheduleList == null) return;
        scheduleList.removeAll();
        scheduleList.add(Box.createVerticalStrut(16));

        List<Appointment> appts = facade.getDoctorAppointments(doctor.getUserId());
        if (appts.isEmpty()) {
            JLabel e = new JLabel("No appointments in your schedule.");
            e.setFont(UITheme.FONT_BODY); e.setForeground(UITheme.TEXT_MUTED); scheduleList.add(e);
        } else {
            for (Appointment a : appts) scheduleList.add(buildApptRow(a));
        }
        scheduleList.revalidate(); scheduleList.repaint();
    }

    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        JLabel title = new JLabel("Doctor Profile");
        title.setFont(UITheme.FONT_TITLE); title.setForeground(UITheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0,0,20,0));

        JPanel form = UITheme.card(14);
        form.setBackground(Color.WHITE);
        form.setLayout(new GridLayout(0,2,20,14));
        form.setBorder(BorderFactory.createCompoundBorder(
            new UITheme.ShadowBorder(), new EmptyBorder(28,28,28,28)));

        addRow(form,"Full Name",      doctor.getName());
        addRow(form,"Email",          doctor.getEmail());
        addRow(form,"Phone",          doctor.getPhone());
        addRow(form,"Specialization", doctor.getSpecialization().getName());
        addRow(form,"Qualification",  doctor.getQualification());
        addRow(form,"Experience",     doctor.getExperienceYears() + " years");
        addRow(form,"Consult. Fee",   doctor.getFormattedFee());
        addRow(form,"Rating",         doctor.getRatingStars() + " " + String.format("%.1f",doctor.getRating()));

        panel.add(title, BorderLayout.NORTH);
        panel.add(form,  BorderLayout.CENTER);
        return panel;
    }

    private void addRow(JPanel p, String label, String val) {
        JLabel l = new JLabel(label); l.setFont(UITheme.FONT_LABEL); l.setForeground(UITheme.TEXT_SECONDARY);
        JLabel v = new JLabel(val!=null&&!val.isEmpty()?val:"—");
        v.setFont(new Font("Segoe UI",Font.BOLD,13)); v.setForeground(UITheme.TEXT_PRIMARY);
        p.add(l); p.add(v);
    }

    private Color statusColor(AppointmentStatus s) {
        switch(s) {
            case CONFIRMED:   return UITheme.ACCENT;
            case CANCELLED:   return UITheme.DANGER;
            case COMPLETED:   return UITheme.PRIMARY;
            case RESCHEDULED: return UITheme.PURPLE;
            default:          return UITheme.WARN;
        }
    }
}
