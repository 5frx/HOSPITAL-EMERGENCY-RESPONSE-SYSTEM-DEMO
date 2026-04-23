package observer;

import model.Ambulance;
import model.EmergencyRequest;

public class NurseObserver implements EmergencyObserver {
    private final String nurseName;

    public NurseObserver(String nurseName) {
        this.nurseName = nurseName;
    }

    @Override
    public void onEmergencyDispatched(EmergencyRequest request, Ambulance ambulance) {
        System.out.println("[NURSE ALERT] " + nurseName +
                " — ready emergency bay for " + request.getType() +
                ". ETA: ambulance " + ambulance.getId() + ".");
    }
}