package repository;

import model.DispatchRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DispatchRepository {
    private final List<DispatchRecord> records = new ArrayList<>();

    public void save(DispatchRecord record) {
        records.add(record);
    }

    public List<DispatchRecord> findAll() {
        return records;
    }

    public Optional<DispatchRecord> findById(String id) {
        for (DispatchRecord r : records) {
            if (r.getId().equals(id)) return Optional.of(r);
        }
        return Optional.empty();
    }

    public List<DispatchRecord> findByAmbulanceId(String ambulanceId) {
        List<DispatchRecord> result = new ArrayList<>();
        for (DispatchRecord r : records) {
            if (r.getAmbulance().getId().equals(ambulanceId)) result.add(r);
        }
        return result;
    }
}
