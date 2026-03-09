package medisphere.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor user — can view/manage their schedule and appointments.
 * Inherits from User (OOP: inheritance).
 */
public class Doctor extends User {
    private Specialization specialization;
    private String qualification;
    private int experienceYears;
    private double consultationFee;
    private String biography;
    private double rating;
    private int totalReviews;
    private List<String> availableTimeSlots; // e.g. "Monday 09:00 AM"
    private List<String> appointmentIds;

    public Doctor(String userId, String name, String email, String password,
                  String phone, Specialization specialization,
                  String qualification, int experienceYears, double consultationFee) {
        super(userId, name, email, password, phone);
        this.specialization   = specialization;
        this.qualification    = qualification;
        this.experienceYears  = experienceYears;
        this.consultationFee  = consultationFee;
        this.biography        = "Experienced specialist with a dedication to patient care.";
        this.rating           = 3.8 + (Math.random() * 1.2); // 3.8–5.0
        this.totalReviews     = 20 + (int)(Math.random() * 280);
        this.availableTimeSlots = new ArrayList<>();
        this.appointmentIds   = new ArrayList<>();
        initDefaultSlots();
    }

    private void initDefaultSlots() {
        String[] days  = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
        String[] times = {"09:00 AM","10:00 AM","11:00 AM","02:00 PM","03:00 PM","04:00 PM"};
        for (String day : days) {
            for (String time : times) {
                availableTimeSlots.add(day + " " + time);
            }
        }
    }

    @Override
    public String getRole()  { return "Doctor"; }

    @Override
    public String getRoleColor() { return "#22C55E"; } // green

    /** Star rating string e.g. "★★★★☆" */
    public String getRatingStars() {
        int stars = (int) Math.round(Math.min(rating, 5.0));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(i < stars ? "\u2605" : "\u2606");
        return sb.toString();
    }

    public String getFormattedFee() { return String.format("RM %.2f", consultationFee); }

    // Appointment management
    public void addAppointmentId(String id)    { appointmentIds.add(id); }
    public List<String> getAppointmentIds()    { return new ArrayList<>(appointmentIds); }
    public List<String> getAvailableTimeSlots(){ return new ArrayList<>(availableTimeSlots); }
    public void addSlot(String slot)           { availableTimeSlots.add(slot); }
    public void removeSlot(String slot)        { availableTimeSlots.remove(slot); }

    // Getters
    public Specialization getSpecialization() { return specialization; }
    public String getQualification()          { return qualification; }
    public int getExperienceYears()           { return experienceYears; }
    public double getConsultationFee()        { return consultationFee; }
    public String getBiography()              { return biography; }
    public double getRating()                 { return Math.min(rating, 5.0); }
    public int getTotalReviews()              { return totalReviews; }

    // Setters
    public void setSpecialization(Specialization s) { this.specialization = s; }
    public void setQualification(String q)          { this.qualification = q; }
    public void setExperienceYears(int y)           { this.experienceYears = y; }
    public void setConsultationFee(double f)        { this.consultationFee = f; }
    public void setBiography(String b)              { this.biography = b; }
    public void setRating(double r)                 { this.rating = r; }
}
