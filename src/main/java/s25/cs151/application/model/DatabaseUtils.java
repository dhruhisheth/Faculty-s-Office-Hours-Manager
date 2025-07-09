package s25.cs151.application.model;

import javafx.scene.control.Alert;

import java.sql.*;

public class DatabaseUtils {

    public static void initializeDatabase() {
        String url = "jdbc:sqlite:semester_office_hours.db";

        String createCoursesTable = "CREATE TABLE IF NOT EXISTS courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "course_code TEXT NOT NULL," +
                "course_name TEXT NOT NULL," +
                "section_number TEXT NOT NULL" +
                ");";

        String createTimeSlotsTable = "CREATE TABLE IF NOT EXISTS time_slots (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "from_hour TEXT NOT NULL," +
                "to_hour TEXT NOT NULL" +
                ");";

        String createOfficeHoursTable = "CREATE TABLE IF NOT EXISTS office_hours (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "semester TEXT NOT NULL," +
                "year TEXT NOT NULL," +
                "days TEXT NOT NULL" +
                ");";

        String createOfficeHoursScheduleTable = "CREATE TABLE IF NOT EXISTS office_hours_schedule (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_name TEXT NOT NULL," +
                "schedule_date TEXT NOT NULL," +
                "time_slot TEXT NOT NULL," +
                "course TEXT NOT NULL," +
                "reason TEXT," +
                "comment TEXT" +
                ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createCoursesTable);
            stmt.execute(createTimeSlotsTable);
            stmt.execute(createOfficeHoursTable);
            stmt.execute(createOfficeHoursScheduleTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoOfficeHoursDatabase(String semester, String year, String days) {
        String sql = "INSERT INTO office_hours (semester, year, days) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:semester_office_hours.db");
             PreparedStatement prep = conn.prepareStatement(sql)) {
            prep.setString(1, semester);
            prep.setString(2, year);
            prep.setString(3, days);
            prep.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Office hours saved successfully.");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving office hours: " + e.getMessage());
            alert.show();
        }
    }
}