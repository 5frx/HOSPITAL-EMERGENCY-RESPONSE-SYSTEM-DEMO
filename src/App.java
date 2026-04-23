import enums.EmergencyType;
import enums.UrgencyLevel;
import model.EmergencyRequest;
import model.DispatchRecord;
import system.EmergencySystem;

public class App {
    public static void main(String[] args) {

        EmergencySystem system = EmergencySystem.getInstance();

        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   HOSPITAL EMERGENCY RESPONSE SYSTEM DEMO    ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        // ── Scenario 1: Critical cardiac arrest ──────────────────────────────
        // Expects: AMB-01 (closest with defibrillator)
        // Notifies: all staff
        pause("SCENARIO 1 — Critical cardiac arrest");
        DispatchRecord r1 = system.getDispatchService().dispatch(
            new EmergencyRequest("REQ-001", EmergencyType.CARDIAC_ARREST,
                UrgencyLevel.CRITICAL, 30.0, 25.0,
                "Patient collapsed, unresponsive, no pulse"));

        // ── Scenario 2: High urgency accident ────────────────────────────────
        // Expects: different ambulance with blood supply
        // Notifies: surgeon + nurses
        pause("SCENARIO 2 — High urgency accident");
        DispatchRecord r2 = system.getDispatchService().dispatch(
            new EmergencyRequest("REQ-002", EmergencyType.ACCIDENT,
                UrgencyLevel.HIGH, 55.0, 60.0,
                "Multi-vehicle collision, two casualties"));

        // ── Scenario 3: High urgency stroke ──────────────────────────────────
        // Expects: nearest ambulance with oxygen
        // Notifies: neurologist + nurses
        pause("SCENARIO 3 — High urgency stroke");
        DispatchRecord r3 = system.getDispatchService().dispatch(
            new EmergencyRequest("REQ-003", EmergencyType.STROKE,
                UrgencyLevel.HIGH, 12.0, 8.0,
                "Patient showing stroke symptoms, left side paralysis"));

        // ── Scenario 4: Second critical cardiac — proves fallback ─────────────
        // AMB-01 is DISPATCHED from scenario 1
        // Expects: system skips AMB-01 and picks next best equipped ambulance
        pause("SCENARIO 4 — Second cardiac arrest (AMB-01 already dispatched)");
        DispatchRecord r4 = system.getDispatchService().dispatch(
            new EmergencyRequest("REQ-004", EmergencyType.CARDIAC_ARREST,
                UrgencyLevel.CRITICAL, 40.0, 30.0,
                "Elderly patient, cardiac event in shopping centre"));

        // ── Scenario 5: Low urgency trauma ───────────────────────────────────
        // Expects: minor strategy, only nurse notified
        pause("SCENARIO 5 — Low urgency trauma");
        DispatchRecord r5 = system.getDispatchService().dispatch(
            new EmergencyRequest("REQ-005", EmergencyType.TRAUMA,
                UrgencyLevel.LOW, 70.0, 10.0,
                "Minor fall, elderly patient, conscious and stable"));

        // ── Summary ──────────────────────────────────────────────────────────
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║               DISPATCH SUMMARY               ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.printf("  Total emergencies handled : %d%n", 5);
        System.out.printf("  Ambulances still available: %d%n",
                system.getAmbulanceRepository().findAvailable().size());
        System.out.printf("  Ambulances dispatched     : %d%n",
                5 - system.getAmbulanceRepository().findAvailable().size());
        System.out.println("══════════════════════════════════════════════");
    }

    private static void pause(String scenarioTitle) {
        System.out.println("\n┌─────────────────────────────────────────────");
        System.out.println("│ " + scenarioTitle);
        System.out.println("└─────────────────────────────────────────────");
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }
}