package com.example.appsaldi.utils;

import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import javafx.geometry.Pos;

public class AlertUtils {

    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showNotificationSuccess(String message) {
        Notifications.create()
                .title("Sukses")
                .text(message)
                .position(Pos.TOP_CENTER)
                .hideAfter(Duration.seconds(3))
                .showInformation();
    }

    public static void showNotificationError(String message) {
        Notifications.create()
                .title("Gagal")
                .text(message)
                .position(Pos.TOP_CENTER)
                .hideAfter(Duration.seconds(3))
                .showError();
    }

    public static void showNotificationWarning(String message) {
        Notifications.create()
                .title("Error")
                .text(message)
                .position(Pos.TOP_CENTER)
                .hideAfter(Duration.seconds(3))
                .showError();
    }
}
