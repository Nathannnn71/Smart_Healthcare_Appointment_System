package medisphere.models;

public enum AppointmentStatus {
    PENDING("Pending", "#F59E0B"),
    CONFIRMED("Confirmed", "#22C55E"),
    CANCELLED("Cancelled", "#EF4444"),
    COMPLETED("Completed", "#3B82F6"),
    RESCHEDULED("Rescheduled", "#8B5CF6");

    private final String label;
    private final String colorHex;

    AppointmentStatus(String label, String colorHex) {
        this.label = label;
        this.colorHex = colorHex;
    }

    public String getLabel() { return label; }
    public String getColorHex() { return colorHex; }

    @Override
    public String toString() { return label; }
}
