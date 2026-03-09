package com.medisphere.model;

public enum Specialty {
    CARDIOLOGY("Cardiology", "❤", "#E74C3C"),
    NEUROLOGY("Neurology", "🧠", "#9B59B6"),
    ORTHOPEDICS("Orthopedics", "🦴", "#E67E22"),
    DERMATOLOGY("Dermatology", "✦", "#F39C12"),
    PEDIATRICS("Pediatrics", "👶", "#2ECC71"),
    OPHTHALMOLOGY("Ophthalmology", "👁", "#1ABC9C"),
    GENERAL_PRACTICE("General Practice", "⚕", "#3498DB"),
    PSYCHIATRY("Psychiatry", "☯", "#8E44AD"),
    ONCOLOGY("Oncology", "⬡", "#C0392B"),
    ENT("ENT", "👂", "#16A085");

    private final String displayName;
    private final String icon;
    private final String color;

    Specialty(String displayName, String icon, String color) {
        this.displayName = displayName;
        this.icon = icon;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public String getIcon() { return icon; }
    public String getColor() { return color; }

    @Override
    public String toString() { return displayName; }
}
