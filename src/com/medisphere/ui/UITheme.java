package com.medisphere.ui;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;

public class UITheme {
    // ── Primary Palette ──────────────────────────────────────────
    public static final Color PRIMARY       = new Color(0x1565C0); // Deep Blue
    public static final Color PRIMARY_LIGHT = new Color(0x1E88E5); // Blue
    public static final Color PRIMARY_DARK  = new Color(0x0D47A1); // Darker Blue
    public static final Color ACCENT        = new Color(0x00ACC1); // Cyan
    public static final Color ACCENT_LIGHT  = new Color(0x26C6DA); // Light Cyan

    // ── Semantic Colors ───────────────────────────────────────────
    public static final Color SUCCESS = new Color(0x2E7D32);
    public static final Color SUCCESS_LIGHT = new Color(0x4CAF50);
    public static final Color WARNING = new Color(0xE65100);
    public static final Color WARNING_LIGHT = new Color(0xFF9800);
    public static final Color ERROR   = new Color(0xC62828);
    public static final Color ERROR_LIGHT   = new Color(0xEF5350);
    public static final Color INFO    = new Color(0x0277BD);
    public static final Color INFO_LIGHT    = new Color(0x29B6F6);

    // ── Neutral Colors ────────────────────────────────────────────
    public static final Color BG_PAGE   = new Color(0xF0F4F8); // Page background
    public static final Color BG_CARD   = Color.WHITE;
    public static final Color SIDEBAR   = new Color(0x0D2137); // Dark navy sidebar
    public static final Color SIDEBAR_HOVER = new Color(0x1A3A5C);
    public static final Color SIDEBAR_ACTIVE = new Color(0x1565C0);
    public static final Color TEXT_DARK  = new Color(0x1A2332);
    public static final Color TEXT_MED   = new Color(0x4A5568);
    public static final Color TEXT_LIGHT = new Color(0x718096);
    public static final Color TEXT_WHITE = Color.WHITE;
    public static final Color BORDER     = new Color(0xE2E8F0);
    public static final Color SHADOW     = new Color(0, 0, 0, 25);

    // ── Typography ─────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_H1      = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_H2      = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_H3      = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BOLD    = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SIDEBAR_ITEM = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_INPUT   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LARGE   = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FONT_ICON    = new Font("Segoe UI Emoji", Font.PLAIN, 18);

    // ── Dimensions ─────────────────────────────────────────────────
    public static final int SIDEBAR_W  = 230;
    public static final int TOPBAR_H   = 60;
    public static final int CARD_RADIUS = 12;
    public static final int BTN_RADIUS  = 8;
    public static final Insets CARD_PAD = new Insets(20, 20, 20, 20);

    // ── Gradient Helpers ───────────────────────────────────────────
    public static GradientPaint primaryGradient(int w, int h) {
        return new GradientPaint(0, 0, PRIMARY_DARK, w, h, ACCENT);
    }

    public static GradientPaint sidebarGradient(int h) {
        return new GradientPaint(0, 0, new Color(0x0D2137), 0, h, new Color(0x0A1929));
    }

    // ── Color from hex string ──────────────────────────────────────
    public static Color fromHex(String hex) {
        try {
            return Color.decode(hex);
        } catch (NumberFormatException e) {
            return Color.GRAY;
        }
    }

    // ── Status color helpers ───────────────────────────────────────
    public static Color statusBg(String hexColor) {
        Color c = fromHex(hexColor);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), 30);
    }
}
