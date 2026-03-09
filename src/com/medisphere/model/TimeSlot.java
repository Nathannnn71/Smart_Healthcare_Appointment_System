package com.medisphere.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSlot {
    private LocalTime time;
    private boolean available;

    public TimeSlot(LocalTime time, boolean available) {
        this.time = time;
        this.available = available;
    }

    public LocalTime getTime()          { return time; }
    public boolean isAvailable()        { return available; }
    public void setAvailable(boolean a) { this.available = a; }

    public String getDisplayTime() {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    @Override
    public String toString() { return getDisplayTime() + (available ? " ✓" : " ✗"); }
}
