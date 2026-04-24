package gui;

import enums.EmergencyType;
import enums.UrgencyLevel;
import gui.MainController.DispatchRow;
import enums.AmbulanceStatus;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Ambulance;
import model.DispatchRecord;
import model.EmergencyRequest;
import system.EmergencySystem;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainController {

    private final EmergencySystem system = EmergencySystem.getInstance();
    private final MapCanvas mapCanvas = new MapCanvas();
    private final List<EmergencyRequest> dispatchedRequests = new ArrayList<>();
    private final ObservableList<DispatchRow> logRows = FXCollections.observableArrayList();

    private Label availableCount  = new Label("");
    private Label dispatchedCount = new Label("");
    private Label totalCount      = new Label("");

    private VBox fleetRows;

    public MainController() {
        
    }

    public BorderPane buildUI() {
        BorderPane root = new BorderPane();

        HBox header = buildHeader();

        root.setStyle("-fx-background-color: #F1EFE8;");
        root.setPadding(new Insets(16));
        root.setTop(header);
        root.setCenter(buildCenter());
        BorderPane.setMargin(header, new Insets(0, 0, 12, 0));

        refreshAll();
        return root;
    }

    // ── Header ──────────────────────────────────────────────────────────────

    private HBox buildHeader() {
        Label title = new Label("Hospital Emergency Response System");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2C2C2A"));

        Label sub = new Label("Real-time dispatch management");
        sub.setFont(Font.font("System", 12));
        sub.setTextFill(Color.web("#888780"));

        VBox titleBox = new VBox(2, title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(spacer, titleBox);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 12, 0));

        VBox left = new VBox(2, title, sub);
        header.getChildren().clear();
        header.getChildren().addAll(left, spacer, buildStatCards());
        return header;
    }

    private HBox buildStatCards() {
        long available  = system.getAmbulanceRepository().findAll().stream()
            .filter(a -> a.getStatus() == AmbulanceStatus.AVAILABLE).count();
        long dispatched = system.getAmbulanceRepository().findAll().stream()
                .filter(a -> a.getStatus() == AmbulanceStatus.DISPATCHED).count();

        availableCount.setText(String.valueOf(available));
        dispatchedCount.setText(String.valueOf(dispatched));
        totalCount.setText(String.valueOf(logRows.size()));


        availableCount.setFont(Font.font("System", FontWeight.BOLD, 22));
        availableCount.setTextFill(Color.web("#1D9E75"));

        dispatchedCount.setFont(Font.font("System", FontWeight.BOLD, 22));
        dispatchedCount.setTextFill(Color.web("#E24B4A"));

        totalCount.setFont(Font.font("System", FontWeight.BOLD, 22));
        totalCount.setTextFill(Color.web("#444441"));

        HBox cards = new HBox(8,
                statCard("Available",  availableCount),
                statCard("Dispatched", dispatchedCount),
                statCard("Total",      totalCount));
        return cards;
    }
    
    private VBox statCard(String label, Label valueLabel) {
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", 11));
        lbl.setTextFill(Color.web("#888780"));

        VBox card = new VBox(2, lbl, valueLabel);
        card.setPadding(new Insets(10, 16, 10, 16));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white;" +
                      "-fx-border-color: #D3D1C7;" +
                      "-fx-border-width: 0.5;" +
                      "-fx-border-radius: 8;" +
                      "-fx-background-radius: 8;");
        return card;
    }

    private Label statLabel(String value, String color) {
        Label l = new Label(value);
        l.setFont(Font.font("System", FontWeight.BOLD, 22));
        l.setTextFill(Color.web(color));
        return l;
    }

    // ── Center layout ────────────────────────────────────────────────────────

    private HBox buildCenter() {
        VBox left  = new VBox(12, buildMapPanel(), buildAmbulancePanel());
        VBox right = new VBox(12, buildFormPanel(), buildLogPanel());
        right.setPrefWidth(420);

        HBox center = new HBox(12, left, right);
        VBox.setVgrow(buildLogPanel(), Priority.ALWAYS);
        return center;
    }

    // ── Map panel ────────────────────────────────────────────────────────────

    private VBox buildMapPanel() {
        Label title = sectionLabel("Dispatch map");
        VBox panel = new VBox(8, title, mapCanvas);
        return wrapPanel(panel);
    }

    // ── Ambulance status panel ───────────────────────────────────────────────

    private VBox buildAmbulancePanel() {
        Label title = sectionLabel("Ambulance fleet");
        fleetRows = new VBox(6);
        refreshFleet();
        VBox panel = new VBox(8, title, fleetRows);
        return wrapPanel(panel);
    }

    private HBox buildAmbulanceRow(Ambulance a) {
        Label id = new Label(a.getId());
        id.setFont(Font.font("System", FontWeight.BOLD, 12));
        id.setTextFill(Color.web("#2C2C2A"));
        id.setPrefWidth(70);

        Label loc = new Label("(" + a.getLocationX() + ", " + a.getLocationY() + ")");
        loc.setFont(Font.font("System", 11));
        loc.setTextFill(Color.web("#888780"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label status = new Label(a.getStatus().toString());
        boolean available = a.getStatus() == AmbulanceStatus.AVAILABLE;
        status.setStyle(available
                ? "-fx-background-color:#E1F5EE;-fx-text-fill:#0F6E56;-fx-padding:3 8;-fx-background-radius:4;-fx-font-size:10;"
                : "-fx-background-color:#FAECE7;-fx-text-fill:#993C1D;-fx-padding:3 8;-fx-background-radius:4;-fx-font-size:10;");

        Button returnBtn = new Button("Return");
        returnBtn.setFont(Font.font("System", 10));
        returnBtn.setStyle("-fx-background-color:transparent;-fx-border-color:#D3D1C7;" +
                           "-fx-border-width:0.5;-fx-border-radius:4;-fx-cursor:hand;-fx-padding:3 8;");
        returnBtn.setDisable(available);
        returnBtn.setOnAction(e -> {
            a.setStatus(AmbulanceStatus.AVAILABLE);
            system.getAmbulanceRepository().save(a);

            // remove the request that was assigned to the ambulance
            dispatchedRequests.removeIf(req -> {
                return system.getDispatchRepository().findAll().stream()
                        .anyMatch(record ->
                                record.getAmbulance().getId().equals(a.getId()) &&
                                record.getRequest().getId().equals(req.getId()));
                    });

            refreshAll();
        });

        HBox row = new HBox(8, id, loc, spacer, status, returnBtn);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 8, 6, 8));
        row.setStyle("-fx-background-color:#F8F7F4;-fx-background-radius:6;");
        return row;
    }

    // ── Form panel ───────────────────────────────────────────────────────────

    private ComboBox<EmergencyType> typeBox;
    private ComboBox<UrgencyLevel>  urgencyBox;
    private TextField locXField, locYField, descField;

    private VBox buildFormPanel() {
        Label title = sectionLabel("New emergency request");

        typeBox = new ComboBox<>(FXCollections.observableArrayList(EmergencyType.values()));
        typeBox.setValue(EmergencyType.CARDIAC_ARREST);
        typeBox.setMaxWidth(Double.MAX_VALUE);

        urgencyBox = new ComboBox<>(FXCollections.observableArrayList(UrgencyLevel.values()));
        urgencyBox.setValue(UrgencyLevel.CRITICAL);
        urgencyBox.setMaxWidth(Double.MAX_VALUE);

        locXField = new TextField("30.0");
        locYField = new TextField("25.0");
        descField = new TextField();
        descField.setPromptText("Patient details...");

        HBox locRow = new HBox(8, labeled("Location X", locXField),
                                   labeled("Location Y", locYField));
        HBox.setHgrow(locXField, Priority.ALWAYS);
        HBox.setHgrow(locYField, Priority.ALWAYS);

        Button dispatchBtn = new Button("Dispatch Emergency");
        dispatchBtn.setMaxWidth(Double.MAX_VALUE);
        dispatchBtn.setFont(Font.font("System", FontWeight.BOLD, 13));
        dispatchBtn.setStyle("-fx-background-color:#185FA5;-fx-text-fill:white;" +
                             "-fx-background-radius:8;-fx-padding:10;-fx-cursor:hand;");
        dispatchBtn.setOnAction(e -> handleDispatch());

        VBox form = new VBox(10,
                labeled("Emergency type", typeBox),
                labeled("Urgency level", urgencyBox),
                locRow,
                labeled("Description", descField),
                dispatchBtn);

        VBox panel = new VBox(8, title, form);
        return wrapPanel(panel);
    }

    private VBox labeled(String label, javafx.scene.Node field) {
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", 11));
        lbl.setTextFill(Color.web("#888780"));
        VBox box = new VBox(4, lbl, field);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    // ── Dispatch log ─────────────────────────────────────────────────────────

    private TableView<DispatchRow> buildLogTable() {
        TableView<DispatchRow> table = new TableView<>(logRows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(180);

        table.getColumns().addAll(
                col("Time",       "time",      90),
                col("Type",       "type",      110),
                col("Urgency",    "urgency",   80),
                col("Ambulance",  "ambulance", 70),
                col("Location",   "location",  90));

        return table;
    }

    private VBox buildLogPanel() {
        Label title = sectionLabel("Dispatch log");
        VBox panel = new VBox(8, title, buildLogTable());
        VBox.setVgrow(panel, Priority.ALWAYS);
        return wrapPanel(panel);
    }

    private TableColumn<DispatchRow, String> col(String name, String prop, double w) {
        TableColumn<DispatchRow, String> c = new TableColumn<>(name);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        c.setPrefWidth(w);
        return c;
    }

    // ── Dispatch handler ─────────────────────────────────────────────────────

    private void handleDispatch() {
        try {
            double x = Double.parseDouble(locXField.getText());
            double y = Double.parseDouble(locYField.getText());

            EmergencyRequest request = new EmergencyRequest(
                    UUID.randomUUID().toString(),
                    typeBox.getValue(),
                    urgencyBox.getValue(),
                    x, y,
                    descField.getText().isEmpty() ? "No description" : descField.getText());

            DispatchRecord record = system.getDispatchService().dispatch(request);

            if (record != null) {
                dispatchedRequests.add(request);
                logRows.add(0, new DispatchRow(record));
                refreshAll();
                showAlert("Dispatched", "Ambulance " + record.getAmbulance().getId() +
                          " dispatched to (" + x + ", " + y + ")", Alert.AlertType.INFORMATION);
            } else {
                showAlert("No ambulance available",
                          "No available ambulance could be found with the required equipment.",
                          Alert.AlertType.WARNING);
            }

        } catch (NumberFormatException ex) {
            showAlert("Invalid input", "Location X and Y must be valid numbers.",
                      Alert.AlertType.ERROR);
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void refreshAll() {
        long available  = system.getAmbulanceRepository().findAll().stream()
                .filter(a -> a.getStatus() == AmbulanceStatus.AVAILABLE).count();
        long dispatched = system.getAmbulanceRepository().findAll().stream()
                .filter(a -> a.getStatus() == AmbulanceStatus.DISPATCHED).count();


        availableCount.setText(String.valueOf(available));
        dispatchedCount.setText(String.valueOf(dispatched));
        totalCount.setText(String.valueOf(logRows.size()));

        mapCanvas.refresh(dispatchedRequests);
        refreshFleet();
    }

    private void refreshFleet() {
        fleetRows.getChildren().clear();
        for (Ambulance a : system.getAmbulanceRepository().findAll()){
            fleetRows.getChildren().add(buildAmbulanceRow(a));
        }
    }

    private VBox wrapPanel(VBox content) {
        content.setPadding(new Insets(14));
        content.setStyle("-fx-background-color:white;" +
                         "-fx-border-color:#D3D1C7;" +
                         "-fx-border-width:0.5;" +
                         "-fx-border-radius:10;" +
                         "-fx-background-radius:10;");
        return content;
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, 12));
        l.setTextFill(Color.web("#888780"));
        return l;
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ── DispatchRow (TableView model) ─────────────────────────────────────────

    public static class DispatchRow {
        private final String time, type, urgency, ambulance, location;

        public DispatchRow(DispatchRecord r) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
            this.time      = r.getDispatchedAt().format(fmt);
            this.type      = r.getRequest().getType().toString().replace("_", " ");
            this.urgency   = r.getRequest().getUrgency().toString();
            this.ambulance = r.getAmbulance().getId();
            this.location  = "(" + r.getRequest().getLocationX() +
                             ", " + r.getRequest().getLocationY() + ")";
        }

        public String getTime()      { return time; }
        public String getType()      { return type; }
        public String getUrgency()   { return urgency; }
        public String getAmbulance() { return ambulance; }
        public String getLocation()  { return location; }
    }
}