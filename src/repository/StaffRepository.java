package repository;

import model.HospitalStaff;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffRepository {

    private final List<HospitalStaff> staffList = new ArrayList<>();

    public StaffRepository() {
        // Example Data
        staffList.add(new HospitalStaff("STF-01", "Dr. Sarah Chen",    "Cardiologist"));
        staffList.add(new HospitalStaff("STF-02", "Dr. James Okafor",  "Trauma Surgeon"));
        staffList.add(new HospitalStaff("STF-03", "Nurse Ana Lima",     "Emergency Nurse"));
        staffList.add(new HospitalStaff("STF-04", "Nurse Tom Reyes",    "Emergency Nurse"));
        staffList.add(new HospitalStaff("STF-05", "Dr. Priya Nair",    "Neurologist"));
        staffList.add(new HospitalStaff("STF-06", "Paramedic Dan Cole", "Paramedic"));
    }

    public List<HospitalStaff> findAll() {
        return staffList;
    }

    public List<HospitalStaff> findAvailable() {
        List<HospitalStaff> available = new ArrayList<>();
        for (HospitalStaff staff : staffList) {
            if (staff.isAvailable()) {
                available.add(staff);
            }
        }
        return available;
    }

    public Optional<HospitalStaff> findById(String id) {
        for (HospitalStaff s : staffList) {
            if (s.getId().equals(id)) return Optional.of(s);
        }
        return Optional.empty();
    }

    public void save(HospitalStaff staff) {
        staffList.removeIf(s -> s.getId().equals(staff.getId()));
        staffList.add(staff);
    }
}
