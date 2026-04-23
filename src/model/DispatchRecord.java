package model;

import java.time.LocalDateTime;
import java.util.List;

public class DispatchRecord {
    private String id;
    private EmergencyRequest request;
    private Ambulance ambulance;
    private List<HospitalStaff> notifiedStaff;
    private LocalDateTime dispatchedAt;

    public DispatchRecord(String id, EmergencyRequest request,
                          Ambulance ambulance, List<HospitalStaff> notifiedStaff) {
        this.id = id;
        this.request = request;
        this.ambulance = ambulance;
        this.notifiedStaff = notifiedStaff;
        this.dispatchedAt = LocalDateTime.now();
    }

    public String getId()                       { return id; }
    public EmergencyRequest getRequest()        { return request; }
    public Ambulance getAmbulance()             { return ambulance; }
    public List<HospitalStaff> getNotifiedStaff(){ return notifiedStaff; }
    public LocalDateTime getDispatchedAt()      { return dispatchedAt; }

    @Override
    public String toString() {
        return "Dispatch #" + id + ": " + request + " → Ambulance " + ambulance.getId()
               + " | Notified: " + notifiedStaff.size() + " staff";
    }
}