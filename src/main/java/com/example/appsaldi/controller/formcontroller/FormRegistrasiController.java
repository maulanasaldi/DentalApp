package com.example.appsaldi.controller.formcontroller;

import com.example.appsaldi.controller.DataController;
import com.example.appsaldi.controller.poopupcontroller.PopupDataPasienController;
import com.example.appsaldi.dao.PasienDAO;
import com.example.appsaldi.model.Pasien;
import com.example.appsaldi.utils.AlertUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import org.controlsfx.control.Notifications;
import javafx.geometry.Pos;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormRegistrasiController {

    @FXML private VBox rootVBox;
    @FXML private Label txtIdPasien;
    @FXML private TextField txtNamaPasien, txtNIK, txtPekerjaan, txtNoTelepon;
    @FXML private TextArea txtAlamat;
    @FXML private DatePicker txtTglLahir;
    @FXML private Button btnBatal, btnCariDataPasien;

    private PasienDAO pasienDAO = new PasienDAO();

    @Setter
    private String idPasienYangSedangDiedit;
    @Setter
    private DataController dataController;

    @FXML
    private void handleSave() {
        String namaPasien = txtNamaPasien.getText();
        LocalDate tglLahirPasien = txtTglLahir.getValue();
        String nik = txtNIK.getText();
        String pekerjaanPasien = txtPekerjaan.getText();
        String noTlpPasien = txtNoTelepon.getText();
        String alamat = txtAlamat.getText();

        if (namaPasien.isEmpty() || tglLahirPasien == null
                || nik.isEmpty() || pekerjaanPasien.isEmpty()
                || noTlpPasien.isEmpty() || alamat.isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field wajib diisi!");
            return;
        }

        Pasien pasien = new Pasien(namaPasien, tglLahirPasien, nik, pekerjaanPasien, noTlpPasien, alamat);

        try {
            if (idPasienYangSedangDiedit != null) {
                pasien.setIdPasien(Integer.valueOf(idPasienYangSedangDiedit));
                pasienDAO.updatetDataPasien(pasien);
                Notifications.create()
                        .title("Sukses")
                        .text("Data pasien berhasil diperbarui.")
                        .position(Pos.TOP_CENTER)
                        .hideAfter(Duration.seconds(10))
                        .showInformation();
                this.idPasienYangSedangDiedit = String.valueOf(pasien.getIdPasien());
            } else {
                pasienDAO.insertDataPasien(pasien);
                Notifications.create()
                        .title("Sukses")
                        .text("Data pasien berhasil disimpan.")
                        .position(Pos.TOP_CENTER)
                        .hideAfter(Duration.seconds(10))
                        .showInformation();
            }
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            Notifications.create()
                    .title("Gagal")
                    .text("Terjadi kesalahan saat menyimpan data pasien.")
                    .position(Pos.TOP_CENTER)
                    .hideAfter(Duration.seconds(10))
                    .showError();
        }
    }

    @FXML
    private void handleBatal() {
        closeForm();
    }

    @FXML
    private void showPopupDataPasien() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appsaldi/view/popup/popupdatapasien.fxml"));
            Parent root = loader.load();

            PopupDataPasienController popupController = loader.getController();
            popupController.setListener(this::setDataPasein);

            Stage mainStage = (Stage) btnCariDataPasien.getScene().getWindow();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(mainStage);
            popupStage.initStyle(StageStyle.TRANSPARENT);

            mainStage.getScene().getRoot().setEffect(new GaussianBlur(10));

            popupStage.setOnHidden(e -> mainStage.getScene().getRoot().setEffect(null));

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);

            popupStage.centerOnScreen();

            Platform.runLater(() -> {
                popupStage.setX(mainStage.getX() + (mainStage.getWidth() / 2) - (popupStage.getWidth() / 2));
                popupStage.setY(mainStage.getY() + (mainStage.getHeight() / 2) - (popupStage.getHeight() / 2));
            });

            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeForm() {
        if (dataController != null) {
            dataController.tampilkanDataPasien();
        }
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
        rootVBox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            Rectangle clip = new Rectangle(newBounds.getWidth(), newBounds.getHeight());
            clip.setArcWidth(40);
            clip.setArcHeight(40);
            rootVBox.setClip(clip);
        });
    }

    public void setDataPasein(Pasien pasien) {
        this.idPasienYangSedangDiedit = String.valueOf(pasien.getIdPasien());
        txtIdPasien.setText(this.idPasienYangSedangDiedit);
        txtNamaPasien.setText(pasien.getNamaPasien());
        txtNIK.setText(pasien.getNik());
        txtTglLahir.setValue(pasien.getTglLahirPasien());
        txtNoTelepon.setText(pasien.getNoTlpPasien());
        txtPekerjaan.setText(pasien.getPekerjaan());
        txtAlamat.setText(pasien.getAlamatPasien());
    }

    public void resetForm() {
        txtIdPasien.setText("Auto");
        txtNamaPasien.setText("");
        txtTglLahir.setValue(null);
        txtNIK.setText("");
        txtPekerjaan.setText("");
        txtNoTelepon.setText("");
        txtAlamat.setText("");
        idPasienYangSedangDiedit = null;
    }

}
