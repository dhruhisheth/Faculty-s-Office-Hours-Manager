package s25.cs151.application.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import s25.cs151.application.model.TimeSlotRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TimeSlotsPage {
    private Stage primaryStage;
    private Scene homeScene;
    private ObservableList<TimeSlotRecord> timeSlotsData = FXCollections.observableArrayList();

    public TimeSlotsPage(Stage primaryStage, Scene homeScene) {
        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
    }

    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label fromLabel = new Label("From Hour:");
        ComboBox<String> fromHourComboBox = createHourComboBox();
        ComboBox<String> fromMinuteComboBox = createMinuteComboBox();

        Label toLabel = new Label("To Hour:");
        ComboBox<String> toHourComboBox = createHourComboBox();
        ComboBox<String> toMinuteComboBox = createMinuteComboBox();

        Button addButton = new Button("Add Time Slot");
        addButton.setOnAction(e -> {
            String fromHour = fromHourComboBox.getValue() + ":" + fromMinuteComboBox.getValue();
            String toHour = toHourComboBox.getValue() + ":" + toMinuteComboBox.getValue();
            if (!fromHour.isEmpty() && !toHour.isEmpty()) {
                timeSlotsData.add(new TimeSlotRecord(fromHour, toHour));

                timeSlotsData.sort((slot1, slot2) -> convertToMinutes(slot1.getFromHour()) - convertToMinutes(slot2.getFromHour()));

                fromHourComboBox.setValue("8");
                fromMinuteComboBox.setValue("00");
                toHourComboBox.setValue("8");
                toMinuteComboBox.setValue("15");
            }
        });

        Button saveButton = new Button("Save Time Slots");
        saveButton.setOnAction(e -> {
            saveTimeSlotsToDatabase();
            primaryStage.setScene(homeScene);
        });

        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(e -> primaryStage.setScene(homeScene));

        layout.getChildren().addAll(
                fromLabel, fromHourComboBox, fromMinuteComboBox,
                toLabel, toHourComboBox, toMinuteComboBox,
                addButton, new Label("Added Time Slots:"), createTimeSlotsTableView(),
                saveButton, goBackButton
        );

        Scene timeSlotsScene = new Scene(layout, 900, 900);
        primaryStage.setScene(timeSlotsScene);
        primaryStage.setTitle("Time Slots");
        primaryStage.show();
    }

    private ComboBox<String> createHourComboBox() {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 8; i <= 18; i++) {
            hours.add(String.valueOf(i <= 12 ? i : i - 12));
        }
        ComboBox<String> hourComboBox = new ComboBox<>(hours);
        hourComboBox.setValue("8");
        return hourComboBox;
    }

    private ComboBox<String> createMinuteComboBox() {
        ObservableList<String> minutes = FXCollections.observableArrayList("00", "15", "30", "45");
        ComboBox<String> minuteComboBox = new ComboBox<>(minutes);
        minuteComboBox.setValue("00");
        return minuteComboBox;
    }

    private TableView<TimeSlotRecord> createTimeSlotsTableView() {
        TableView<TimeSlotRecord> tableView = new TableView<>(timeSlotsData);
        TableColumn<TimeSlotRecord, String> fromHourColumn = new TableColumn<>("From Hour");
        fromHourColumn.setCellValueFactory(cellData -> cellData.getValue().fromHourProperty());

        TableColumn<TimeSlotRecord, String> toHourColumn = new TableColumn<>("To Hour");
        toHourColumn.setCellValueFactory(cellData -> cellData.getValue().toHourProperty());

        tableView.getColumns().addAll(fromHourColumn, toHourColumn);
        return tableView;
    }

    private void saveTimeSlotsToDatabase() {
        String url = "jdbc:sqlite:semester_office_hours.db";
        String sql = "INSERT INTO time_slots (from_hour, to_hour) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prep = conn.prepareStatement(sql)) {
            for (TimeSlotRecord record : timeSlotsData) {
                prep.setString(1, record.getFromHour());
                prep.setString(2, record.getToHour());
                prep.addBatch();
            }
            prep.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int convertToMinutes(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        if (hour < 8) {
            hour += 12;
        }

        return hour * 60 + minute;
    }
}