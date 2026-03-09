package com.medisphere.ui.components;

import com.medisphere.ui.UITheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoundedButton extends JButton {
    public enum Style { PRIMARY, SECONDARY, DANGER, SUCCESS, OUTLINE, GHOST }

    private Style style;
    private boolean hovered = false;
    private int radius = UITheme.BTN_RADIUS;

    public RoundedButton(String text) {
        this(text, Style.PRIMARY);
    }

    public RoundedButton(String text, Style style) {
        super(text);
        this.style = style;
        setFont(UITheme.FONT_BUTTON);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    public void setButtonStyle(Style s) { this.style = s; repaint(); }
    public void setRadius(int r)        { this.radius = r; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();

        Color bg, fg, border;
        switch (style) {
            case SECONDARY:
                bg = hovered ? new Color(0xE3F2FD) : new Color(0xECF5FD);
                fg = UITheme.PRIMARY; border = null; break;
            case DANGER:
                bg = hovered ? UITheme.ERROR : UITheme.ERROR_LIGHT;
                fg = Color.WHITE; border = null; break;
            case SUCCESS:
                bg = hovered ? UITheme.SUCCESS : UITheme.SUCCESS_LIGHT;
                fg = Color.WHITE; border = null; break;
            case OUTLINE:
                bg = hovered ? new Color(UITheme.PRIMARY.getRed(), UITheme.PRIMARY.getGreen(),
                        UITheme.PRIMARY.getBlue(), 15) : new Color(0, 0, 0, 0);
                fg = UITheme.PRIMARY; border = UITheme.PRIMARY; break;
            case GHOST:
                bg = hovered ? UITheme.BORDER : new Color(0,0,0,0);
                fg = UITheme.TEXT_MED; border = null; break;
            default: // PRIMARY
                bg = hovered ? UITheme.PRIMARY_DARK : UITheme.PRIMARY;
                fg = Color.WHITE; border = null;
        }

        // Drop shadow for primary
        if (style == Style.PRIMARY || style == Style.DANGER || style == Style.SUCCESS) {
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(1, 3, w - 2, h - 2, radius, radius);
        }

        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        if (border != null) {
            g2.setColor(border);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);
        }

        g2.dispose();

        setForeground(fg);
        super.paintComponent(g);
    }
}
