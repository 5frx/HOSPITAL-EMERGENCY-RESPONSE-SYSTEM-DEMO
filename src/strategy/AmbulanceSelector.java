package strategy;

import model.Ambulance;
import model.EmergencyRequest;
import enums.Equipment;
import java.util.List;
import java.util.Set;

public class AmbulanceSelector {

    public static Ambulance selectBest(EmergencyRequest request,
                                       List<Ambulance> available,
                                       Set<Equipment> requiredEquipment) {
        Ambulance best = null;
        double bestScore = Double.MAX_VALUE;

        for (Ambulance a : available) {
            if (!a.hasEquipment(requiredEquipment)) continue;

            double distance = calculateDistance(
                    a.getLocationX(), a.getLocationY(),
                    request.getLocationX(), request.getLocationY());

            if (distance < bestScore) {
                bestScore = distance;
                best = a;
            }
        }
        return best;
    }

    private static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}