package com.example.appsaldi.controller.formcontroller;

import com.example.appsaldi.controller.DataController;
import com.example.appsaldi.controller.poopupcontroller.PopupMultiSelectController;
import com.example.appsaldi.controller.poopupcontroller.PopupSingleSelectController;
import com.example.appsaldi.dao.RiwayatDAO;
import com.example.appsaldi.model.*;

import com.example.appsaldi.utils.AlertUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class FormRiwayatController {

    @Setter
    private Integer idRiwayatYangSedangDiedit;

    @FXML private TextField txtNamaPasien;
    @FXML private TextArea txtPenyakit, txtGejala;
    @FXML private DatePicker dpTanggal;
    @FXML private Button btnSimpan, btnBatal;

    @Setter private int idPasienYangSedangDiedit;
    @Setter private DataController dataController;

    private Riwayat riwayatAwal = null;
    private List<Integer> idPenyakitList;
    private List<Integer> idGejalaList;
    private boolean isPenyakitDiubah = false;
    private boolean isGejalaDiubah = false;
    private boolean isPaseinDiubah = false;



    private RiwayatDAO riwayatDAO = new RiwayatDAO();

    @FXML
    private void initialize() {
        btnSimpan.setOnAction(event -> simpanPerubahan());
        btnBatal.setOnAction(event -> closeWindow());
    }

    public void setRiwayat(Riwayat r) {
        if (r != null) {
            this.riwayatAwal = r;
            this.idRiwayatYangSedangDiedit = r.getIdRiwayat();
            this.idPasienYangSedangDiedit = r.getIdPasien();
            this.idPenyakitList = r.getIdPenyakitList();
            this.idGejalaList = r.getIdGejalaList();

            txtNamaPasien.setText(r.getNamaPasien());
            txtPenyakit.setText(String.valueOf(r.getDaftarPenyakit()));
            txtGejala.setText(String.valueOf(r.getDaftarGejala()));
            dpTanggal.setValue(r.getTglDiagnosa());
        }
    }

    private void simpanPerubahan() {
        String namaPasien = isPaseinDiubah ? txtNamaPasien.getText() : riwayatAwal.getNamaPasien();
        int idPasien = isPaseinDiubah ? idPasienYangSedangDiedit : riwayatAwal.getIdPasien();
        String penyakit = txtPenyakit.getText();
        String gejala = txtGejala.getText();
        LocalDate tanggal = dpTanggal.getValue();

        if (namaPasien.isEmpty() || penyakit.isEmpty() || gejala.isEmpty() || tanggal == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field wajib diisi!");
            return;
        }

        Riwayat riwayat = new Riwayat();
        riwayat.setNamaPasien(namaPasien);
        riwayat.setIdPasien(idPasien);
        riwayat.setDaftarPenyakit(Arrays.asList(penyakit.split(",\\s*")));
        riwayat.setIdPenyakitList(idPenyakitList);
        riwayat.setDaftarGejala(Arrays.asList(gejala.split(",\\s*")));
        riwayat.setIdGejalaList(idGejalaList);
        riwayat.setTglDiagnosa(tanggal);

        if (idRiwayatYangSedangDiedit != null) { // Mode Edit
            riwayat.setIdRiwayat(idRiwayatYangSedangDiedit);
            riwayat.setIdPenyakitList(idPenyakitList);
            riwayat.setIdGejalaList(idGejalaList);
            riwayatDAO.updateRiwayat(riwayat, isPenyakitDiubah, isGejalaDiubah);
            Notifications.create()
                    .title("Sukses")
                    .text("Data riwayat berhasil diperbarui.")
                    .position(Pos.TOP_CENTER)
                    .hideAfter(javafx.util.Duration.seconds(5))
                    .showInformation();
        }

        if (dataController != null) {
            dataController.tampilkanRiwayatDiagnosa();
        }
        closeWindow();
    }

    @FXML
    private void showPopupDataPasien() {
        showPopup("/com/example/appsaldi/view/popup/popupdatapasien.fxml", this::setDataPasein);
    }

    @FXML
    private void showPopupDataPenyakit() {
        showPopupMulti("/com/example/appsaldi/view/popup/popupdatapenyakit.fxml", this::setDataPenyakit);
    }

    @FXML
    private void showPopupDataGejala() {
        showPopupMulti("/com/example/appsaldi/view/popup/popupdatagejala.fxml", this::setDataGejala);
    }

    public void setDataPasein(Pasien pasien) {
        txtNamaPasien.setText(pasien.getNamaPasien());
        idPasienYangSedangDiedit = pasien.getIdPasien();
        isPaseinDiubah = true;
    }

    public void setDataPenyakit(List<Penyakit> penyakitList) {
        StringBuilder sb = new StringBuilder();
        List<Integer> idList = new ArrayList<>();

        idPenyakitList = new ArrayList<>();
        for (Penyakit p : penyakitList) {
            sb.append(p.getNamaPenyakit()).append(", ");
            idList.add(p.getIdPenyakit());
        }

        // Hapus koma terakhir
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        txtPenyakit.setText(sb.toString());
        this.idPenyakitList = idList;

        isPenyakitDiubah = true;
    }


    public void setDataGejala(List<Gejala> gejalaList) {
        StringBuilder sb = new StringBuilder();
        List<Integer> idList = new ArrayList<>();

        idGejalaList = new ArrayList<>();
        for (Gejala p : gejalaList) {
            sb.append(p.getNama_gejala()).append(", ");
            idList.add(p.getId_gejala());
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        txtGejala.setText(sb.toString());
        this.idGejalaList = idList;

        isGejalaDiubah = true;
    }


    private void closeWindow() {
        Stage stage = (Stage) btnSimpan.getScene().getWindow();
        stage.close();
    }

    private <T> void showPopup(String fxmlPath, Consumer<T> onDataSelected) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            T popupController = loader.getController();
            if (popupController instanceof PopupSingleSelectController<?>) {
                ((PopupSingleSelectController<T>) popupController).setListener(onDataSelected);
            }

            Stage mainStage = (Stage) btnSimpan.getScene().getWindow();
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

    private <T> void showPopupMulti(String fxmlPath, Consumer<java.util.List<T>> onDataSelected) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            T popupController = loader.getController();
            if (popupController instanceof PopupMultiSelectController<?>) {
                ((PopupMultiSelectController<T>) popupController).setListener(onDataSelected);
            }

            Stage mainStage = (Stage) btnSimpan.getScene().getWindow();
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

}
