package strategy;

import enums.Equipment;
import model.Ambulance;
import model.EmergencyRequest;
import java.util.EnumSet;
import java.util.List;

public class MinorEmergencyStrategy implements EmergencyStrategy {

    @Override
    public Ambulance selectAmbulance(EmergencyRequest request, List<Ambulance> available) {
        return AmbulanceSelector.selectBest(request, available,
                EnumSet.of(Equipment.STRETCHER));
    }

    @Override
    public String getResponseProtocol() {
        return "Minor emergency: dispatch nearest ambulance with stretcher. " +
               "Notify one nurse on standby.";
    }
}