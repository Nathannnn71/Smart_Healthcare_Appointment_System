package com.medisphere.ui.components;

import com.medisphere.ui.UITheme;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;

public class StyledPasswordField extends JPasswordField {
    private String placeholder;
    private boolean focused = false;

    public StyledPasswordField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        setFont(UITheme.FONT_INPUT);
        setForeground(UITheme.TEXT_DARK);
        setBackground(Color.WHITE);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

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

        if (getPassword().length == 0 && !isFocusOwner()) {
            Graphics2D gh = (Graphics2D) g.create();
            gh.setFont(UITheme.FONT_INPUT);
            gh.setColor(UITheme.TEXT_LIGHT);
            gh.drawString(placeholder, 12, (getHeight() + gh.getFontMetrics().getAscent()
                    - gh.getFontMetrics().getDescent()) / 2);
            gh.dispose();
        }
    }
}
