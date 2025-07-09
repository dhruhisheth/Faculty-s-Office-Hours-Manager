package s25.cs151.application.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.model.DatabaseUtils;

public class OfficeHoursPage {
    private Stage primaryStage;
    private Scene homeScene;

    public OfficeHoursPage(Stage primaryStage, Scene homeScene) {
        this.primaryStage = primaryStage;
        this.homeScene = homeScene;
    }

    public void start(Stage primaryStage) {
        Label semesterLabel = new Label("Semester:");
        ComboBox<String> semesterComboBox = new ComboBox<>();
        semesterComboBox.getItems().addAll("Spring", "Summer", "Fall", "Winter");
        semesterComboBox.setValue("Spring");

        Label yearLabel = new Label("Year:");
        TextField yearTextField = new TextField();
        yearTextField.setPromptText("Enter 4-digit year");

        Label daysLabel = new Label("Days:");
        CheckBox mondayCheckBox = new CheckBox("Monday");
        CheckBox tuesdayCheckBox = new CheckBox("Tuesday");
        CheckBox wednesdayCheckBox = new CheckBox("Wednesday");
        CheckBox thursdayCheckBox = new CheckBox("Thursday");
        CheckBox fridayCheckBox = new CheckBox("Friday");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String semester = semesterComboBox.getValue();
            String year = yearTextField.getText().trim();
            StringBuilder days = new StringBuilder();
            if (mondayCheckBox.isSelected()) days.append("Monday ");
            if (tuesdayCheckBox.isSelected()) days.append("Tuesday ");
            if (wednesdayCheckBox.isSelected()) days.append("Wednesday ");
            if (thursdayCheckBox.isSelected()) days.append("Thursday ");
            if (fridayCheckBox.isSelected()) days.append("Friday ");
            if (year.length() != 4 || !year.matches("\\d+")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid 4-digit year.");
                alert.showAndWait();
                return;
            }
            DatabaseUtils.insertIntoOfficeHoursDatabase(semester, year, days.toString().trim());
        });

        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(e -> {
            primaryStage.setScene(homeScene);
            primaryStage.setTitle("Professor Tracker - Home");
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                semesterLabel, semesterComboBox,
                yearLabel, yearTextField,
                daysLabel, mondayCheckBox, tuesdayCheckBox,
                wednesdayCheckBox, thursdayCheckBox, fridayCheckBox,
                submitButton, goBackButton);
        Scene scene = new Scene(layout, 900, 900);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Semester Office Hours");
    }
}