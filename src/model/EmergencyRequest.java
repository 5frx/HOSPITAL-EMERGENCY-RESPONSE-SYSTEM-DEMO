package model;

import enums.EmergencyType;
import enums.UrgencyLevel;
import java.time.LocalDateTime;

public class EmergencyRequest {
    private String id;
    private EmergencyType type;
    private UrgencyLevel urgency;
    private double locationX;
    private double locationY;
    private String description;
    private LocalDateTime timestamp;

    public EmergencyRequest(String id, EmergencyType type, UrgencyLevel urgency,
                            double locationX, double locationY, String description) {
        this.id = id;
        this.type = type;
        this.urgency = urgency;
        this.locationX = locationX;
        this.locationY = locationY;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getId()           { return id; }
    public EmergencyType getType()  { return type; }
    public UrgencyLevel getUrgency(){ return urgency; }
    public double getLocationX()    { return locationX; }
    public double getLocationY()    { return locationY; }
    public String getDescription()  { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + urgency + "] " + type + " at (" + locationX + ", " + locationY + ")";
    }
}
