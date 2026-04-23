package service;

import enums.AmbulanceStatus;
import factory.EmergencyStrategyFactory;
import model.Ambulance;
import model.EmergencyRequest;
import repository.AmbulanceRepository;
import strategy.EmergencyStrategy;

import java.util.List;

public class AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;

    public AmbulanceService(AmbulanceRepository ambulanceRepository) {
        this.ambulanceRepository = ambulanceRepository;
    }

    public Ambulance findBestAmbulance(EmergencyRequest request) {
        List<Ambulance> available = ambulanceRepository.findAvailable();

        if (available.isEmpty()) {
            System.out.println("[WARNING] No ambulances currently available.");
            return null;
        }

        EmergencyStrategy strategy = EmergencyStrategyFactory.createStrategy(request.getUrgency());
        System.out.println("[PROTOCOL] " + strategy.getResponseProtocol());

        Ambulance selected = strategy.selectAmbulance(request, available);

        if (selected == null) {
            System.out.println("[WARNING] No ambulance has the required equipment for " +
                               request.getType() + ". Dispatching closest available.");
            selected = findClosestRegardless(request, available);
        }

        return selected;
    }

    public void markDispatched(Ambulance ambulance) {
        ambulance.setStatus(AmbulanceStatus.DISPATCHED);
        ambulanceRepository.save(ambulance);
    }

    public void markAvailable(Ambulance ambulance) {
        ambulance.setStatus(AmbulanceStatus.AVAILABLE);
        ambulanceRepository.save(ambulance);
    }

    private Ambulance findClosestRegardless(EmergencyRequest request, List<Ambulance> available) {
        Ambulance closest = null;
        double bestDistance = Double.MAX_VALUE;

        for (Ambulance a : available) {
            double distance = Math.sqrt(
                Math.pow(a.getLocationX() - request.getLocationX(), 2) +
                Math.pow(a.getLocationY() - request.getLocationY(), 2));
            if (distance < bestDistance) {
                bestDistance = distance;
                closest = a;
            }
        }
        return closest;
    }
}