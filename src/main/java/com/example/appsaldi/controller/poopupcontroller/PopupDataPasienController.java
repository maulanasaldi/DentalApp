package com.example.appsaldi.controller.poopupcontroller;

import com.example.appsaldi.controller.DataController;
import com.example.appsaldi.controller.DiagnosisController;
import com.example.appsaldi.controller.formcontroller.FormRegistrasiController;
import com.example.appsaldi.controller.PasienSelectionListener;
import com.example.appsaldi.controller.formcontroller.FormRiwayatController;
import com.example.appsaldi.dao.PasienDAO;
import com.example.appsaldi.model.Pasien;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class PopupDataPasienController implements PopupSingleSelectController<Pasien> {

    @FXML private Button btnKeluar;
    @FXML private TextField txtCariDataPasien;
    @FXML private TableView<Pasien> tblDataPasien;

    private PasienSelectionListener listener;
    @Setter
    private FormRegistrasiController parentFormRegistrasiController;
    @Setter
    private DiagnosisController parentDiagnosaController;
    @Setter
    private DataController parentDataController;
    @Setter private FormRiwayatController parentFormRiwayatController;

    private ObservableList<Pasien> masterData = FXCollections.observableArrayList();
    private Consumer<Pasien> pasienConsumer;

    @FXML
    private void initialize() {
        TableColumn<Pasien, String> colIdPasien = new TableColumn<>("ID Pasien");
        colIdPasien.setCellValueFactory(new PropertyValueFactory<>("idPasien"));

        TableColumn<Pasien, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));

        TableColumn<Pasien, String> colTglLahirPasien = new TableColumn<>("Tanggal Lahir");
        colTglLahirPasien.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTglLahirPasien() != null) {
                return new SimpleStringProperty(formatTanggal(cellData.getValue().getTglLahirPasien()));
            } else {
                return new SimpleStringProperty("-");
            }
        });

        TableColumn<Pasien, String> colNIK = new TableColumn<>("NIK");
        colNIK.setCellValueFactory(new PropertyValueFactory<>("nik"));

        TableColumn<Pasien, String> colPekerjaan = new TableColumn<>("Pekerjaan");
        colPekerjaan.setCellValueFactory(new PropertyValueFactory<>("pekerjaan"));

        TableColumn<Pasien, String> colNoTlpPasien = new TableColumn<>("No. Telepon");
        colNoTlpPasien.setCellValueFactory(new PropertyValueFactory<>("noTlpPasien"));

        TableColumn<Pasien, String> colAlamat = new TableColumn<>("Alamat");
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamatPasien"));


        tblDataPasien.getColumns().addAll(colIdPasien, colNama, colTglLahirPasien, colNIK, colPekerjaan, colNoTlpPasien, colAlamat);

        loadDataFormDatabase();

        tblDataPasien.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1) {
                Pasien selected = tblDataPasien.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if (listener != null) {
                        listener.onPasienSelected(selected);
                    } else if (parentFormRegistrasiController != null) {
                        parentFormRegistrasiController.setDataPasein(selected);
                    } else if (parentDiagnosaController != null) {
                        parentDiagnosaController.setDataPasein(selected);
                    } else if (parentFormRiwayatController != null) {
                        parentFormRiwayatController.setDataPasein(selected);
                    }
                    if (pasienConsumer != null) {
                        pasienConsumer.accept(selected);
                    }
                    Stage stage = (Stage) tblDataPasien.getScene().getWindow();
                    stage.close();
                }
            }
        });
    }

    public void loadDataFormDatabase() {
        PasienDAO pasienDAO = new PasienDAO();
        try {
            masterData.setAll(pasienDAO.getAllPasien());

            // Buat FilteredList dari masterData
            FilteredList<Pasien> filteredData = new FilteredList<>(masterData, b -> true);

            // Tambahkan listener pada TextField untuk pencarian
            txtCariDataPasien.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(pasien -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    return String.valueOf(pasien.getIdPasien()).toLowerCase().contains(lowerCaseFilter)
                            || pasien.getNamaPasien().toLowerCase().contains(lowerCaseFilter)
                            || pasien.getNik().toLowerCase().contains(lowerCaseFilter)
                            || pasien.getPekerjaan().toLowerCase().contains(lowerCaseFilter)
                            || pasien.getNoTlpPasien().toLowerCase().contains(lowerCaseFilter)
                            || pasien.getAlamatPasien().toLowerCase().contains(lowerCaseFilter);
                });
            });

            // Bungkus FilteredList dengan SortedList agar bisa sorting juga
            SortedList<Pasien> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblDataPasien.comparatorProperty());

            // Set data ke tabel
            tblDataPasien.setItems(sortedData);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatTanggal(LocalDate tanggal) {
        if (tanggal != null) {
            return tanggal.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } else {
            return "-";
        }
    }


    @FXML
    private void handelKeluar() {
        Stage stage = (Stage) btnKeluar.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setListener(Consumer<Pasien> listener) {
        this.pasienConsumer = listener;
    }
}
