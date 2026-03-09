package com.medisphere.ui.components;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private Color colorStart;
    private Color colorEnd;
    private boolean vertical;

    public GradientPanel(Color start, Color end) {
        this(start, end, true);
    }

    public GradientPanel(Color start, Color end, boolean vertical) {
        this.colorStart = start;
        this.colorEnd   = end;
        this.vertical   = vertical;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(), h = getHeight();
        GradientPaint gp = vertical
                ? new GradientPaint(0, 0, colorStart, 0, h, colorEnd)
                : new GradientPaint(0, 0, colorStart, w, 0, colorEnd);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
        g2.dispose();
        super.paintComponent(g);
    }
}
