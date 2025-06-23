package com.example.appsaldi.controller.formcontroller;

import com.example.appsaldi.dao.PenyakitDAO;
import com.example.appsaldi.model.Penyakit;
import com.example.appsaldi.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;
import org.controlsfx.control.Notifications;

import java.sql.SQLException;

public class FormPenyakitController {

    @Setter
    private String idPenyakitYangSedangDiedit;
    private PenyakitDAO penyakitDAO = new PenyakitDAO();

    @FXML private TextField txtNamaPenyakit;
    @FXML private TextArea txtSolusi;
    @FXML private Button btnBatalInput;

    @FXML
    private void handelSaveDataPenyakit() {
        String namaPenyakit = txtNamaPenyakit.getText();
        String solusi = txtSolusi.getText();

        if (namaPenyakit.isEmpty() || solusi.isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field wajib diisi!");
            return;
        }

        Penyakit penyakit = new Penyakit(namaPenyakit, solusi);

        try {
            if (idPenyakitYangSedangDiedit != null) {
                penyakit.setIdPenyakit(Integer.parseInt(idPenyakitYangSedangDiedit));
                penyakitDAO.updatetDataPenyakit(penyakit);
                Notifications.create()
                        .title("Sukses")
                        .text("Data penyakit berhasil diperbarui.")
                        .position(Pos.TOP_CENTER)
                        .hideAfter(Duration.seconds(10))
                        .showInformation();
                this.idPenyakitYangSedangDiedit = String.valueOf(penyakit.getIdPenyakit());
            } else {
                penyakitDAO.insertDataPenyakit(penyakit);
                Notifications.create()
                        .title("Sukses")
                        .text("Data penyakit berhasil disimpan.")
                        .position(Pos.TOP_CENTER)
                        .hideAfter(Duration.seconds(10))
                        .showInformation();
            }
            txtNamaPenyakit.setText("");
            txtSolusi.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            Notifications.create()
                    .title("Gagal")
                    .text("Terjadi kesalahan saat menyimpan data penyakit.")
                    .position(Pos.TOP_CENTER)
                    .hideAfter(Duration.seconds(10))
                    .showError();
        }
    }

    @FXML
    private void handelBatalInput() {closeForm();}

    public void setDataPenyakit(Penyakit penyakit) {
        this.idPenyakitYangSedangDiedit = String.valueOf(penyakit.getIdPenyakit());
        txtNamaPenyakit.setText(penyakit.getNamaPenyakit());
        txtSolusi.setText(penyakit.getSolusi());
    }

    private void closeForm() {
        Stage stage = (Stage) btnBatalInput.getScene().getWindow();
        stage.close();
    }

}
