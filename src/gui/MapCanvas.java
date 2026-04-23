package gui;

import enums.AmbulanceStatus;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Ambulance;
import model.DispatchRecord;
import model.EmergencyRequest;
import system.EmergencySystem;

import java.util.List;

public class MapCanvas extends Canvas{

    private static final double MAP_W = 500;
    private static final double MAP_H = 500;
    private static final double SCALE = 4.5;
    private static final double RADIUS = 14;

    private List<EmergencyRequest> recentRequests = new java.util.ArrayList<>();

    public MapCanvas() {
        super(MAP_W, MAP_H);
        draw();
    }

    public void refresh(List<EmergencyRequest> requests) {
        this.recentRequests = requests;
        draw();
    }

    private void draw() {
        GraphicsContext gc = getGraphicsContext2D();

        // Background
        gc.setFill(Color.web("#F8F7F4"));
        gc.fillRect(0, 0, MAP_W, MAP_H);

        drawGrid(gc);
        drawRoads(gc);
        drawAmbulances(gc);
        drawEmergencies(gc);
        drawLegend(gc);
    }

    private void drawGrid(GraphicsContext gc){
        gc.setStroke(Color.web("#E0DED8"));
        gc.setLineWidth(0.5);
        for (int x = 0; x < MAP_W; x += 30) {
            gc.strokeLine(x, 0, x, MAP_H);
        }
        for (int y = 0; y < MAP_H; y += 30) {
            gc.strokeLine(0, y, MAP_W, y);
        }
    }

    private void drawRoads(GraphicsContext gc) {
        gc.setStroke(Color.web("#D0CEC8"));
        gc.setLineWidth(2.5);
        gc.strokeLine(0, MAP_H / 2, MAP_W, MAP_H / 2);
        gc.strokeLine(MAP_W / 2, 0, MAP_W / 2, MAP_H);
    }

     private void drawAmbulances(GraphicsContext gc) {
        List<Ambulance> ambulances = EmergencySystem.getInstance()
                .getAmbulanceRepository().findAll();

        for (Ambulance a : ambulances) {
            double cx = a.getLocationX() * SCALE;
            double cy = a.getLocationY() * SCALE;

            boolean dispatched = a.getStatus() == AmbulanceStatus.DISPATCHED;

            // Shadow
            gc.setFill(Color.web("#00000018"));
            gc.fillOval(cx - RADIUS + 2, cy - RADIUS + 2, RADIUS * 2, RADIUS * 2);

            // Circle
            gc.setFill(dispatched ? Color.web("#E24B4A") : Color.web("#1D9E75"));
            gc.fillOval(cx - RADIUS, cy - RADIUS, RADIUS * 2, RADIUS * 2);

            // Border
            gc.setStroke(dispatched ? Color.web("#A32D2D") : Color.web("#0F6E56"));
            gc.setLineWidth(1.5);
            gc.strokeOval(cx - RADIUS, cy - RADIUS, RADIUS * 2, RADIUS * 2);

            // Label
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("System", FontWeight.BOLD, 9));
            gc.fillText(a.getId().replace("AMB-", "A"), cx - 7, cy + 4);
        }
    }

    private void drawEmergencies(GraphicsContext gc) {
        for (EmergencyRequest req : recentRequests) {
            double cx = req.getLocationX() * SCALE;
            double cy = req.getLocationY() * SCALE;

            // Triangle marker
            double[] xs = {cx, cx + 12, cx - 12};
            double[] ys = {cy - 16, cy + 8, cy + 8};
            gc.setFill(Color.web("#BA7517"));
            gc.fillPolygon(xs, ys, 3);
            gc.setStroke(Color.web("#854F0B"));
            gc.setLineWidth(1);
            gc.strokePolygon(xs, ys, 3);

            // Exclamation
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("System", FontWeight.BOLD, 10));
            gc.fillText("!", cx - 3, cy + 5);

            // Draw route line to assigned ambulance
            drawRouteLine(gc, req, cx, cy);
        }
    }

    private void drawRouteLine(GraphicsContext gc, EmergencyRequest req,
                                double emergencyX, double emergencyY) {
        List<DispatchRecord> records = EmergencySystem.getInstance()
                .getDispatchRepository().findAll();

        for (DispatchRecord r : records) {
            if (r.getRequest().getId().equals(req.getId())) {
                Ambulance a = r.getAmbulance();
                double ax = a.getLocationX() * SCALE;
                double ay = a.getLocationY() * SCALE;

                gc.setStroke(Color.web("#BA751780"));
                gc.setLineWidth(1.5);
                gc.setLineDashes(6, 4);
                gc.strokeLine(ax, ay, emergencyX, emergencyY);
                gc.setLineDashes(0);
                break;
            }
        }
    }

    private void drawLegend(GraphicsContext gc) {
        gc.setFill(Color.web("#FFFFFFCC"));
        gc.fillRoundRect(10, MAP_H - 60, 200, 50, 8, 8);
        gc.setStroke(Color.web("#D0CEC8"));
        gc.setLineWidth(0.5);
        gc.strokeRoundRect(10, MAP_H - 60, 200, 50, 8, 8);

        gc.setFont(Font.font("System", FontWeight.NORMAL, 10));

        gc.setFill(Color.web("#1D9E75"));
        gc.fillOval(20, MAP_H - 48, 10, 10);
        gc.setFill(Color.web("#444441"));
        gc.fillText("Available", 36, MAP_H - 40);

        gc.setFill(Color.web("#E24B4A"));
        gc.fillOval(20, MAP_H - 30, 10, 10);
        gc.setFill(Color.web("#444441"));
        gc.fillText("Dispatched", 36, MAP_H - 22);

        double[] lx = {130, 138, 122};
        double[] ly = {MAP_H - 48, MAP_H - 28, MAP_H - 28};
        gc.setFill(Color.web("#BA7517"));
        gc.fillPolygon(lx, ly, 3);
        gc.setFill(Color.web("#444441"));
        gc.fillText("Emergency", 145, MAP_H - 32);
    }
}
