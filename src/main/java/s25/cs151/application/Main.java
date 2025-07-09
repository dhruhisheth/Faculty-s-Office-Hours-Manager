package s25.cs151.application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

// import the new search page
import s25.cs151.application.model.DatabaseUtils;
import s25.cs151.application.view.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // ---------------- Home Page ----------------
        Button btnOfficeHours = new Button("Office Hours");
        Button btnTimeSlots    = new Button("Time Slots");
        Button btnCourses      = new Button("Courses");
        Button btnViewRecords  = new Button("History");
        Button btnSearch       = new Button("Search Office Hours");
        Button btnSchedule     = new Button("Office Hours Schedule");

        // add buttons in desired order
        VBox homeLayout = new VBox(20);
        homeLayout.setPadding(new Insets(20));
        homeLayout.getChildren().addAll(
                btnOfficeHours,
                btnTimeSlots,
                btnCourses,
                btnViewRecords,
                btnSearch,
                btnSchedule
        );

        Scene homeScene = new Scene(homeLayout, 900, 900);
        primaryStage.setTitle("Professor Tracker - Home");
        primaryStage.setScene(homeScene);
        primaryStage.show();

        // ---------------- Office Hours Page ----------------
        btnOfficeHours.setOnAction(e -> {
            new OfficeHoursPage(primaryStage, homeScene).start(primaryStage);
        });

        // ---------------- Time Slots Page ----------------
        btnTimeSlots.setOnAction(e -> {
            new TimeSlotsPage(primaryStage, homeScene).start(primaryStage);
        });

        // ---------------- Courses Page ----------------
        btnCourses.setOnAction(e -> {
            new CoursesPage(primaryStage, homeScene).start(primaryStage);
        });

        // ---------------- History Page ----------------
        btnViewRecords.setOnAction(e -> {
            new HistoryPage(primaryStage, homeScene).start(primaryStage);
        });

        // ---------------- Search Office Hours Page ----------------
        btnSearch.setOnAction(e -> {
            new SearchOfficeHoursPage(primaryStage, homeScene).start(primaryStage);
        });

        // ---------------- Office Hours Schedule Page ----------------
        btnSchedule.setOnAction(e -> {
            new OfficeHoursSchedulePage(primaryStage, homeScene).start(primaryStage);
        });
    }

    public static void main(String[] args) {
        DatabaseUtils.initializeDatabase();
        launch();
    }
}
