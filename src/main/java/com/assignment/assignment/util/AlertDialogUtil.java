package com.assignment.assignment.util;

import javafx.scene.control.Alert;

public class AlertDialogUtil {

    private static void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showError(String message) {
        showAlert(Alert.AlertType.ERROR, "Error", "Error", message);
    }
    public static void showConfirmation(String message) {
        showAlert(Alert.AlertType.CONFIRMATION, "Success", "Success", message);
    }
    public static void showWarning(String message) {
        showAlert(Alert.AlertType.WARNING, "Warning", "Warning", message);
    }
    public static void showInformation(String message) {
        showAlert(Alert.AlertType.WARNING, "Information", "Information", message);
    }

}
