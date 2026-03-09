package com.medisphere.model;

public enum AppointmentStatus {
    PENDING("Pending", "#F39C12"),
    CONFIRMED("Confirmed", "#27AE60"),
    CANCELLED("Cancelled", "#E74C3C"),
    COMPLETED("Completed", "#3498DB"),
    RESCHEDULED("Rescheduled", "#9B59B6");

    private final String displayName;
    private final String hexColor;

    AppointmentStatus(String displayName, String hexColor) {
        this.displayName = displayName;
        this.hexColor = hexColor;
    }

    public String getDisplayName() { return displayName; }
    public String getHexColor() { return hexColor; }

    @Override
    public String toString() { return displayName; }
}
