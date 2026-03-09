package com.medisphere.ui.components;

import com.medisphere.ui.UITheme;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;

public class StyledTextField extends JTextField {
    private String placeholder;
    private boolean focused = false;

    public StyledTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        setFont(UITheme.FONT_INPUT);
        setForeground(UITheme.TEXT_DARK);
        setBackground(Color.WHITE);
        setBorder(new RoundedBorder(8));
        setOpaque(false);

        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { focused = true; repaint(); }
            @Override public void focusLost(FocusEvent e)   { focused = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(focused ? new Color(0xEBF5FB) : Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        g2.setColor(focused ? UITheme.PRIMARY_LIGHT : UITheme.BORDER);
        g2.setStroke(new BasicStroke(focused ? 2f : 1f));
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
        g2.dispose();
        super.paintComponent(g);

        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D gh = (Graphics2D) g.create();
            gh.setFont(UITheme.FONT_INPUT);
            gh.setColor(UITheme.TEXT_LIGHT);
            Insets ins = getInsets();
            FontMetrics fm = gh.getFontMetrics();
            gh.drawString(placeholder, ins.left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            gh.dispose();
        }
    }

    // Inner border class
    private static class RoundedBorder extends AbstractBorder {
        private final int r;
        RoundedBorder(int r) { this.r = r; }
        @Override
        public Insets getBorderInsets(java.awt.Component c) { return new Insets(8, 12, 8, 12); }
        @Override
        public Insets getBorderInsets(java.awt.Component c, Insets i) {
            i.set(8, 12, 8, 12); return i;
        }
    }
}
