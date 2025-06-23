package com.example.appsaldi.controller.formcontroller;

import com.example.appsaldi.dao.GejalaDAO;
import com.example.appsaldi.model.Gejala;
import com.example.appsaldi.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;

public class FormGejalaController {

    @Setter
    private String idGejalaYangSedangDiedit;
    private GejalaDAO gejalaDAO = new GejalaDAO();

    @FXML private Label txtIdGejala;
    @FXML private TextArea txtNamaGejala;
    @FXML private Button btnBatalInput;

    @FXML
    private void handelSaveDataGejala() {
        String namaGejala = txtNamaGejala.getText();

        if (namaGejala.isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field wajib diisi!");
            return;
        }

        Gejala gejala = new Gejala(namaGejala);

        try {
            if (idGejalaYangSedangDiedit != null) {
                gejala.setId_gejala(Integer.parseInt(idGejalaYangSedangDiedit));
                gejalaDAO.updatetDataGejala(gejala);
                Notifications.create()
                        .title("Sukses")
                        .text("Data gejala berhasil diperbarui.")
                        .position(Pos.TOP_CENTER)
                        .hideAfter(Duration.seconds(10))
                        .showInformation();
                this.idGejalaYangSedangDiedit = String.valueOf(gejala.getId_gejala());
            } else {
                gejalaDAO.insertDataGejala(gejala);
                Notifications.create()
                        .title("Sukses")
                        .text("Data gejala berhasil disimpan.")
                        .position(Pos.TOP_CENTER)
                        .hideAfter(Duration.seconds(10))
                        .showInformation();
            }
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            Notifications.create()
                    .title("Gagal")
                    .text("Terjadi kesalahan saat menyimpan data gejala.")
                    .position(Pos.TOP_CENTER)
                    .hideAfter(Duration.seconds(10))
                    .showError();
        }
    }

    @FXML
    private void handelBatalInput() {closeForm();}

    public void setDataGejala(Gejala gejala) {
        this.idGejalaYangSedangDiedit = String.valueOf(gejala.getId_gejala());
        txtIdGejala.setText(this.idGejalaYangSedangDiedit);
        txtNamaGejala.setText(gejala.getNama_gejala());
    }

    public void resetForm() {
        txtIdGejala.setText("Auto");
        txtNamaGejala.setText("");
        idGejalaYangSedangDiedit = null;
    }

    private void closeForm() {
        Stage stage = (Stage) btnBatalInput.getScene().getWindow();
        stage.close();
    }

}
