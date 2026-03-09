package com.medisphere.model;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private static int idCounter = 1000;

    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected String phone;
    protected String role;
    protected List<String> notifications;

    public User(String name, String email, String password, String phone, String role) {
        this.userId = role.substring(0, 1).toUpperCase() + (++idCounter);
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.notifications = new ArrayList<>();
    }

    public abstract String getDashboardTitle();
    public abstract String getRoleIcon();

    // Getters & Setters
    public String getUserId()   { return userId; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getPhone()    { return phone; }
    public String getRole()     { return role; }
    public List<String> getNotifications() { return notifications; }

    public void setName(String name)       { this.name = name; }
    public void setEmail(String email)     { this.email = email; }
    public void setPassword(String pw)     { this.password = pw; }
    public void setPhone(String phone)     { this.phone = phone; }
    public void addNotification(String msg){ notifications.add(0, msg); }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s)", role, name, email);
    }
}
