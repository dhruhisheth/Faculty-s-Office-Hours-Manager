package s25.cs151.application.controller;
import javafx.scene.control.Alert;

public class InfoAlertHandler implements AlertHandler {
    @Override
    public void show(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }
}
