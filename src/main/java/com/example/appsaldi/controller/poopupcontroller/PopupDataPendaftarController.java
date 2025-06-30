package com.example.appsaldi.controller.poopupcontroller;
import com.example.appsaldi.controller.DataController;
import com.example.appsaldi.controller.DiagnosisController;
import com.example.appsaldi.controller.PasienSelectionListener;
import com.example.appsaldi.controller.formcontroller.FormRegistrasiController;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class PopupDataPendaftarController implements PopupSingleSelectController<Pasien> {

    @Setter private FormRegistrasiController parentFormRegistrasiController;
    @Setter private DiagnosisController parentDiagnosaController;
    @Setter private DataController parentDataController;
    @Setter private FormRiwayatController parentFormRiwayatController;

    @FXML private Button btnKeluar;
    @FXML private TextField txtCariDataPasien;
    @FXML private TableView<Pasien> tblDataPasien;

    private PasienSelectionListener listener;
    private ObservableList<Pasien> masterData = FXCollections.observableArrayList();
    private Consumer<Pasien> pasienConsumer;

    @FXML
    private void initialize() {
        TableColumn<Pasien, String> colIdPendaftaran = new TableColumn<>("ID Pendaftaran");
        colIdPendaftaran.setCellValueFactory(new PropertyValueFactory<>("idPendaftaran"));
        TableColumn<Pasien, String> colNama = new TableColumn<>("Nama Pasien");
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        TableColumn<Pasien, String> colTglDaftar = new TableColumn<>("Tanggal Daftar");
        colTglDaftar.setCellValueFactory(cellData -> {
            Timestamp tglPendaftaran = cellData.getValue().getTglPendaftaran();
            if (tglPendaftaran != null) {
                LocalDate tgl = tglPendaftaran.toLocalDateTime().toLocalDate();
                return new SimpleStringProperty(formatTanggal(tgl));
            } else {
                return new SimpleStringProperty("-");
            }
        });
        tblDataPasien.getColumns().addAll(colIdPendaftaran, colNama, colTglDaftar);
        loadDataFormDatabase();
        tblDataPasien.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1) {
                Pasien selected = tblDataPasien.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    if (listener != null) {
                        listener.onPasienSelected(selected);
                    } else if (parentFormRegistrasiController != null) {
                        parentFormRegistrasiController.setDataPasienLama(selected);
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
        masterData.setAll(pasienDAO.getPasienBelumDidiagnosa());
        FilteredList<Pasien> filteredData = new FilteredList<>(masterData, b -> true);
        txtCariDataPasien.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pasien -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(pasien.getIdPasien()).toLowerCase().contains(lowerCaseFilter)
                        || pasien.getNamaPasien().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Pasien> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblDataPasien.comparatorProperty());
        tblDataPasien.setItems(sortedData);
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
