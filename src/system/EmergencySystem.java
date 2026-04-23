package system;

// SINGLETON: Centralized system managing repositories and services

import repository.AmbulanceRepository;
import repository.DispatchRepository;
import repository.StaffRepository;
import service.EmergencyDispatchService;
import service.NotificationService;

public class EmergencySystem {

    private static EmergencySystem instance;

    private final AmbulanceRepository ambulanceRepository;
    private final StaffRepository staffRepository;
    private final DispatchRepository dispatchRepository;
    private final NotificationService notificationService;
    private final EmergencyDispatchService dispatchService;

    private EmergencySystem() {
        this.ambulanceRepository = new AmbulanceRepository();
        this.staffRepository     = new StaffRepository();
        this.dispatchRepository  = new DispatchRepository();
        this.notificationService = new NotificationService(staffRepository);
        this.dispatchService     = new EmergencyDispatchService(
                                        ambulanceRepository,
                                        dispatchRepository,
                                        notificationService);
    }

    public static EmergencySystem getInstance() {
        if (instance == null) {
            instance = new EmergencySystem();
        }
        return instance;
    }

    public EmergencyDispatchService getDispatchService()  { return dispatchService; }
    public AmbulanceRepository getAmbulanceRepository()   { return ambulanceRepository; }
    public StaffRepository getStaffRepository()           { return staffRepository; }
    public DispatchRepository getDispatchRepository()     { return dispatchRepository; }
    public NotificationService getNotificationService()   { return notificationService; }
}