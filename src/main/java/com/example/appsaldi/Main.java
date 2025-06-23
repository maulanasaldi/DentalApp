package com.example.appsaldi;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/appsaldi/view/login.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/appsaldi/css/style-notifikasi.css").toExternalForm());

            primaryStage.setTitle("Dental Care");
            primaryStage.getIcons().add(
                new javafx.scene.image.Image(
                        getClass().getResourceAsStream("/com/example/appsaldi/img/Logo S.png")
                )
            );
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}