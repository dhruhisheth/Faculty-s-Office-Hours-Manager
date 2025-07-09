package s25.cs151.application.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.controller.AlertHandler;
import s25.cs151.application.controller.ErrorAlertHandler;
import s25.cs151.application.controller.InfoAlertHandler;

import java.sql.*;
import java.time.LocalDate;

public class EditOfficeHoursSchedulePage {
    private final Stage primaryStage;
    private final Scene returnScene;
    private final SearchOfficeHoursPage.ScheduleRecord record;

    //Alert handlers using polymorphism
    private final AlertHandler infoHandler = new InfoAlertHandler();
    private final AlertHandler errorHandler = new ErrorAlertHandler();

    public EditOfficeHoursSchedulePage(Stage primaryStage,
                                       Scene returnScene,
                                       SearchOfficeHoursPage.ScheduleRecord record) {
        this.primaryStage = primaryStage;
        this.returnScene = returnScene;
        this.record = record;
    }

    public void start(Stage stage) {
        TextField nameField = new TextField(record.getStudentName());
        DatePicker datePicker = new DatePicker(LocalDate.parse(record.getScheduleDate()));

        ComboBox<String> timeSlotComboBox = new ComboBox<>(getTimeSlots());
        timeSlotComboBox.getSelectionModel().select(record.getTimeSlot());

        ComboBox<String> courseComboBox = new ComboBox<>(getCourses());
        courseComboBox.getSelectionModel().select(record.getCourse());

        TextField reasonField = new TextField(record.getReason());
        TextField commentField = new TextField(record.getComment());

        Button saveBtn = new Button("Save Changes");
        saveBtn.setOnAction(e -> {
            String studentName = nameField.getText().trim();
            LocalDate scheduleDate = datePicker.getValue();
            String timeSlot = timeSlotComboBox.getValue();
            String course = courseComboBox.getValue();
            String reason = reasonField.getText().trim();
            String comment = commentField.getText().trim();

            if (studentName.isEmpty() || timeSlot == null || course == null) {
                new Alert(Alert.AlertType.ERROR,
                        "Please fill in all required fields.")
                        .showAndWait();
                return;
            }

            updateRecord(record.getId(),
                    studentName,
                    scheduleDate.toString(),
                    timeSlot,
                    course,
                    reason,
                    comment);

            //update using polymorphism
            infoHandler.show("Changes saved successfully.");
            primaryStage.setScene(returnScene);
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> primaryStage.setScene(returnScene));

        VBox layout = new VBox(10,
                new Label("Student's Full Name:"), nameField,
                new Label("Schedule Date:"), datePicker,
                new Label("Time Slot:"), timeSlotComboBox,
                new Label("Course:"), courseComboBox,
                new Label("Reason (Optional):"), reasonField,
                new Label("Comment (Optional):"), commentField,
                saveBtn, cancelBtn
        );
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 900, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Edit Office Hours Schedule");
    }

    private ObservableList<String> getTimeSlots() {
        ObservableList<String> slots = FXCollections.observableArrayList();
        String sql = "SELECT from_hour, to_hour FROM time_slots";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                slots.add(rs.getString("from_hour") + " - " + rs.getString("to_hour"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return slots;
    }

    private ObservableList<String> getCourses() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        String sql = "SELECT course_code, section_number FROM courses";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(rs.getString("course_code") + "-" +
                        rs.getString("section_number"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return courses;
    }

    private void updateRecord(int id,
                              String studentName,
                              String scheduleDate,
                              String timeSlot,
                              String course,
                              String reason,
                              String comment) {
        String sql = """
            UPDATE office_hours_schedule
               SET student_name = ?,
                   schedule_date = ?,
                   time_slot = ?,
                   course = ?,
                   reason = ?,
                   comment = ?
             WHERE id = ?
        """;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentName);
            ps.setString(2, scheduleDate);
            ps.setString(3, timeSlot);
            ps.setString(4, course);
            ps.setString(5, reason);
            ps.setString(6, comment);
            ps.setInt   (7, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            errorHandler.show("Failed to save changes: " + ex.getMessage());
        }
    }
}
