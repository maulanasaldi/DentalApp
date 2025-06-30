package com.example.appsaldi.controller;
import com.example.appsaldi.model.UsersModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.Setter;

public class PengaturanController {

    @FXML private Button btnProfileAkun, btnLogOut, btnTentangAplikasi;

    @Setter private UsersModel currentUser;
    @Setter private TampilanUtamaController utamaController;

    @FXML
    private void initialize() {
        btnProfileAkun.setOnAction(event -> utamaController.bukaProfileAkun());
        btnTentangAplikasi.setOnAction(event -> utamaController.bukaTantangAplikasi());
        btnLogOut.setOnAction(event -> utamaController.showLogin());
    }
}
