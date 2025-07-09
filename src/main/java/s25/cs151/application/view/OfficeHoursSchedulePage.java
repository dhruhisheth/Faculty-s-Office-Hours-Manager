package s25.cs151.application.view;

import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class OfficeHoursSchedulePage {
    private Stage primaryStage;
    private Scene homeScene;
    public OfficeHoursSchedulePage(Stage primaryStage, Scene homeScene){
        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
    }
    public void start(Stage primaryStage) {
        Label nameLabel = new Label("Student's Full Name:");
        TextField nameField = new TextField();

        Label dateLabel = new Label("Schedule Date:");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        Label timeSlotLabel = new Label("Time Slot:");
        ComboBox<String> timeSlotComboBox = new ComboBox<>(getTimeSlots());
        timeSlotComboBox.getSelectionModel().selectFirst();

        Label courseLabel = new Label("Course:");
        ComboBox<String> courseComboBox = new ComboBox<>(getCourses());
        courseComboBox.getSelectionModel().selectFirst();

        Label reasonLabel = new Label("Reason (Optional):");
        TextField reasonField = new TextField();

        Label commentLabel = new Label("Comment (Optional):");
        TextField commentField = new TextField();

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String studentName = nameField.getText();
            LocalDate scheduleDate = datePicker.getValue();
            String timeSlot = timeSlotComboBox.getValue();
            String course = courseComboBox.getValue();
            String reason = reasonField.getText();
            String comment = commentField.getText();

            if (studentName.isEmpty() || timeSlot == null || course == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.");
                alert.showAndWait();
                return;
            }

            // Save data to the database
            saveOfficeHoursScheduleData(studentName, scheduleDate, timeSlot, course, reason, comment);

            // Confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Office Hours Schedule Saved");
            alert.setContentText("The office hours schedule for " + studentName + " has been saved.");
            alert.showAndWait();
        });

        // Go back button
        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(e -> {
            primaryStage.setScene(homeScene);
            primaryStage.setTitle("Professor Tracker - Home");
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                nameLabel, nameField,
                dateLabel, datePicker,
                timeSlotLabel, timeSlotComboBox,
                courseLabel, courseComboBox,
                reasonLabel, reasonField,
                commentLabel, commentField,
                submitButton, goBackButton);

        Scene scene = new Scene(layout, 900, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Office Hours Schedule");
    }

    //database
    private ObservableList<String> getTimeSlots() {
        ObservableList<String> timeSlots = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:semester_office_hours.db";
        String sql = "SELECT from_hour, to_hour FROM time_slots";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String timeSlot = rs.getString("from_hour") + " - " + rs.getString("to_hour");
                timeSlots.add(timeSlot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timeSlots;
    }
    private ObservableList<String> getCourses() {
        ObservableList<String> courses = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:semester_office_hours.db";
        String sql = "SELECT course_code, section_number FROM courses";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String course = rs.getString("course_code") + "-" + rs.getString("section_number");
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    private void saveOfficeHoursScheduleData(String studentName, LocalDate scheduleDate, String timeSlot, String course, String reason, String comment) {
        String url = "jdbc:sqlite:semester_office_hours.db";
        String sql = "INSERT INTO office_hours_schedule (student_name, schedule_date, time_slot, course, reason, comment) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prep = conn.prepareStatement(sql)) {

            prep.setString(1, studentName);
            prep.setString(2, scheduleDate.toString());
            prep.setString(3, timeSlot);
            prep.setString(4, course);
            prep.setString(5, reason);
            prep.setString(6, comment);

            prep.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
