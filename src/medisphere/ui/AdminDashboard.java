package medisphere.ui;

import medisphere.data.DataStore;
import medisphere.models.*;
import medisphere.patterns.facade.AppointmentFacade;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDashboard extends JPanel {

    private final MainFrame        frame;
    private final Admin            admin;
    private final AppointmentFacade facade;
    private final DataStore        store = DataStore.getInstance();

    private CardLayout contentLayout;
    private JPanel     contentArea;
    private String     activeNav = "HOME";

    private JTable  apptTable, userTable;
    private DefaultTableModel apptModel, userModel;

    public AdminDashboard(MainFrame frame, User user, AppointmentFacade facade) {
        this.frame  = frame;
        this.admin  = (Admin) user;
        this.facade = facade;
        setLayout(new BorderLayout());
        setBackground(UITheme.BG);
        add(buildSidebar(),  BorderLayout.WEST);
        add(buildTopBar(),   BorderLayout.NORTH);

        contentLayout = new CardLayout();
        contentArea   = new JPanel(contentLayout);
        contentArea.setBackground(UITheme.BG);
        contentArea.add(buildHomePanel(),        "HOME");
        contentArea.add(buildAppointmentsPanel(),"APPOINTMENTS");
        contentArea.add(buildUsersPanel(),       "USERS");
        add(contentArea, BorderLayout.CENTER);
        contentLayout.show(contentArea, "HOME");
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(UITheme.SIDEBAR_BG);
        side.setPreferredSize(new Dimension(230,0));
        side.setBorder(new EmptyBorder(20,0,20,0));

        // Avatar
        JPanel av = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,UITheme.PURPLE,getWidth(),getHeight(),new Color(0xEC4899));
                g2.setPaint(gp); g2.fillOval(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        av.setOpaque(false); av.setPreferredSize(new Dimension(64,64)); av.setMaximumSize(new Dimension(64,64));
        JLabel avL = new JLabel(admin.getAvatarInitial());
        avL.setFont(new Font("Segoe UI",Font.BOLD,26)); avL.setForeground(Color.WHITE);
        av.add(avL); av.setAlignmentX(CENTER_ALIGNMENT);
        side.add(av); side.add(Box.createVerticalStrut(6));

        JLabel nameL = new JLabel(admin.getName(), SwingConstants.CENTER);
        nameL.setFont(new Font("Segoe UI",Font.BOLD,13)); nameL.setForeground(Color.WHITE); nameL.setAlignmentX(CENTER_ALIGNMENT);
        JLabel roleL = new JLabel("Administrator", SwingConstants.CENTER);
        roleL.setFont(UITheme.FONT_SMALL); roleL.setForeground(new Color(180,120,240)); roleL.setAlignmentX(CENTER_ALIGNMENT);
        side.add(nameL); side.add(roleL); side.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator(); sep.setForeground(new Color(50,70,100));
        sep.setMaximumSize(new Dimension(180,1)); sep.setAlignmentX(CENTER_ALIGNMENT);
        side.add(sep); side.add(Box.createVerticalStrut(16));

        addNavItem(side, "\uD83D\uDCCA","Overview",     "HOME");
        addNavItem(side, "\uD83D\uDCC5","Appointments", "APPOINTMENTS");
        addNavItem(side, "\uD83D\uDC65","Users",        "USERS");

        side.add(Box.createVerticalGlue());
        JButton lo = new JButton("\u2190  Logout"); lo.setFont(UITheme.FONT_BODY);
        lo.setForeground(new Color(200,100,100)); lo.setBorderPainted(false); lo.setContentAreaFilled(false);
        lo.setFocusPainted(false); lo.setAlignmentX(CENTER_ALIGNMENT);
        lo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lo.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(frame,"Logout?","Confirm",JOptionPane.YES_NO_OPTION);
            if (c==JOptionPane.YES_OPTION) frame.logout();
        });
        side.add(lo); side.add(Box.createVerticalStrut(10));
        return side;
    }

    private JButton[] navBtns = new JButton[5]; private int navIdx=0;

    private void addNavItem(JPanel parent, String icon, String label, String card) {
        JButton btn = new JButton(icon+"  "+label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active=card.equals(activeNav);
                if (active) {
                    g2.setColor(new Color(139,92,246,60));
                    g2.fillRoundRect(10,2,getWidth()-20,getHeight()-4,8,8);
                    g2.setColor(UITheme.PURPLE);
                    g2.fillRoundRect(0,(getHeight()-28)/2,4,28,4,4);
                } else if(getModel().isRollover()) {
                    g2.setColor(new Color(255,255,255,15));
                    g2.fillRoundRect(10,2,getWidth()-20,getHeight()-4,8,8);
                }
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI",card.equals(activeNav)?Font.BOLD:Font.PLAIN,13));
        btn.setForeground(card.equals(activeNav)?Color.WHITE:new Color(160,180,220));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false); btn.setContentAreaFilled(false); btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        btn.setBorder(new EmptyBorder(8,20,8,20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            activeNav=card;
            for (JButton b:navBtns) if(b!=null) b.repaint();
            if (card.equals("APPOINTMENTS")) refreshApptTable();
            if (card.equals("USERS"))        refreshUserTable();
            contentLayout.show(contentArea, card);
        });
        if (navIdx<navBtns.length) navBtns[navIdx++]=btn;
        parent.add(btn); parent.add(Box.createVerticalStrut(4));
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,UITheme.BORDER), new EmptyBorder(12,30,12,30)));
        JLabel title = new JLabel("Admin Panel");
        title.setFont(new Font("Segoe UI",Font.BOLD,18)); title.setForeground(UITheme.TEXT_PRIMARY);
        JLabel sub = new JLabel("MediSphere System Management");
        sub.setFont(UITheme.FONT_BODY); sub.setForeground(UITheme.TEXT_SECONDARY);
        bar.add(title, BorderLayout.WEST); bar.add(sub, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        // Hero
        JPanel hero = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp=new GradientPaint(0,0,new Color(0x4F46E5),getWidth(),0,new Color(0x7C3AED));
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16); g2.dispose();
            }
        };
        hero.setOpaque(false); hero.setLayout(new BorderLayout()); hero.setBorder(new EmptyBorder(22,28,22,28));
        hero.setPreferredSize(new Dimension(0,100));
        JLabel hl = new JLabel("System Overview  \u2699\uFE0F");
        hl.setFont(new Font("Segoe UI",Font.BOLD,22)); hl.setForeground(Color.WHITE);
        JLabel hs = new JLabel("Manage users, appointments and monitor system health.");
        hs.setFont(UITheme.FONT_BODY); hs.setForeground(new Color(200,190,255));
        JPanel ht = new JPanel(new GridLayout(2,1,0,4)); ht.setOpaque(false); ht.add(hl); ht.add(hs);
        hero.add(ht, BorderLayout.CENTER);

        // Stats
        long totalUsers    = store.getAllUsers().stream().count();
        long totalDocs     = store.getAllDoctors().size();
        long totalPatients = store.getAllPatients().size();
        long totalAppts    = store.getAllAppointments().stream().count();
        long pendingAppts  = store.getAllAppointments().stream().filter(a->a.getStatus()==AppointmentStatus.PENDING).count();
        long completedAppts= store.getAllAppointments().stream().filter(a->a.getStatus()==AppointmentStatus.COMPLETED).count();

        JPanel stats = new JPanel(new GridLayout(2,3,16,16));
        stats.setOpaque(false);
        stats.add(UITheme.statCard("\uD83D\uDC65", String.valueOf(totalUsers),     "Total Users",   UITheme.PRIMARY));
        stats.add(UITheme.statCard("\uD83D\uDC68\u200D\u2695\uFE0F", String.valueOf(totalDocs), "Doctors",  UITheme.ACCENT));
        stats.add(UITheme.statCard("\uD83D\uDC64", String.valueOf(totalPatients),   "Patients",      UITheme.SECONDARY));
        stats.add(UITheme.statCard("\uD83D\uDCC5", String.valueOf(totalAppts),      "Total Appts",   UITheme.PURPLE));
        stats.add(UITheme.statCard("\u23F3",        String.valueOf(pendingAppts),    "Pending",       UITheme.WARN));
        stats.add(UITheme.statCard("\u2705",        String.valueOf(completedAppts),  "Completed",     UITheme.ACCENT));

        // Recent appointments snippet
        JLabel secL = new JLabel("Recent Appointments");
        secL.setFont(UITheme.FONT_HEADER); secL.setForeground(UITheme.TEXT_PRIMARY);

        String[] cols = {"ID","Patient","Doctor","Date & Time","Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0){public boolean isCellEditable(int r,int c){return false;}};
        store.getAllAppointments().stream().limit(6).forEach(a -> {
            User pat = store.getUserById(a.getPatientId());
            User doc = store.getUserById(a.getDoctorId());
            model.addRow(new Object[]{
                a.getAppointmentId(),
                pat!=null?pat.getName():"?",
                doc!=null?doc.getName():"?",
                a.getFormattedDateTime(),
                a.getStatus().getLabel()
            });
        });
        JTable table = styleTable(model);
        JScrollPane scroll = new JScrollPane(table);
        UITheme.flatScrollPane(scroll); scroll.setPreferredSize(new Dimension(0,180));

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS)); center.setOpaque(false);
        center.add(Box.createVerticalStrut(20)); center.add(stats);
        center.add(Box.createVerticalStrut(28)); center.add(secL);
        center.add(Box.createVerticalStrut(12)); center.add(scroll);

        panel.add(hero,   BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        JLabel title = new JLabel("All Appointments");
        title.setFont(UITheme.FONT_TITLE); title.setForeground(UITheme.TEXT_PRIMARY);

        String[] cols = {"ID","Patient","Doctor","Specialization","Date & Time","Status","Symptoms"};
        apptModel = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        apptTable = styleTable(apptModel);

        JScrollPane scroll = new JScrollPane(apptTable);
        UITheme.flatScrollPane(scroll);

        // Action buttons
        JButton cancelBtn = UITheme.dangerButton("Cancel Selected");
        cancelBtn.addActionListener(e -> {
            int row = apptTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(frame,"Select an appointment first."); return; }
            String id = (String) apptModel.getValueAt(row, 0);
            int c = JOptionPane.showConfirmDialog(frame,"Cancel appointment "+id+"?","Confirm",JOptionPane.YES_NO_OPTION);
            if (c==JOptionPane.YES_OPTION) {
                facade.cancelAppointment(id, admin.getUserId());
                refreshApptTable();
            }
        });
        JButton refreshBtn = UITheme.secondaryButton("\uD83D\uDD04 Refresh");
        refreshBtn.addActionListener(e -> refreshApptTable());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); btnRow.setOpaque(false);
        btnRow.add(refreshBtn); btnRow.add(cancelBtn);

        JPanel top = new JPanel(new BorderLayout()); top.setOpaque(false);
        top.add(title, BorderLayout.WEST); top.add(btnRow, BorderLayout.EAST);

        panel.add(top,    BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        refreshApptTable();
        return panel;
    }

    private void refreshApptTable() {
        if (apptModel==null) return;
        apptModel.setRowCount(0);
        store.getAllAppointments().forEach(a -> {
            User pat = store.getUserById(a.getPatientId());
            User doc = store.getUserById(a.getDoctorId());
            Doctor d = (doc instanceof Doctor) ? (Doctor)doc : null;
            apptModel.addRow(new Object[]{
                a.getAppointmentId(),
                pat!=null?pat.getName():"?",
                doc!=null?doc.getName():"?",
                d!=null?d.getSpecialization().getName():"?",
                a.getFormattedDateTime(),
                a.getStatus().getLabel(),
                a.getSymptoms()
            });
        });
    }

    private JPanel buildUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(new EmptyBorder(28,30,28,30));

        JLabel title = new JLabel("User Management");
        title.setFont(UITheme.FONT_TITLE); title.setForeground(UITheme.TEXT_PRIMARY);

        String[] cols = {"ID","Name","Email","Role","Phone","Status"};
        userModel = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        userTable = styleTable(userModel);

        JScrollPane scroll = new JScrollPane(userTable);
        UITheme.flatScrollPane(scroll);

        JButton toggleBtn = UITheme.warnButton("Toggle Active Status");
        toggleBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row<0) { JOptionPane.showMessageDialog(frame,"Select a user first."); return; }
            String id = (String) userModel.getValueAt(row,0);
            User u = store.getUserById(id);
            if (u!=null) {
                u.setActive(!u.isActive());
                refreshUserTable();
            }
        });
        JButton refreshBtn = UITheme.secondaryButton("\uD83D\uDD04 Refresh");
        refreshBtn.addActionListener(e -> refreshUserTable());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); btnRow.setOpaque(false);
        btnRow.add(refreshBtn); btnRow.add(toggleBtn);

        JPanel top = new JPanel(new BorderLayout()); top.setOpaque(false);
        top.add(title, BorderLayout.WEST); top.add(btnRow, BorderLayout.EAST);

        panel.add(top,    BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        refreshUserTable();
        return panel;
    }

    private void refreshUserTable() {
        if (userModel==null) return;
        userModel.setRowCount(0);
        store.getAllUsers().forEach(u -> userModel.addRow(new Object[]{
            u.getUserId(), u.getName(), u.getEmail(), u.getRole(),
            u.getPhone(), u.isActive()?"Active":"Inactive"
        }));
    }

    private JTable styleTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setSelectionBackground(UITheme.PRIMARY_LIGHT);
        table.setSelectionForeground(UITheme.TEXT_PRIMARY);
        table.setBackground(Color.WHITE);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
        table.getTableHeader().setBackground(new Color(0xF8FAFF));
        table.getTableHeader().setForeground(UITheme.TEXT_SECONDARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,1,0,UITheme.BORDER));
        table.setBorder(BorderFactory.createEmptyBorder());

        // Alternating rows renderer
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                if (!sel) setBackground(row%2==0?Color.WHITE:new Color(0xF9FAFB));
                setBorder(new EmptyBorder(4,12,4,12));
                return this;
            }
        };
        for (int i=0; i<model.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        return table;
    }
}
