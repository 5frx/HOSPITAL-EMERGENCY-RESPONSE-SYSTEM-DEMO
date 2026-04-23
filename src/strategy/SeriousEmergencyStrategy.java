package strategy;

import enums.Equipment;
import model.Ambulance;
import model.EmergencyRequest;
import java.util.EnumSet;
import java.util.List;

public class SeriousEmergencyStrategy implements EmergencyStrategy {

    @Override
    public Ambulance selectAmbulance(EmergencyRequest request, List<Ambulance> available) {
        return AmbulanceSelector.selectBest(request, available,
                EnumSet.of(Equipment.STRETCHER, Equipment.OXYGEN, Equipment.BLOOD_SUPPLY));
    }

    @Override
    public String getResponseProtocol() {
        return "Serious emergency: dispatch ambulance with oxygen and blood supply. " +
               "Notify trauma surgeon and two nurses.";
    }
}