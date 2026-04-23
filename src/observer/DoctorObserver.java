package observer;

import model.Ambulance;
import model.EmergencyRequest;

public class DoctorObserver implements EmergencyObserver {
    private final String doctorName;

    public DoctorObserver(String doctorName) {
        this.doctorName = doctorName;
    }

    @Override
    public void onEmergencyDispatched(EmergencyRequest request, Ambulance ambulance) {
        System.out.println("[DOCTOR ALERT] " + doctorName +
                " — prepare for incoming " + request.getType() +
                " (" + request.getUrgency() + "). Ambulance " +
                ambulance.getId() + " en route.");
    }
}