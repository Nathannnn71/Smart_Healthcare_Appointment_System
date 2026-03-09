package medisphere.models;

/**
 * Admin user — manages doctors, patients and system-wide appointments.
 * Inherits from User (OOP: inheritance).
 */
public class Admin extends User {
    private String department;
    private String adminLevel; // "SUPER_ADMIN" | "ADMIN"

    public Admin(String userId, String name, String email, String password,
                 String phone, String department, String adminLevel) {
        super(userId, name, email, password, phone);
        this.department = department;
        this.adminLevel = adminLevel;
    }

    @Override
    public String getRole()  { return "Admin"; }

    @Override
    public String getRoleColor() { return "#8B5CF6"; } // purple

    public String getDepartment() { return department; }
    public String getAdminLevel() { return adminLevel; }
    public void setDepartment(String dept)  { this.department = dept; }
    public void setAdminLevel(String level) { this.adminLevel = level; }

    public boolean isSuperAdmin() { return "SUPER_ADMIN".equals(adminLevel); }
}
