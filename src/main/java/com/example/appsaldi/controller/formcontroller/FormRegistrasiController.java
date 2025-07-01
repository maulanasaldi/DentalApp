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
import java.sql.SQLException;
import java.time.LocalDate;

public class FormRegistrasiController {

    @Setter private DataController dataController;
    @Setter private boolean isFromCariPopup = false;

    @FXML private VBox rootVBox;
    @FXML private Label txtIdPasien;
    @FXML private TextField txtNamaPasien, txtNIK, txtPekerjaan, txtNoTelepon;
    @FXML private TextArea txtAlamat;
    @FXML private DatePicker txtTglLahir;
    @FXML private Button btnBatal, btnCariDataPasien;

    private PasienDAO pasienDAO = new PasienDAO();
    private Integer idPasienUntukDaftarUlang = null;

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

        try {
            if (idPasienUntukDaftarUlang != null) {
                new com.example.appsaldi.dao.PendaftaranDAO().insert(idPasienUntukDaftarUlang);
                AlertUtils.showNotificationSuccess("Pasien lama berhasil mendaftar ulang.");
            } else {
                if (pasienDAO.isNikExist(nik)) {
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "NIK Duplikat", "NIK sudah terdaftar sebagai pasien!");
                    return;
                }
                Pasien pasien = new Pasien(namaPasien, tglLahirPasien, nik, pekerjaanPasien, noTlpPasien, alamat);
                int idBaru = pasienDAO.insertDataPasien(pasien);
                new com.example.appsaldi.dao.PendaftaranDAO().insert(idBaru);
                AlertUtils.showNotificationSuccess("Pasien baru berhasil didaftarkan.");
            }
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showNotificationSuccess("Terjadi kesalahan saat menyimpan data.");
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
            popupController.setListener(this::setDataPasienLama);
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
        setupButtonCariVisibility();
    }

    public void setDataPasienLama(Pasien pasien) {
        idPasienUntukDaftarUlang = pasien.getIdPasien();
        txtIdPasien.setText(String.valueOf(pasien.getIdPasien()));
        txtNamaPasien.setText(pasien.getNamaPasien());
        txtNIK.setText(pasien.getNik());
        txtTglLahir.setValue(pasien.getTglLahirPasien());
        txtNoTelepon.setText(pasien.getNoTlpPasien());
        txtPekerjaan.setText(pasien.getPekerjaan());
        txtAlamat.setText(pasien.getAlamatPasien());

        txtNamaPasien.setDisable(true);
        txtNIK.setDisable(true);
        txtTglLahir.setDisable(true);
        txtNoTelepon.setDisable(true);
        txtPekerjaan.setDisable(true);
        txtAlamat.setDisable(true);
    }

    public void resetForm() {
        txtIdPasien.setText("Auto");
        txtNamaPasien.setText("");
        txtTglLahir.setValue(null);
        txtNIK.setText("");
        txtPekerjaan.setText("");
        txtNoTelepon.setText("");
        txtAlamat.setText("");
        idPasienUntukDaftarUlang = null;

        txtNamaPasien.setDisable(false);
        txtNIK.setDisable(false);
        txtTglLahir.setDisable(false);
        txtNoTelepon.setDisable(false);
        txtPekerjaan.setDisable(false);
        txtAlamat.setDisable(false);
    }

    public void setupButtonCariVisibility() {
        btnCariDataPasien.setVisible(isFromCariPopup);
        btnCariDataPasien.setManaged(isFromCariPopup);
    }
}

