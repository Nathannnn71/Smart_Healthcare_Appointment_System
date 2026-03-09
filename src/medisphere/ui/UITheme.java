package medisphere.ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Centralised UI constants and helper factory methods for
 * MediSphere's clean, medical-grade design language.
 */
public final class UITheme {

    // ── Colour Palette ──────────────────────────────────────
    public static final Color PRIMARY        = new Color(0x1A73E8);
    public static final Color PRIMARY_DARK   = new Color(0x1557B0);
    public static final Color PRIMARY_LIGHT  = new Color(0xE8F0FE);
    public static final Color SECONDARY      = new Color(0x00BCD4);
    public static final Color ACCENT         = new Color(0x22C55E);
    public static final Color WARN           = new Color(0xF59E0B);
    public static final Color DANGER         = new Color(0xEF4444);
    public static final Color PURPLE         = new Color(0x8B5CF6);
    public static final Color BG             = new Color(0xF4F7FF);
    public static final Color CARD           = Color.WHITE;
    public static final Color BORDER         = new Color(0xE2E8F0);
    public static final Color TEXT_PRIMARY   = new Color(0x1A2B4A);
    public static final Color TEXT_SECONDARY = new Color(0x6B7280);
    public static final Color TEXT_MUTED     = new Color(0x9CA3AF);
    public static final Color SIDEBAR_BG     = new Color(0x0F172A);
    public static final Color SIDEBAR_HOVER  = new Color(0x1E293B);
    public static final Color SIDEBAR_ACTIVE = new Color(0x1A73E8);

    // ── Fonts ────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUB     = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONT_INPUT   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_ICON    = new Font("Dialog",   Font.BOLD,  18);

    private UITheme() {}

    // ── Button factory ───────────────────────────────────────
    public static JButton primaryButton(String text) {
        return styledButton(text, PRIMARY, Color.WHITE);
    }
    public static JButton successButton(String text) {
        return styledButton(text, ACCENT, Color.WHITE);
    }
    public static JButton dangerButton(String text) {
        return styledButton(text, DANGER, Color.WHITE);
    }
    public static JButton warnButton(String text) {
        return styledButton(text, WARN, Color.WHITE);
    }
    public static JButton secondaryButton(String text) {
        return styledButton(text, new Color(0xF1F5F9), TEXT_PRIMARY);
    }

    private static JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color drawBg = getModel().isPressed() ? bg.darker()
                             : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(drawBg);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10,20,10,20));
        return btn;
    }

    // ── Rounded card panel factory ───────────────────────────
    public static JPanel card(int radius) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),radius,radius));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBackground(CARD);
        return p;
    }

    // ── Text field factory ───────────────────────────────────
    public static JTextField inputField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(FONT_INPUT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, BORDER),
            new EmptyBorder(8, 12, 8, 12)));
        tf.setBackground(Color.WHITE);
        tf.setForeground(TEXT_PRIMARY);
        // Simple placeholder (Java 8 friendly)
        tf.setText(placeholder);
        tf.setForeground(TEXT_MUTED);
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText(""); tf.setForeground(TEXT_PRIMARY);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder); tf.setForeground(TEXT_MUTED);
                }
            }
        });
        return tf;
    }

    public static JPasswordField passwordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_INPUT);
        pf.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, BORDER),
            new EmptyBorder(8, 12, 8, 12)));
        pf.setBackground(Color.WHITE);
        pf.setForeground(TEXT_PRIMARY);
        return pf;
    }

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    // ── Separator ────────────────────────────────────────────
    public static JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBackground(BORDER);
        return sep;
    }

    // ── Stat card ─────────────────────────────────────────────
    public static JPanel statCard(String icon, String value, String label, Color color) {
        JPanel card = card(14);
        card.setBackground(CARD);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(20,20,20,20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,4,2,4);

        // Icon badge
        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Dialog", Font.PLAIN, 28));
        JPanel iconBadge = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.dispose();
            }
        };
        iconBadge.setOpaque(false);
        iconBadge.setPreferredSize(new Dimension(56,56));
        iconBadge.add(iconLbl);

        gbc.gridx=0; gbc.gridy=0; gbc.gridheight=2; gbc.anchor=GridBagConstraints.WEST;
        card.add(iconBadge, gbc);

        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLbl.setForeground(TEXT_PRIMARY);
        gbc.gridx=1; gbc.gridy=0; gbc.gridheight=1; gbc.anchor=GridBagConstraints.SOUTHWEST;
        card.add(valueLbl, gbc);

        JLabel labelLbl = new JLabel(label);
        labelLbl.setFont(FONT_SMALL);
        labelLbl.setForeground(TEXT_SECONDARY);
        gbc.gridy=1; gbc.anchor=GridBagConstraints.NORTHWEST;
        card.add(labelLbl, gbc);

        // drop shadow via border
        card.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(), new EmptyBorder(18,18,18,18)));
        return card;
    }

    // ── Status badge ─────────────────────────────────────────
    public static JLabel statusBadge(String text, Color bg) {
        JLabel l = new JLabel(text, JLabel.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgAlpha = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 30);
                g2.setColor(bgAlpha);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(bg.darker());
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(3,10,3,10));
        l.setPreferredSize(new Dimension(100,22));
        return l;
    }

    // ── Inner helper border classes ──────────────────────────
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        public RoundedBorder(int radius, Color color) { this.radius=radius; this.color=color; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.draw(new RoundRectangle2D.Float(x,y,w-1,h-1,radius,radius));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c){ return new Insets(radius/2,radius/2,radius/2,radius/2); }
    }

    public static class ShadowBorder extends AbstractBorder {
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i=1; i<=4; i++) {
                g2.setColor(new Color(0,0,0, 8-i));
                g2.draw(new RoundRectangle2D.Float(x+i,y+i,w-i*2,h-i*2,14,14));
            }
            g2.setColor(BORDER);
            g2.draw(new RoundRectangle2D.Float(x,y,w-1,h-1,14,14));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c){ return new Insets(6,6,6,6); }
    }

    /** Apply flat styling to a JScrollPane */
    public static void flatScrollPane(JScrollPane sp) {
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG);
        sp.setBackground(BG);
        if (sp.getVerticalScrollBar() != null) sp.getVerticalScrollBar().setUnitIncrement(16);
    }
}
