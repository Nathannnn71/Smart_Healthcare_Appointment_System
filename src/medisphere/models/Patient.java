package medisphere.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Patient user — can search doctors, book/cancel/reschedule appointments.
 * Inherits from User (OOP: inheritance).
 */
public class Patient extends User {
    private String dateOfBirth;
    private String bloodType;
    private String allergies;
    private String medicalHistory;
    private String currentSymptoms; // used for recommendation engine
    private List<String> appointmentIds;

    public Patient(String userId, String name, String email, String password,
                   String phone, String dateOfBirth, String bloodType) {
        super(userId, name, email, password, phone);
        this.dateOfBirth    = dateOfBirth;
        this.bloodType      = bloodType;
        this.allergies      = "None";
        this.medicalHistory = "";
        this.currentSymptoms = "";
        this.appointmentIds  = new ArrayList<>();
    }

    @Override
    public String getRole()  { return "Patient"; }

    @Override
    public String getRoleColor() { return "#3B82F6"; } // blue

    // Appointment management
    public void addAppointmentId(String id)    { appointmentIds.add(id); }
    public void removeAppointmentId(String id) { appointmentIds.remove(id); }
    public List<String> getAppointmentIds()    { return new ArrayList<>(appointmentIds); }

    // Getters
    public String getDateOfBirth()    { return dateOfBirth; }
    public String getBloodType()      { return bloodType; }
    public String getAllergies()      { return allergies; }
    public String getMedicalHistory() { return medicalHistory; }
    public String getCurrentSymptoms(){ return currentSymptoms; }

    // Setters
    public void setDateOfBirth(String dob)       { this.dateOfBirth = dob; }
    public void setBloodType(String bt)          { this.bloodType = bt; }
    public void setAllergies(String allergies)   { this.allergies = allergies; }
    public void setMedicalHistory(String history){ this.medicalHistory = history; }
    public void setCurrentSymptoms(String symp)  { this.currentSymptoms = symp; }
}
