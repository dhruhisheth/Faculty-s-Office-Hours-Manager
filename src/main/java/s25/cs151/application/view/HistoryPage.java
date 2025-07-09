package s25.cs151.application.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import s25.cs151.application.model.OfficeHoursScheduleRecord;

import java.sql.*;

public class HistoryPage {
    private Stage primaryStage;
    private Scene homeScene;

    public HistoryPage(Stage primaryStage, Scene homeScene) {
        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
    }

    public void start(Stage primaryStage) {
        TableView<OfficeHoursRecord> officeHoursTable = createOfficeHoursTableView();
        TableView<CourseRecord> coursesTable = createCoursesTableView();
        TableView<TimeSlotRecord> timeSlotsTable = createTimeSlotsTableView();
        TableView<OfficeHoursScheduleRecord> officeHoursScheduleTable = createOfficeHoursScheduleTableView();

        ObservableList<OfficeHoursRecord> officeHoursData = getAllOfficeHoursRecords();
        officeHoursTable.setItems(officeHoursData);

        ObservableList<CourseRecord> coursesData = getAllCoursesRecords();
        coursesTable.setItems(coursesData);

        ObservableList<TimeSlotRecord> timeSlotsData = getAllTimeSlotsRecords();
        timeSlotsTable.setItems(timeSlotsData);

        ObservableList<OfficeHoursScheduleRecord> officeHoursScheduleData = getAllOfficeHoursScheduleRecords();
        officeHoursScheduleTable.setItems(officeHoursScheduleData);

        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(e -> primaryStage.setScene(homeScene));

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                goBackButton,
                new Label("Office Hours History:"), officeHoursTable,
                new Label("Courses History:"), coursesTable,
                new Label("Time Slots History:"), timeSlotsTable,
                new Label("Office Hours Schedule History:"), officeHoursScheduleTable
        );

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);

        Scene historyScene = new Scene(scrollPane, 900, 900);
        primaryStage.setScene(historyScene);
        primaryStage.setTitle("History");
        primaryStage.show();
    }

    private TableView<OfficeHoursRecord> createOfficeHoursTableView() {
        TableView<OfficeHoursRecord> tableView = new TableView<>();
        TableColumn<OfficeHoursRecord, String> semesterColumn = new TableColumn<>("Semester");
        semesterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemester()));

        TableColumn<OfficeHoursRecord, String> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getYear()));

        TableColumn<OfficeHoursRecord, String> daysColumn = new TableColumn<>("Days");
        daysColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDays()));

        tableView.getColumns().addAll(semesterColumn, yearColumn, daysColumn);
        return tableView;
    }

    private TableView<CourseRecord> createCoursesTableView() {
        TableView<CourseRecord> tableView = new TableView<>();
        TableColumn<CourseRecord, String> courseCodeColumn = new TableColumn<>("Course Code");
        courseCodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseCode()));

        TableColumn<CourseRecord, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));

        TableColumn<CourseRecord, String> sectionNumberColumn = new TableColumn<>("Section");
        sectionNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSectionNumber()));

        tableView.getColumns().addAll(courseCodeColumn, courseNameColumn, sectionNumberColumn);
        return tableView;
    }

    private TableView<TimeSlotRecord> createTimeSlotsTableView() {
        TableView<TimeSlotRecord> tableView = new TableView<>();
        TableColumn<TimeSlotRecord, String> fromHourColumn = new TableColumn<>("From Hour");
        fromHourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFromHour()));

        TableColumn<TimeSlotRecord, String> toHourColumn = new TableColumn<>("To Hour");
        toHourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getToHour()));

        tableView.getColumns().addAll(fromHourColumn, toHourColumn);
        return tableView;
    }

    private TableView<OfficeHoursScheduleRecord> createOfficeHoursScheduleTableView() {
        TableView<OfficeHoursScheduleRecord> tableView = new TableView<>();

        TableColumn<OfficeHoursScheduleRecord, String> studentNameColumn = new TableColumn<>("Student Name");
        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentName()));

        TableColumn<OfficeHoursScheduleRecord, String> scheduleDateColumn = new TableColumn<>("Schedule Date");
        scheduleDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getScheduleDate()));

        TableColumn<OfficeHoursScheduleRecord, String> timeSlotColumn = new TableColumn<>("Time Slot");
        timeSlotColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimeSlot()));

        TableColumn<OfficeHoursScheduleRecord, String> courseColumn = new TableColumn<>("Course");
        courseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse()));

        TableColumn<OfficeHoursScheduleRecord, String> reasonColumn = new TableColumn<>("Reason");
        reasonColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReason()));

        TableColumn<OfficeHoursScheduleRecord, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComment()));

        tableView.getColumns().addAll(
                studentNameColumn,
                scheduleDateColumn,
                timeSlotColumn,
                courseColumn,
                reasonColumn,
                commentColumn
        );

        return tableView;
    }

    private ObservableList<OfficeHoursRecord> getAllOfficeHoursRecords() {
        ObservableList<OfficeHoursRecord> data = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db")) {
            String sql = "SELECT semester, year, days FROM office_hours ORDER BY year DESC, semester DESC LIMIT 4";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data.add(new OfficeHoursRecord(rs.getString("semester"), rs.getString("year"), rs.getString("days")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private ObservableList<CourseRecord> getAllCoursesRecords() {
        ObservableList<CourseRecord> data = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db")) {
            String sql = "SELECT course_code, course_name, section_number FROM courses ORDER BY course_code DESC LIMIT 3";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data.add(new CourseRecord(rs.getString("course_code"), rs.getString("course_name"), rs.getString("section_number")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private ObservableList<TimeSlotRecord> getAllTimeSlotsRecords() {
        ObservableList<TimeSlotRecord> data = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db")) {
            String sql = "SELECT from_hour, to_hour FROM time_slots ORDER BY from_hour ASC LIMIT 4";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data.add(new TimeSlotRecord(rs.getString("from_hour"), rs.getString("to_hour")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private ObservableList<OfficeHoursScheduleRecord> getAllOfficeHoursScheduleRecords() {
        ObservableList<OfficeHoursScheduleRecord> data = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db")) {
            String sql = "SELECT student_name, schedule_date, time_slot, course, reason, comment " +
                    "FROM office_hours_schedule ORDER BY schedule_date ASC, time_slot ASC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                data.add(new OfficeHoursScheduleRecord(
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
        return data;
    }



    public static class OfficeHoursRecord {
        private final SimpleStringProperty semester;
        private final SimpleStringProperty year;
        private final SimpleStringProperty days;

        public OfficeHoursRecord(String semester, String year, String days) {
            this.semester = new SimpleStringProperty(semester);
            this.year = new SimpleStringProperty(year);
            this.days = new SimpleStringProperty(days);
        }

        public String getSemester() {
            return semester.get();
        }

        public String getYear() {
            return year.get();
        }

        public String getDays() {
            return days.get();
        }
    }

    public static class CourseRecord {
        private final SimpleStringProperty courseCode;
        private final SimpleStringProperty courseName;
        private final SimpleStringProperty sectionNumber;

        public CourseRecord(String courseCode, String courseName, String sectionNumber) {
            this.courseCode = new SimpleStringProperty(courseCode);
            this.courseName = new SimpleStringProperty(courseName);
            this.sectionNumber = new SimpleStringProperty(sectionNumber);
        }

        public String getCourseCode() {
            return courseCode.get();
        }

        public String getCourseName() {
            return courseName.get();
        }

        public String getSectionNumber() {
            return sectionNumber.get();
        }
    }

    public static class TimeSlotRecord {
        private final SimpleStringProperty fromHour;
        private final SimpleStringProperty toHour;

        public TimeSlotRecord(String fromHour, String toHour) {
            this.fromHour = new SimpleStringProperty(fromHour);
            this.toHour = new SimpleStringProperty(toHour);
        }

        public String getFromHour() {
            return fromHour.get();
        }

        public String getToHour() {
            return toHour.get();
        }
    }
}