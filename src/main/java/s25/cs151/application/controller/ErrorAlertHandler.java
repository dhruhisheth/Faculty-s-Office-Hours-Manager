package s25.cs151.application.controller;
import javafx.scene.control.Alert;

public class ErrorAlertHandler implements AlertHandler{
    @Override
    public void show(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}
