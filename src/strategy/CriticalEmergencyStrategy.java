package strategy;

import enums.Equipment;
import model.Ambulance;
import model.EmergencyRequest;
import java.util.EnumSet;
import java.util.List;

public class CriticalEmergencyStrategy implements EmergencyStrategy {

    @Override
    public Ambulance selectAmbulance(EmergencyRequest request, List<Ambulance> available) {
        return AmbulanceSelector.selectBest(request, available,
                EnumSet.of(Equipment.DEFIBRILLATOR, Equipment.OXYGEN,
                           Equipment.STRETCHER, Equipment.BLOOD_SUPPLY));
    }

    @Override
    public String getResponseProtocol() {
        return "CRITICAL: dispatch fully equipped ambulance immediately. " +
               "Notify all available staff — cardiologist, surgeon, nurses.";
    }
}