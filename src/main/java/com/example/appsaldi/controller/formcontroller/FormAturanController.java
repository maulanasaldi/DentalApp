package com.example.appsaldi.controller.formcontroller;

import com.example.appsaldi.controller.DataController;
import com.example.appsaldi.controller.poopupcontroller.PopupDataPenyakitController;
import com.example.appsaldi.controller.poopupcontroller.PopupMode;
import com.example.appsaldi.controller.poopupcontroller.PopupMultiSelectController;
import com.example.appsaldi.controller.poopupcontroller.PopupSingleSelectController;
import com.example.appsaldi.dao.KnowladgeDAO;
import com.example.appsaldi.model.Aturan;
import com.example.appsaldi.model.Gejala;
import com.example.appsaldi.model.Penyakit;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FormAturanController {

    @Setter private String idAturanYangSedangDiedit;
    @Setter private DataController dataController;

    @FXML private Label txtIDAturan;
    @FXML private TextField txtIDPenyakit;
    @FXML private TextArea txtIDGejala;
    @FXML private Button btnSimpan, btnBatal;

    private KnowladgeDAO aturanDAO = new KnowladgeDAO();
    private List<Integer> idGejalaList;
    private boolean isGejalaDiubah = false;

    @FXML
    private void handelSaveDataAturan() {
        String idPenyakitText = txtIDPenyakit.getText();
        String idGejalaText = txtIDGejala.getText();
        if (idPenyakitText.isEmpty() || idGejalaText.isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field wajib diisi!");
            return;
        }
        try {
            int idPenyakit = Integer.parseInt(idPenyakitText);
            List<String> daftarGejala = Arrays.stream(idGejalaText.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            Aturan aturan = new Aturan();
            aturan.setIdPenyakit(idPenyakit);
            aturan.setDaftarGejala(daftarGejala);
            if (idAturanYangSedangDiedit != null) {
                aturan.setIdAturan(Integer.parseInt(idAturanYangSedangDiedit));
                aturanDAO.updateDataAturan(aturan.getIdAturan(), aturan);
                AlertUtils.showNotificationSuccess("Data aturan berhasil dirubah");
            } else {
                aturanDAO.insertDataAturan(aturan);
                AlertUtils.showNotificationSuccess("Data aturan berhasil disimpan");
            }
            txtIDPenyakit.clear();
            txtIDGejala.clear();
            idAturanYangSedangDiedit = null;
        } catch (NumberFormatException e) {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "ID Penyakit dan Gejala harus berupa angka dipisah koma.");
        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showNotificationError("Terjadi kesalah saat menyimpan data aturan");
        }
    }

    @FXML
    private void showPopupDataPenyakit() {
        showPopup("/com/example/appsaldi/view/popup/popupdatapenyakit.fxml", this::setDataPenyakit, PopupMode.FORM_ATURAN);
    }

    @FXML
    private void showPopupDataGejala() {
        showPopupMulti("/com/example/appsaldi/view/popup/popupdatagejala.fxml", this::setDataGejala);
    }

    @FXML
    private void handelBatalInput() {closeForm();}

    public void setDataPenyakit(Penyakit penyakit) {
        txtIDPenyakit.setText(String.valueOf(penyakit.getIdPenyakit()));
    }

    public void setDataGejala(List<Gejala> gejalaList) {
        StringBuilder sb = new StringBuilder();
        List<Integer> idList = new ArrayList<>();
        idGejalaList = new ArrayList<>();
        for (Gejala p : gejalaList) {
            sb.append(p.getId_gejala()).append(", ");
            idList.add(p.getId_gejala());
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        txtIDGejala.setText(sb.toString());
        this.idGejalaList = idList;
        isGejalaDiubah = true;
    }

    public void setDataAturan(Aturan aturan) {
        this.idAturanYangSedangDiedit = String.valueOf(aturan.getIdAturan());
        txtIDPenyakit.setText(String.valueOf(aturan.getIdPenyakit()));
        List<String> daftarGejala = aturan.getDaftarGejala();
        if (daftarGejala != null && !daftarGejala.isEmpty()) {
            String txtGejala = String.join(", ", daftarGejala);
            txtIDGejala.setText(txtGejala);
        } else {
            txtIDGejala.clear();
        }
        isGejalaDiubah = false;
    }

    private <T> void showPopup(String fxmlPath, Consumer<T> onDataSelected, PopupMode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            T popupController = loader.getController();
            if (popupController instanceof PopupDataPenyakitController) {
                ((PopupDataPenyakitController) popupController).setMode(mode);
                ((PopupDataPenyakitController) popupController).setListener((Consumer<List<Penyakit>>) selected -> {
                    if (!selected.isEmpty()) {
                        onDataSelected.accept((T) selected.get(0)); // Ambil satu saja
                    }
                });
            } else if (popupController instanceof PopupSingleSelectController<?>) {
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

    private void closeForm() {
        Stage stage = (Stage) btnBatal.getScene().getWindow();
        stage.close();
    }
}