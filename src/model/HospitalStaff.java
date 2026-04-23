package model;

public class HospitalStaff {
    private String id;
    private String name;
    private String role;
    private boolean available;

    public HospitalStaff(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.available = true; // Default to available
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return name + " (" + role + ") - " + (available ? "available" : "busy");
    }
}
