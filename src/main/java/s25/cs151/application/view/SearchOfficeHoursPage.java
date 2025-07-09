package s25.cs151.application.view;

import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Optional;

public class SearchOfficeHoursPage {
    private final Stage primaryStage;
    private final Scene homeScene;
    private Scene searchScene;
    private TableView<ScheduleRecord> tableView;
    private TextField searchField;

    public SearchOfficeHoursPage(Stage primaryStage, Scene homeScene) {
        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
    }

    public void start(Stage primaryStage) {
        // ——— Search Field ———
        searchField = new TextField();
        searchField.setPromptText("Search by student name");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> loadData(newVal.trim()));

        // ——— TableView & Columns ———
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ScheduleRecord, String> nameCol = new TableColumn<>("Student Name");
        nameCol.setCellValueFactory(cd -> cd.getValue().studentNameProperty());

        TableColumn<ScheduleRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cd -> cd.getValue().scheduleDateProperty());

        TableColumn<ScheduleRecord, String> timeCol = new TableColumn<>("Time Slot");
        timeCol.setCellValueFactory(cd -> cd.getValue().timeSlotProperty());

        TableColumn<ScheduleRecord, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(cd -> cd.getValue().courseProperty());

        TableColumn<ScheduleRecord, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(cd -> cd.getValue().reasonProperty());

        TableColumn<ScheduleRecord, String> commentCol = new TableColumn<>("Comment");
        commentCol.setCellValueFactory(cd -> cd.getValue().commentProperty());

        TableColumn<ScheduleRecord, Void> deleteCol = new TableColumn<>("Action");
        deleteCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Delete");
            {
                btn.setOnAction(e -> {
                    ScheduleRecord rec = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "Delete record for " + rec.getStudentName() + " on " + rec.getScheduleDate() + "?",
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> res = confirm.showAndWait();
                    if (res.isPresent() && res.get() == ButtonType.YES) {
                        deleteRecord(rec.getId());
                        tableView.getItems().remove(rec);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        TableColumn<ScheduleRecord, Void> editCol = new TableColumn<>("Edit");
        editCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Edit");
            {
                btn.setOnAction(e -> {
                    ScheduleRecord rec = getTableView().getItems().get(getIndex());
                    new EditOfficeHoursSchedulePage(primaryStage, searchScene, rec)
                            .start(primaryStage);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tableView.getColumns().addAll(
                nameCol, dateCol, timeCol,
                courseCol, reasonCol, commentCol,
                deleteCol, editCol
        );

        // ——— Back Button ———
        Button backBtn = new Button("Go Back");
        backBtn.setOnAction(e -> primaryStage.setScene(homeScene));

        // ——— Layout ———
        VBox root = new VBox(10, searchField, tableView, backBtn);
        root.setPadding(new Insets(20));
        searchScene = new Scene(root, 900, 900);
        primaryStage.setScene(searchScene);
        primaryStage.setTitle("Search Office Hours");
        // initial load
        loadData("");
    }

    private void loadData(String filter) {
        ObservableList<ScheduleRecord> list = FXCollections.observableArrayList();
        String sql = """
            SELECT id, student_name, schedule_date, time_slot, course, reason, comment
              FROM office_hours_schedule
             WHERE LOWER(student_name) LIKE ?
          ORDER BY schedule_date DESC, time_slot DESC
            """;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + filter.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ScheduleRecord(
                        rs.getInt("id"),
                        rs.getString("student_name"),
                        rs.getString("schedule_date"),
                        rs.getString("time_slot"),
                        rs.getString("course"),
                        rs.getString("reason"),
                        rs.getString("comment")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(list);
    }

    private void deleteRecord(int id) {
        String sql = "DELETE FROM office_hours_schedule WHERE id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class ScheduleRecord {
        private final IntegerProperty id;
        private final StringProperty studentName, scheduleDate, timeSlot, course, reason, comment;

        public ScheduleRecord(int id, String studentName, String scheduleDate,
                              String timeSlot, String course, String reason, String comment) {
            this.id = new SimpleIntegerProperty(id);
            this.studentName = new SimpleStringProperty(studentName);
            this.scheduleDate = new SimpleStringProperty(scheduleDate);
            this.timeSlot = new SimpleStringProperty(timeSlot);
            this.course = new SimpleStringProperty(course);
            this.reason = new SimpleStringProperty(reason);
            this.comment = new SimpleStringProperty(comment);
        }

        public int getId() { return id.get(); }
        public IntegerProperty idProperty() { return id; }
        public String getStudentName() { return studentName.get(); }
        public StringProperty studentNameProperty() { return studentName; }
        public String getScheduleDate() { return scheduleDate.get(); }
        public StringProperty scheduleDateProperty() { return scheduleDate; }
        public String getTimeSlot() { return timeSlot.get(); }
        public StringProperty timeSlotProperty() { return timeSlot; }
        public String getCourse() { return course.get(); }
        public StringProperty courseProperty() { return course; }
        public String getReason() { return reason.get(); }
        public StringProperty reasonProperty() { return reason; }
        public String getComment() { return comment.get(); }
        public StringProperty commentProperty() { return comment; }
    }
}
