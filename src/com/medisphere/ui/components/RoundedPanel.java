package com.medisphere.ui.components;

import com.medisphere.ui.UITheme;
import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int radius;
    private Color bgColor;
    private boolean drawShadow;
    private Color borderColor;

    public RoundedPanel(int radius) {
        this(radius, UITheme.BG_CARD, false);
    }

    public RoundedPanel(int radius, Color bg, boolean shadow) {
        this.radius = radius;
        this.bgColor = bg;
        this.drawShadow = shadow;
        this.borderColor = null;
        setOpaque(false);
    }

    public void setBorderColor(Color c) { this.borderColor = c; repaint(); }
    public void setBgColor(Color c)     { this.bgColor = c; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        if (drawShadow) {
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(3, 5, w - 6, h - 4, radius, radius);
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(2, 4, w - 4, h - 3, radius, radius);
        }

        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
