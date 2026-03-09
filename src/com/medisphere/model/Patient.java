package com.medisphere.model;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private int age;
    private String bloodType;
    private String medicalHistory;
    private List<String> symptoms;
    private List<String> pastAppointmentDoctorIds;

    public Patient(String name, String email, String password, String phone, int age, String bloodType) {
        super(name, email, password, phone, "PATIENT");
        this.age = age;
        this.bloodType = bloodType;
        this.medicalHistory = "";
        this.symptoms = new ArrayList<>();
        this.pastAppointmentDoctorIds = new ArrayList<>();
    }

    @Override
    public String getDashboardTitle() { return "Patient Dashboard"; }

    @Override
    public String getRoleIcon() { return "👤"; }

    public int getAge()                     { return age; }
    public String getBloodType()            { return bloodType; }
    public String getMedicalHistory()       { return medicalHistory; }
    public List<String> getSymptoms()       { return symptoms; }
    public List<String> getPastDoctorIds()  { return pastAppointmentDoctorIds; }

    public void setAge(int age)             { this.age = age; }
    public void setBloodType(String bt)     { this.bloodType = bt; }
    public void setMedicalHistory(String h) { this.medicalHistory = h; }
    public void addSymptom(String symptom)  { symptoms.add(symptom); }
    public void addPastDoctorId(String id)  { if (!pastAppointmentDoctorIds.contains(id)) pastAppointmentDoctorIds.add(id); }
}
