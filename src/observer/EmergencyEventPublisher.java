package observer;

import model.Ambulance;
import model.EmergencyRequest;
import java.util.ArrayList;
import java.util.List;

public class EmergencyEventPublisher {

    private final List<EmergencyObserver> observers = new ArrayList<>();

    public void register(EmergencyObserver observer) {
        observers.add(observer);
    }

    public void unregister(EmergencyObserver observer) {
        observers.remove(observer);
    }

    public void publish(EmergencyRequest request, Ambulance ambulance) {
    for (EmergencyObserver observer : observers) {
        observer.onEmergencyDispatched(request, ambulance);
    }
}
}