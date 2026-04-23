package observer;

import model.Ambulance;
import model.EmergencyRequest;

public class AmbulanceDriverObserver implements EmergencyObserver {
    @Override
    public void onEmergencyDispatched(EmergencyRequest request, Ambulance ambulance) {
        System.out.println("[DRIVER ALERT] Ambulance " + ambulance.getId() +
                " dispatched to (" + request.getLocationX() + ", " +
                request.getLocationY() + ") for " + request.getType() + ".");
    }
}