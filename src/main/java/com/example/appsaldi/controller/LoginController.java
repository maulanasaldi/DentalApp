package com.example.appsaldi.controller;

import com.example.appsaldi.dao.UserDAO;
import com.example.appsaldi.model.UsersModel;
import com.example.appsaldi.utils.Session;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private CheckBox cbShowPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private ProgressIndicator loadingIndicator;
    @FXML private HBox mainBox;

    UserDAO userDAO = new UserDAO();

    @FXML
    private void handelLogin() {
        String username = txtUsername.getText().trim();
        String password = cbShowPassword.isSelected() ? txtPasswordVisible.getText().trim() : txtPassword.getText().trim();

        loadingIndicator.setVisible(true);
        loadingIndicator.setManaged(true);
        mainBox.setEffect(new BoxBlur(5, 5, 3));
        btnLogin.setDisable(true);

        new Thread(() -> {
            UsersModel usersModel = userDAO.login(username, password);

            Platform.runLater(() -> {
                loadingIndicator.setVisible(false);
                loadingIndicator.setManaged(false);
                mainBox.setEffect(null);
                btnLogin.setDisable(false);

                if (usersModel != null) {
                    Session.setCurrentUserId(usersModel.getId());
                    Session.setCurrentRole(usersModel.getStatus()); // Ini penting
                    openDashboard(usersModel);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Login gagal. Cek username dan password!", ButtonType.OK);
                    alert.showAndWait();
                }
            });
        }).start();
    }


    @FXML
    private void togglePasswordVisibility() {
        if (cbShowPassword.isSelected()) {
            txtPasswordVisible.setText(txtPassword.getText());
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);

            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
        } else {
            txtPassword.setText(txtPasswordVisible.getText());
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);

            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
        }
    }


    private void openDashboard(UsersModel usersModel) {
        loadingIndicator.setVisible(true);
        loadingIndicator.setManaged(true);
        btnLogin.setDisable(true);
        new Thread(() -> {
            try {
                Session.setCurrentUserId(usersModel.getId());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appsaldi/view/tampilanutama.fxml"));
                Parent root = loader.load();

                TampilanUtamaController dashboardController = loader.getController();

                Platform.runLater(() -> {
                    dashboardController.setUser(usersModel);
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    Scene scene = new Scene(root);

                    root.setOpacity(0);
                    root.setScaleX(0.8);
                    root.setScaleY(0.8);

                    stage.setScene(scene);
                    stage.centerOnScreen();

                    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);

                    ScaleTransition scaleIn = new ScaleTransition(Duration.millis(500), root);
                    scaleIn.setFromX(0.8);
                    scaleIn.setFromY(0.8);
                    scaleIn.setToX(1);
                    scaleIn.setToY(1);

                    fadeIn.play();
                    scaleIn.play();

                    fadeIn.setOnFinished(event -> {
                        loadingIndicator.setVisible(false);
                        loadingIndicator.setManaged(false);
                        btnLogin.setDisable(false);
                    });

                    stage.show();
                });

            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    loadingIndicator.setVisible(false);
                    loadingIndicator.setManaged(false);
                    btnLogin.setDisable(false);
                });
            }
        }).start();

    }

}