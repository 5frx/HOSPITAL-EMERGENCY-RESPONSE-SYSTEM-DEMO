package strategy;

import model.Ambulance;
import model.EmergencyRequest;
import java.util.List;

public interface EmergencyStrategy {
    Ambulance selectAmbulance(EmergencyRequest request, List<Ambulance> available);
    String getResponseProtocol();
}