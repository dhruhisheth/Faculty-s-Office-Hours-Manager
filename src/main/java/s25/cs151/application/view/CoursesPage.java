package s25.cs151.application.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import s25.cs151.application.model.CourseRecord;

import java.sql.*;

public class CoursesPage {
    private Stage primaryStage;
    private Scene homeScene;
    private ObservableList<CourseRecord> coursesData = FXCollections.observableArrayList();

    public CoursesPage(Stage primaryStage, Scene homeScene) {
        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
    }

    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField courseCodeField = new TextField();
        courseCodeField.setPromptText("Course Code");

        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Course Name");

        TextField sectionNumberField = new TextField();
        sectionNumberField.setPromptText("Section Number");

        Button addButton = new Button("Add Course");
        addButton.setOnAction(event -> {
            String courseCode = courseCodeField.getText().trim();
            String courseName = courseNameField.getText().trim();
            String sectionNumber = sectionNumberField.getText().trim();
            if (!courseCode.isEmpty() && !courseName.isEmpty() && !sectionNumber.isEmpty()) {
                if (!isDuplicateEntry(courseCode, courseName, sectionNumber)) {
                    coursesData.add(new CourseRecord(courseCode, courseName, sectionNumber));
                    coursesData.sort((course1, course2) -> compareCourseCodes(course2.getCourseCode(), course1.getCourseCode()));
                    courseCodeField.clear();
                    courseNameField.clear();
                    sectionNumberField.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Duplicate entry for the combination of course code, course name, and section number.");
                    alert.showAndWait();
                }
            }
        });

        Button saveButton = new Button("Save Courses");
        saveButton.setOnAction(event -> {
            saveCoursesToDatabase();
            primaryStage.setScene(homeScene);
        });

        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(event -> primaryStage.setScene(homeScene));

        layout.getChildren().addAll(
                new Label("Enter Course Information:"),
                courseCodeField, courseNameField, sectionNumberField,
                addButton, new Label("Added Courses:"), createCoursesTableView(),
                saveButton, goBackButton
        );

        Scene coursesScene = new Scene(layout, 900, 900);
        primaryStage.setScene(coursesScene);
        primaryStage.setTitle("Define Courses");
        primaryStage.show();
    }

    private TableView<CourseRecord> createCoursesTableView() {
        TableView<CourseRecord> tableView = new TableView<>(coursesData);
        TableColumn<CourseRecord, String> courseCodeColumn = new TableColumn<>("Course Code");
        courseCodeColumn.setCellValueFactory(cellData -> cellData.getValue().courseCodeProperty());

        TableColumn<CourseRecord, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());

        TableColumn<CourseRecord, String> sectionNumberColumn = new TableColumn<>("Section");
        sectionNumberColumn.setCellValueFactory(cellData -> cellData.getValue().sectionNumberProperty());

        tableView.getColumns().addAll(courseCodeColumn, courseNameColumn, sectionNumberColumn);
        coursesData.sort((course1, course2) -> compareCourseCodes(course2.getCourseCode(), course1.getCourseCode()));

        return tableView;
    }

    private boolean isDuplicateEntry(String courseCode, String courseName, String sectionNumber) {
        for (CourseRecord record : coursesData) {
            if (record.getCourseCode().equals(courseCode) &&
                    record.getCourseName().equals(courseName) &&
                    record.getSectionNumber().equals(sectionNumber)) {
                return true;
            }
        }

        String url = "jdbc:sqlite:semester_office_hours.db";
        String sql = "SELECT COUNT(*) FROM courses WHERE course_code = ? AND course_name = ? AND section_number = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prep = conn.prepareStatement(sql)) {
            prep.setString(1, courseCode);
            prep.setString(2, courseName);
            prep.setString(3, sectionNumber);
            ResultSet rs = prep.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void saveCoursesToDatabase() {
        String url = "jdbc:sqlite:semester_office_hours.db";
        String sql = "INSERT INTO courses (course_code, course_name, section_number) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement prep = conn.prepareStatement(sql)) {
            for (CourseRecord record : coursesData) {
                prep.setString(1, record.getCourseCode());
                prep.setString(2, record.getCourseName());
                prep.setString(3, record.getSectionNumber());
                prep.addBatch();
            }
            prep.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int compareCourseCodes(String courseCode1, String courseCode2) {
        String dept1 = extractDepartment(courseCode1);
        String dept2 = extractDepartment(courseCode2);

        int deptComparison = dept2.compareTo(dept1);
        if (deptComparison != 0) {
            return deptComparison;
        }

        int num1 = extractCourseNumber(courseCode1);
        int num2 = extractCourseNumber(courseCode2);
        if (num1 != num2) {
            return Integer.compare(num1, num2);
        }

        String suffix1 = extractSuffix(courseCode1);
        String suffix2 = extractSuffix(courseCode2);

        return suffix1.compareTo(suffix2);
    }

    private String extractDepartment(String courseCode) {
        return courseCode.split(" ")[0];
    }

    private int extractCourseNumber(String courseCode) {
        String[] parts = courseCode.split(" ");
        for (String part : parts) {
            if (part.matches("\\d+")) {
                return Integer.parseInt(part);
            }
        }
        return 0;
    }

    private String extractSuffix(String courseCode) {
        return courseCode.replaceAll("[^A-Za-z]","").trim();
    }
}