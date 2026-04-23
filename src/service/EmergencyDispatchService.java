package service;

import model.Ambulance;
import model.DispatchRecord;
import model.EmergencyRequest;
import model.HospitalStaff;
import repository.AmbulanceRepository;
import repository.DispatchRepository;

import java.util.List;
import java.util.UUID;

public class EmergencyDispatchService {

    private final AmbulanceService ambulanceService;
    private final NotificationService notificationService;
    private final DispatchRepository dispatchRepository;

    public EmergencyDispatchService(AmbulanceRepository ambulanceRepository,
                                    DispatchRepository dispatchRepository,
                                    NotificationService notificationService) {
        this.ambulanceService     = new AmbulanceService(ambulanceRepository);
        this.notificationService  = notificationService;
        this.dispatchRepository   = dispatchRepository;
    }

    public DispatchRecord dispatch(EmergencyRequest request) {
        System.out.println("\n========================================");
        System.out.println("EMERGENCY RECEIVED: " + request);
        System.out.println("========================================");

        Ambulance ambulance = ambulanceService.findBestAmbulance(request);

        if (ambulance == null) {
            System.out.println("[CRITICAL FAILURE] No ambulance could be dispatched.");
            return null;
        }

        ambulanceService.markDispatched(ambulance);
        System.out.println("[DISPATCHED] " + ambulance);

        List<HospitalStaff> notified = notificationService.getNotifiedStaff(request);
        notificationService.notifyAll(request, ambulance);

        DispatchRecord record = new DispatchRecord(
                UUID.randomUUID().toString(),
                request,
                ambulance,
                notified);

        dispatchRepository.save(record);

        System.out.println("[RECORD SAVED] " + record);
        System.out.println("========================================\n");

        return record;
    }
}