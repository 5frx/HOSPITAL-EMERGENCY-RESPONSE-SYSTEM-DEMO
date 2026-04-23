package service;

import model.Ambulance;
import model.EmergencyRequest;
import model.HospitalStaff;
import observer.AmbulanceDriverObserver;
import observer.DoctorObserver;
import observer.EmergencyEventPublisher;
import observer.NurseObserver;
import repository.StaffRepository;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final EmergencyEventPublisher publisher;
    private final StaffRepository staffRepository;

    public NotificationService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
        this.publisher = new EmergencyEventPublisher();
        registerObservers();
    }

    private void registerObservers() {
        for (HospitalStaff staff : staffRepository.findAvailable()) {
            switch (staff.getRole()) {
                case "Cardiologist":
                case "Trauma Surgeon":
                case "Neurologist":
                    publisher.register(new DoctorObserver(staff.getName()));
                    break;
                case "Emergency Nurse":
                    publisher.register(new NurseObserver(staff.getName()));
                    break;
                case "Paramedic":
                    publisher.register(new AmbulanceDriverObserver());
                    break;
            }
        }
    }

    public void notifyAll(EmergencyRequest request, Ambulance ambulance) {
        publisher.publish(request, ambulance);  // was publisher.notifyAll()
    }

    public List<HospitalStaff> getNotifiedStaff(EmergencyRequest request) {
        List<HospitalStaff> relevant = new ArrayList<>();
        for (HospitalStaff staff : staffRepository.findAvailable()) {
            if (shouldNotify(staff, request)) {
                relevant.add(staff);
            }
        }
        return relevant;
    }

    private boolean shouldNotify(HospitalStaff staff, EmergencyRequest request) {
        switch (request.getUrgency()) {
            case CRITICAL:
                return true;
            case HIGH:
                return !staff.getRole().equals("Paramedic");
            case MEDIUM:
                return staff.getRole().equals("Emergency Nurse") || staff.getRole().equals("Trauma Surgeon");
            case LOW:
                return staff.getRole().equals("Emergency Nurse");
            default: return false;
        }
    }
}
