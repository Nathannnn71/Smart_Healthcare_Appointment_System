package medisphere.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for all users in MediSphere.
 * Demonstrates OOP principles: abstraction and encapsulation.
 */
public abstract class User {
    private final String userId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private final LocalDateTime createdAt;
    private boolean active;

    public User(String userId, String name, String email, String password, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    // Abstract methods — each subclass defines its own role (polymorphism)
    public abstract String getRole();
    public abstract String getRoleColor(); // hex color for role badge

    // Authentication
    public boolean authenticate(String inputEmail, String inputPassword) {
        return this.email.equalsIgnoreCase(inputEmail) && this.password.equals(inputPassword);
    }

    // Getters
    public String getUserId()   { return userId; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getPhone()    { return phone; }
    public boolean isActive()   { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setName(String name)         { this.name = name; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone)       { this.phone = phone; }
    public void setActive(boolean active)    { this.active = active; }

    public String getFormattedJoinDate() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    /** Returns first letter of name for avatar display */
    public String getAvatarInitial() {
        return name != null && !name.isEmpty() ? String.valueOf(name.charAt(0)).toUpperCase() : "?";
    }

    @Override
    public String toString() {
        return String.format("%s (%s) [%s]", name, email, getRole());
    }
}
