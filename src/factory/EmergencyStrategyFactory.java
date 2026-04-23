package factory;

import enums.UrgencyLevel;
import strategy.CriticalEmergencyStrategy;
import strategy.EmergencyStrategy;
import strategy.MinorEmergencyStrategy;
import strategy.SeriousEmergencyStrategy;

public class EmergencyStrategyFactory {

    public static EmergencyStrategy createStrategy(UrgencyLevel urgency) {
        switch (urgency) {
            case CRITICAL: return new CriticalEmergencyStrategy();
            case HIGH:     return new SeriousEmergencyStrategy();
            case MEDIUM:   return new SeriousEmergencyStrategy();
            case LOW:      return new MinorEmergencyStrategy();
            default: throw new IllegalArgumentException("Unknown urgency level: " + urgency);
        }
    }
}