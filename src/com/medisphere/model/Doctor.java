package com.medisphere.model;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends User {
    private Specialty specialty;
    private String qualification;
    private int experienceYears;
    private double rating;
    private int reviewCount;
    private String consultationFee;
    private String bio;
    private List<String> availableDays;
    private String consultationHours;
    private String hospitalAffiliation;
    private String avatarInitials;

    public Doctor(String name, String email, String password, String phone,
                  Specialty specialty, String qualification, int experienceYears,
                  double rating, String consultationFee, String bio,
                  String hospitalAffiliation, String consultationHours) {
        super(name, email, password, phone, "DOCTOR");
        this.specialty = specialty;
        this.qualification = qualification;
        this.experienceYears = experienceYears;
        this.rating = rating;
        this.reviewCount = (int)(Math.random() * 200 + 50);
        this.consultationFee = consultationFee;
        this.bio = bio;
        this.hospitalAffiliation = hospitalAffiliation;
        this.consultationHours = consultationHours;
        this.availableDays = new ArrayList<>();
        this.avatarInitials = initials(name);
        initDefaultAvailability();
    }

    private String initials(String name) {
        String[] parts = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) if (!p.isEmpty()) sb.append(p.charAt(0));
        return sb.toString().toUpperCase();
    }

    private void initDefaultAvailability() {
        availableDays.add("Monday");
        availableDays.add("Tuesday");
        availableDays.add("Wednesday");
        availableDays.add("Thursday");
        availableDays.add("Friday");
    }

    @Override
    public String getDashboardTitle() { return "Doctor Dashboard"; }

    @Override
    public String getRoleIcon() { return "👨‍⚕️"; }

    public Specialty getSpecialty()         { return specialty; }
    public String getQualification()        { return qualification; }
    public int getExperienceYears()         { return experienceYears; }
    public double getRating()               { return rating; }
    public int getReviewCount()             { return reviewCount; }
    public String getConsultationFee()      { return consultationFee; }
    public String getBio()                  { return bio; }
    public List<String> getAvailableDays()  { return availableDays; }
    public String getConsultationHours()    { return consultationHours; }
    public String getHospitalAffiliation()  { return hospitalAffiliation; }
    public String getAvatarInitials()       { return avatarInitials; }

    public void setRating(double r)         { this.rating = r; }
    public void setAvailableDays(List<String> days) { this.availableDays = days; }

    public String getStarRating() {
        int stars = (int) Math.round(rating);
        return "★".repeat(stars) + "☆".repeat(5 - stars) + String.format(" (%.1f)", rating);
    }
}
