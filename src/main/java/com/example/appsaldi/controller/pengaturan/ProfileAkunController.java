package com.example.appsaldi.controller.pengaturan;

import com.example.appsaldi.controller.TampilanUtamaController;
import com.example.appsaldi.model.UsersModel;
import com.example.appsaldi.dao.UserDAO;
import com.example.appsaldi.utils.AlertUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import lombok.Setter;

import java.io.File;

public class ProfileAkunController {

    @Setter private UsersModel currentUser;
    @Setter private TampilanUtamaController utamaController;

    @FXML private TextField txtNama, txtUserName, txtKataSandi, txtPathFotoProfile;
    @FXML private Button btnEditFotoProfile, btnSimpanPerubahan;

    public void initFormData() {
        if (currentUser != null) {
            txtNama.setText(currentUser.getNama());
            txtUserName.setText(currentUser.getUsername());
            txtPathFotoProfile.setText(currentUser.getFotoPath());
        }
    }

    @FXML
    private void initialize() {
        btnEditFotoProfile.setOnAction(e -> pilihFoto());
        btnSimpanPerubahan.setOnAction(e -> simpanPerubahan());
    }

    private void pilihFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            txtPathFotoProfile.setText(selectedFile.getAbsolutePath());
        }
    }

    private void simpanPerubahan() {
        String namaBaru = txtNama.getText();
        String userNameBaru = txtUserName.getText();
        String passwordBaru = txtKataSandi.getText();
        String pathFoto = txtPathFotoProfile.getText();

        if (namaBaru.isEmpty()) {
            AlertUtils.showNotificationWarning("Nama tidak boleh kosong.");
            return;
        }
        currentUser.setNama(namaBaru);
        currentUser.setUsername(userNameBaru);
        if (!passwordBaru.isEmpty()) {
            currentUser.setPassword(passwordBaru);
        }
        currentUser.setFotoPath(pathFoto);

        UserDAO userDAO = new UserDAO();
        boolean berhasil = userDAO.updateUser(currentUser);

        if (berhasil) {
            AlertUtils.showNotificationSuccess("Perubahan berhasil disimpan");
            txtKataSandi.clear();
            UserDAO userDAO1 = new UserDAO();
            UsersModel updateUser = userDAO1.getById(String.valueOf(currentUser.getId()));
            utamaController.setUser(updateUser);
        } else {
            AlertUtils.showNotificationError("Gagal menyimpan perubahan.");
        }
    }
}
