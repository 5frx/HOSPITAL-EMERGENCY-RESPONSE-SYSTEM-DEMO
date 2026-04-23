package model;

import enums.AmbulanceStatus;
import enums.Equipment;
import java.util.Set;

public class Ambulance {
    private String id;
    private double locationX;
    private double locationY;
    private AmbulanceStatus status;
    private Set<Equipment> equipment;

    public Ambulance(String id, double locationX, double locationY, Set<Equipment> equipment) {
        this.id = id;
        this.locationX = locationX;
        this.locationY = locationY;
        this.status = AmbulanceStatus.AVAILABLE; // Default to available
        this.equipment = equipment;
    }

    public String getId() {
        return id;
    }
    public double getLocationX() {
        return locationX;
    }
    public double getLocationY() {
        return locationY;
    }
    public AmbulanceStatus getStatus() {
        return status;
    }
    public Set<Equipment> getEquipment() {
        return equipment;
    }
    public void setStatus(AmbulanceStatus status) {
        this.status = status;
    }

    public boolean hasEquipment(Set<Equipment> required) {
        return equipment.containsAll(required);
    }

    @Override
    public String toString() {
        return "Ambulance " + id + " at (" + locationX + ", " + locationY + ") - " + status;
    }

}
