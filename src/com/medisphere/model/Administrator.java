package com.medisphere.model;

public class Administrator extends User {
    private String adminLevel;
    private String department;

    public Administrator(String name, String email, String password, String phone,
                         String adminLevel, String department) {
        super(name, email, password, phone, "ADMIN");
        this.adminLevel = adminLevel;
        this.department = department;
    }

    @Override
    public String getDashboardTitle() { return "Administrator Dashboard"; }

    @Override
    public String getRoleIcon() { return "⚙"; }

    public String getAdminLevel()   { return adminLevel; }
    public String getDepartment()   { return department; }
}
