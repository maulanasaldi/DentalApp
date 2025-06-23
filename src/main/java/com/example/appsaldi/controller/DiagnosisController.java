package com.example.appsaldi.controller;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.controller.poopupcontroller.PopupDataPasienController;
import com.example.appsaldi.controller.poopupcontroller.PopupDiagnosisResultController;
import com.example.appsaldi.dao.GejalaDAO;
import com.example.appsaldi.model.*;
import com.example.appsaldi.utils.AlertUtils;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DiagnosisController implements Initializable {

    @FXML private TableView<GejalaPilihan> tabelGejala;
    @FXML private TableColumn<GejalaPilihan, Boolean> kolomPilih;
    @FXML private TableColumn<GejalaPilihan, String> kolomGejala;

    @FXML private TableView<Gejala> tabelGejalaDipilih;
    @FXML private TableColumn<Gejala, String> kolomGejalaDipilih;
    @FXML private TableColumn<Gejala, Void> kolomAksi;

    @FXML private Button btnCari;
    @FXML private TextField txtCari, txtNIK, txtNamaPasien;
    @FXML private TextArea txtAlamat;
    @FXML private DatePicker txtTglLahir;

    private final ObservableList<GejalaPilihan> daftarGejala = FXCollections.observableArrayList();
    private final ObservableList<Gejala> daftarGejalaDipilih = FXCollections.observableArrayList();
    private final GejalaDAO gejalaDAO = new GejalaDAO();
    Connection connection = ConnectionDB.getConnection();
    private final DiagnosaModel diagnosaModel = new DiagnosaModel(connection);

    @Setter
    private TampilanUtamaController utamaController;

    @Setter
    private UsersModel usersModel;
    private Pasien selectedPasien;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupKolomTabel();
        isiDataGejala();
        setupPencarian();
    }

    private void setupKolomTabel() {
        // Kolom pilih dengan checkbox
        kolomPilih.setCellValueFactory(cell -> cell.getValue().selectedProperty());
        kolomPilih.setCellFactory(col -> new TableCell<GejalaPilihan, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setStyle("-fx-scale-x: 1; -fx-scale-y: 1;");
                checkBox.setAlignment(Pos.CENTER);

                checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isNowSelected) -> {
                    GejalaPilihan item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        item.setSelected(isNowSelected);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    GejalaPilihan currentItem = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(currentItem.isSelected());
                    setGraphic(checkBox);
                }
            }
        });

        DoubleBinding tableWidth = tabelGejala.widthProperty().subtract(17); // sisakan untuk scrollbar vertikal
        DoubleBinding tableWidtBinding = tabelGejalaDipilih.widthProperty().subtract(17);
        kolomPilih.prefWidthProperty().bind(tableWidth.multiply(0.150));
        kolomGejala.prefWidthProperty().bind(tableWidth.multiply(0.850));
        kolomGejalaDipilih.prefWidthProperty().bind(tableWidtBinding.multiply(0.780));
        kolomAksi.prefWidthProperty().bind(tableWidtBinding.multiply(0.220));

        kolomPilih.setEditable(true);
        tabelGejala.setEditable(true);

        // Kolom nama gejala di tabel gejala
        kolomGejala.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGejala().getNama_gejala()));
        kolomGejala.setCellFactory(col -> {
            TableCell<GejalaPilihan, String> cell = new TableCell<>() {
                private final Text text = new Text();
                {
                    text.wrappingWidthProperty().bind(col.widthProperty().subtract(10));
                    setGraphic(text);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    text.setText(empty ? null : item);
                }
            };
            return cell;
        });

        // Kolom gejala di tabel gejala dipilih
        kolomGejalaDipilih.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNama_gejala()));
        kolomGejalaDipilih.setCellFactory(col -> {
            TableCell<Gejala, String> cell = new TableCell<>() {
                private final Text text = new Text();
                {
                    text.wrappingWidthProperty().bind(col.widthProperty());
                    setGraphic(text);
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    text.setText(empty ? null : item);
                }
            };
            return cell;
        });

        // Kolom aksi untuk batal hapus gejala terpilih
        kolomAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnBatal = new Button("Batal");

            {
                btnBatal.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white;");
                btnBatal.setOnAction(e -> {
                    Gejala gejala = getTableView().getItems().get(getIndex());
                    // Cari GejalaPilihan yang sesuai lalu hapus centangnya
                    daftarGejala.stream()
                            .filter(gp -> gp.getGejala().equals(gejala))
                            .findFirst()
                            .ifPresent(gp -> gp.setSelected(false));

                    daftarGejalaDipilih.remove(gejala);
                    tabelGejalaDipilih.refresh();
                    tabelGejala.refresh();

                    // Fokus ke tombol diagnosa utama jika controller tersedia
                    if (utamaController != null) {
                        utamaController.btnDiagnosa.requestFocus();
                        utamaController.btnDiagnosa.setStyle("-fx-background-color: #D4EAF7; -fx-font-weight: bold;");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnBatal);
            }
        });
    }

    private void isiDataGejala() {
        daftarGejala.clear();
        daftarGejalaDipilih.clear();

        List<Gejala> semuaGejala = gejalaDAO.getAllGejala();

        for (Gejala g : semuaGejala) {
            GejalaPilihan gp = new GejalaPilihan(g);

            gp.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    if (!daftarGejalaDipilih.contains(g)) {
                        daftarGejalaDipilih.add(g);
                    }
                } else {
                    daftarGejalaDipilih.remove(g);
                }
                tabelGejalaDipilih.refresh();
            });

            daftarGejala.add(gp);
        }

        tabelGejala.setItems(daftarGejala);
        tabelGejalaDipilih.setItems(daftarGejalaDipilih);
    }

    private void setupPencarian() {
        txtCari.setText("Masukkan kata kunci...");

        txtCari.setOnMouseClicked(e -> {
            if (txtCari.getText().equals("Masukkan kata kunci...")) {
                txtCari.clear();
            }
        });

        txtCari.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus && txtCari.getText().isEmpty()) {
                txtCari.setText("Masukkan kata kunci...");
                tabelGejala.setItems(daftarGejala);
            }
        });

        txtCari.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.equals("Masukkan kata kunci...") || newText.isEmpty()) {
                tabelGejala.setItems(daftarGejala);
                return;
            }

            ObservableList<GejalaPilihan> filtered = FXCollections.observableArrayList();
            for (GejalaPilihan gp : daftarGejala) {
                if (gp.getGejala().getNama_gejala().toLowerCase().contains(newText.toLowerCase())) {
                    filtered.add(gp);
                }
            }
            tabelGejala.setItems(filtered);
        });
    }

    @FXML
    private void handleDiagnosisButton() {
        try {
            if (selectedPasien == null) {
                AlertUtils.showAlert(Alert.AlertType.WARNING, null, "Silahkan pilih pasien terlebih dahulu!");
                return;
            } else if (daftarGejalaDipilih.isEmpty()) {
                AlertUtils.showAlert(Alert.AlertType.WARNING, null, "Silahkan pilih gejala terlebih dahulu! minimal satu gejala.");
                return;
            }

            List<Integer> daftarIdPenyakit = diagnosaModel.diagnosaPenyakit(daftarGejalaDipilih);

            if (daftarIdPenyakit.isEmpty()) {
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, null, "Tidak ditemukan penyakit yang cocok dengan gejala yang dipilih.");
            }

            int idPasien = selectedPasien.getIdPasien();
            String namaPasien = selectedPasien.getNamaPasien();

            List<String> daftarNamaPenyakit = new ArrayList<>();
            List<String> daftarSolusi = new ArrayList<>();

            for (Integer idPenyakit : daftarIdPenyakit) {
                String namaPenyakit = diagnosaModel.getNamaPenyakitById(idPenyakit);
                String solusi = diagnosaModel.getSolusiById(idPenyakit);

                daftarNamaPenyakit.add(namaPenyakit != null ? namaPenyakit : "Tidak diketahui");
                daftarSolusi.add(solusi != null ? solusi : "-");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appsaldi/view/popup/popuphasildiagnosa.fxml"));
            Parent root = loader.load();

            PopupDiagnosisResultController controller = loader.getController();
            controller.setDiagnosisController(this);
            controller.setUsersModel(usersModel);
            controller.setData(idPasien, namaPasien, daftarNamaPenyakit, daftarSolusi);
            controller.setUtamaController(utamaController);
            List<Integer> idGejalaList = daftarGejalaDipilih.stream()
                    .map(Gejala::getId_gejala)
                    .toList();

            controller.setDiagnosisData(selectedPasien.getIdPasien(), daftarIdPenyakit, idGejalaList, diagnosaModel);

            Stage mainStage = (Stage) btnCari.getScene().getWindow();
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


    @FXML
    private void showPopupDataPasien(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appsaldi/view/popup/popupdatapasien.fxml"));
            Parent root = loader.load();
            PopupDataPasienController popupDataPasienController = loader.getController();
            popupDataPasienController.setListener(this::setDataPasein);

            Stage mainStage = (Stage) btnCari.getScene().getWindow();

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

    @FXML
    private void reset() {
        resetDataDiagnosa();
    }

    public void resetDataDiagnosa() {
        txtNIK.setText("");
        txtNamaPasien.setText("");
        txtTglLahir.setValue(null);
        txtAlamat.setText("");
        daftarGejalaDipilih.clear();
        daftarGejala.forEach(gp -> gp.setSelected(false));
        tabelGejalaDipilih.refresh();
        tabelGejala.refresh();
    }

    public void setDataPasein(Pasien pasien) {
        this.selectedPasien = pasien;
        txtNIK.setText(pasien.getNik());
        txtNamaPasien.setText(pasien.getNamaPasien());
        txtAlamat.setText(pasien.getAlamatPasien());
        txtTglLahir.setValue(pasien.getTglLahirPasien());
    }

}
