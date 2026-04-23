package repository;

import enums.AmbulanceStatus;
import enums.Equipment;
import model.Ambulance;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;
import java.util.Optional;

public class AmbulanceRepository {

    private final List<Ambulance> ambulances = new ArrayList<>();

    public AmbulanceRepository() {
        // Example Data
        ambulances.add(new Ambulance("AMB-01", 10.0, 20.0,
                EnumSet.of(Equipment.DEFIBRILLATOR, Equipment.OXYGEN, Equipment.STRETCHER)));
        ambulances.add(new Ambulance("AMB-02", 35.0, 5.0,
                EnumSet.of(Equipment.OXYGEN, Equipment.STRETCHER, Equipment.BLOOD_SUPPLY)));
        ambulances.add(new Ambulance("AMB-03", 50.0, 50.0,
                EnumSet.of(Equipment.DEFIBRILLATOR, Equipment.BURN_KIT, Equipment.STRETCHER)));
        ambulances.add(new Ambulance("AMB-04", 80.0, 15.0,
                EnumSet.of(Equipment.STRETCHER, Equipment.OXYGEN)));
        ambulances.add(new Ambulance("AMB-05", 5.0, 70.0,
                EnumSet.of(Equipment.DEFIBRILLATOR, Equipment.OXYGEN, Equipment.BURN_KIT, Equipment.STRETCHER)));
    }

    public List<Ambulance> findAll() {
        return ambulances;
    }

    public List<Ambulance> findAvailable() {
        List<Ambulance> available = new ArrayList<>();
        for (Ambulance amb : ambulances) {
            if (amb.getStatus() == AmbulanceStatus.AVAILABLE) {
                available.add(amb);
            }
        }
        return available;
    }

    public Optional<Ambulance> findById(String id) {
        for (Ambulance a : ambulances) {
            if (a.getId().equals(id)) return Optional.of(a);
        }
        return Optional.empty();
    }

    public void save(Ambulance ambulance) {
        ambulances.removeIf(a -> a.getId().equals(ambulance.getId()));
        ambulances.add(ambulance);
    }
}
