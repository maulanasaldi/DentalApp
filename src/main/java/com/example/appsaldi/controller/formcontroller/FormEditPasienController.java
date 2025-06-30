package com.example.appsaldi.controller.formcontroller;
import com.example.appsaldi.controller.DataController;
import com.example.appsaldi.dao.PasienDAO;
import com.example.appsaldi.model.Pasien;
import com.example.appsaldi.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Setter;
import java.sql.SQLException;
import java.time.LocalDate;

public class FormEditPasienController {

    @Setter private DataController dataController;

    @FXML private VBox rootVBox;
    @FXML private Label txtIdPasien;
    @FXML private TextField txtNamaPasien, txtNIK, txtPekerjaan, txtNoTelepon;
    @FXML private TextArea txtAlamat;
    @FXML private DatePicker txtTglLahir;
    @FXML private Button btnBatal, btnSimpan;

    private Pasien pasienYangDiedit;
    private final PasienDAO pasienDAO = new PasienDAO();

    public void initialize() {
        rootVBox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            Rectangle clip = new Rectangle(newBounds.getWidth(), newBounds.getHeight());
            clip.setArcWidth(40);
            clip.setArcHeight(40);
            rootVBox.setClip(clip);
        });
    }

    public void setDataPasien(Pasien pasien) {
        this.pasienYangDiedit = pasien;
        txtIdPasien.setText(String.valueOf(pasien.getIdPasien()));
        txtNamaPasien.setText(pasien.getNamaPasien());
        txtNIK.setText(pasien.getNik());
        txtTglLahir.setValue(pasien.getTglLahirPasien());
        txtNoTelepon.setText(pasien.getNoTlpPasien());
        txtPekerjaan.setText(pasien.getPekerjaan());
        txtAlamat.setText(pasien.getAlamatPasien());
    }

    @FXML
    private void handleSave() {
        String nama = txtNamaPasien.getText();
        String nik = txtNIK.getText();
        String pekerjaan = txtPekerjaan.getText();
        String telepon = txtNoTelepon.getText();
        String alamat = txtAlamat.getText();
        LocalDate tglLahir = txtTglLahir.getValue();
        if (nama.isEmpty() || nik.isEmpty() || pekerjaan.isEmpty() || telepon.isEmpty() || alamat.isEmpty() || tglLahir == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field wajib diisi!");
            return;
        }
        pasienYangDiedit.setNamaPasien(nama);
        pasienYangDiedit.setNik(nik);
        pasienYangDiedit.setPekerjaan(pekerjaan);
        pasienYangDiedit.setNoTlpPasien(telepon);
        pasienYangDiedit.setAlamatPasien(alamat);
        pasienYangDiedit.setTglLahirPasien(tglLahir);
        try {
            pasienDAO.updatetDataPasien(pasienYangDiedit);
            AlertUtils.showNotificationSuccess("Data pasien berhasil diperbarui");
            if (dataController != null) {
                dataController.tampilkanDataPasien();
            }
            if (pasienYangDiedit.getIdPendaftaran() != null) {
                pasienDAO.updateStatusNotifPendaftaran(pasienYangDiedit.getIdPendaftaran());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showNotificationError("Terjadi kessalahan saat menyimpan perubahan.");
        }
    }

    @FXML
    private void handleBatal() {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }
}