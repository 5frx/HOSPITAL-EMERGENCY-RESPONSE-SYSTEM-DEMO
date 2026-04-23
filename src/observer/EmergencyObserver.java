package observer;

import model.EmergencyRequest;
import model.Ambulance;

public interface EmergencyObserver {
    void onEmergencyDispatched(EmergencyRequest request, Ambulance ambulance);
}