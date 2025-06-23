package com.example.appsaldi.controller;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.dao.GejalaDAO;
import com.example.appsaldi.dao.PasienDAO;
import com.example.appsaldi.dao.PenyakitDAO;
import com.example.appsaldi.dao.RiwayatDAO;
import com.example.appsaldi.model.Riwayat;
import com.example.appsaldi.model.UsersModel;
import com.example.appsaldi.utils.AlertUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LaporanController {

    @Setter private UsersModel currentUser;
    @Setter private TampilanUtamaController utamaController;

    @FXML private HBox menu;
    @FXML private Label lblNamaData, lblSampai;
    @FXML private Button btnPasien;
    @FXML private TableView tblData;
    @FXML private DatePicker dpTglAwal, dpTglAkhir;

    public enum JenisData {
        PASIEN, PENYAKIT, GEJALA, RIWAYAT
    }

    private JenisData jenisDataAktif;

    @FXML public void initialize() {
        tampilkanDataPasien();
        btnPasien.setOnAction(e -> tampilkanDataPasien());
        Platform.runLater(this::sembunyikanTombolJikaResepsionis);
    }

    public void sembunyikanTombolJikaResepsionis() {
        if (currentUser != null && "resepsionis".equalsIgnoreCase(currentUser.getStatus())) {
            menu.setVisible(false);
            menu.setManaged(false);
            lblNamaData.setVisible(false);
            lblNamaData.setManaged(false);
        }
    }

    @FXML private void handelCetakData() {
        cetakLaporan();
    }

    @FXML private void showDataPasien(){
        datePickerShow();
        tampilkanDataPasien();
        tblData.refresh();
    }

    @FXML public void showRiwayatDiagnosa(){
        datePickerShow();
        tampilkanRiwayatDiagnosa();
        tblData.refresh();
    }

    @FXML public void showDataPenyakit() throws SQLException {
        datePickerHidden();
        tampilkanDataPenyakit();
        tblData.refresh();
    }

    @FXML private void showDataGejala() {
        datePickerHidden();
        tampilkanDataGejala();
        tblData.refresh();
    }

    // Menampilkan data pasien
    public void tampilkanDataPasien() {
        try {
            jenisDataAktif = JenisData.PASIEN;
            tblData.getColumns().clear();

            tblData.getColumns().addAll(
                    createResizableColumn("ID", "idPasien", 5),
                    createResizableColumn("Nama Pasien", "namaPasien", 20),
                    createResizableColumn("Tanggal Lahir", "tglLahirPasien", 10),
                    createResizableColumn("NIK", "nik", 13),
                    createResizableColumn("Pekerjaan", "pekerjaan", 15),
                    createResizableColumn("No. Telepon", "noTlpPasien", 12),
                    createResizableColumn("Alamat", "alamatPasien", 25)
            );

            setTableData(FXCollections.observableArrayList(new PasienDAO().getAllPasien()), "Data Pasien");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Menampilkan data penyakit
    public void tampilkanDataPenyakit() throws SQLException {
        jenisDataAktif = JenisData.PENYAKIT;
        tblData.getColumns().clear();

        tblData.getColumns().addAll(
                createResizableColumn("ID", "idPenyakit", 5),
                createWrappingColumn("Penyakit", "namaPenyakit", 25),
                createWrappingColumn("Solusi", "solusi", 70)

        );

        setTableData(FXCollections.observableArrayList(new PenyakitDAO().getAllPenyakit()), "Data Penyakit");
    }

    // Menampilkan data gejala
    public void tampilkanDataGejala() {
        jenisDataAktif = JenisData.GEJALA;
        tblData.getColumns().clear();

        tblData.getColumns().addAll(
                createResizableColumn("ID", "id_gejala", 5),
                createWrappingColumn("Gejala", "nama_gejala", 90)
        );

        setTableData(FXCollections.observableArrayList(new GejalaDAO().getAllGejala()), "Data Gejala");
    }

    // Menampilkan data riwayat diagnosa
    public void tampilkanRiwayatDiagnosa() {
        jenisDataAktif = JenisData.RIWAYAT;
        tblData.getColumns().clear();

        TableColumn<Riwayat, String> colGejala = new TableColumn<>("Gejala");
        colGejala.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getGejalaFormated()));
        colGejala.setCellFactory(tc -> createWrappingCell());
        colGejala.prefWidthProperty().bind(tblData.widthProperty().multiply(0.30));

        TableColumn<Riwayat, String> colPenyakit = new TableColumn<>("Penyakit");
        colPenyakit.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPenyakitFormated()));
        colPenyakit.setCellFactory(tc -> createWrappingCell());
        colPenyakit.prefWidthProperty().bind(tblData.widthProperty().multiply(0.15));

        tblData.getColumns().addAll(
                createResizableColumn("ID", "idRiwayat", 5),
                createResizableColumn("Nama Pasien", "namaPasien", 20),
                colPenyakit,
                colGejala,
                createResizableColumn("Tanggal Diagnosa", "tglDiagnosa", 15),
                createResizableColumn("Dokter", "namaDokter", 20)
        );

        setTableData(FXCollections.observableArrayList(new RiwayatDAO().getRiwayat()), "Data Riwayat Diagnosa");
    }

    private <T> TableCell<T, String> createWrappingCell() {
        return new TableCell<T, String>() {
            private final Text text;

            {
                text = new Text();
                text.wrappingWidthProperty().bind(widthProperty().subtract(10));
                text.getStyleClass().add("wrapped-text");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isEmpty()) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    setGraphic(text);
                }
            }
        };
    }

    private <T, R> TableColumn<T, R> createResizableColumn(String title, String property, double percentage) {
        TableColumn<T, R> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.prefWidthProperty().bind(tblData.widthProperty().multiply(percentage / 100.0));
        return col;
    }


    private <T> TableColumn<T, String> createWrappingColumn(String title, String property, double percentage) {
        TableColumn<T, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.prefWidthProperty().bind(tblData.widthProperty().multiply(percentage / 100.0));
        col.setCellFactory(tc -> createWrappingCell());
        return col;
    }

    private <T> void setTableData(ObservableList<T> data, String labelText) {
        tblData.setItems(data);
        lblNamaData.setText(labelText);
    }

    public void cetakLaporan() {
        // Buat Stage loading
        Stage loadingStage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getChildren().add(new Label("Memuat laporan, mohon tunggu..."));
        Scene scene = new Scene(root, 250, 100);
        loadingStage.setScene(scene);
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setResizable(false);
        loadingStage.setTitle("Loading");

        // Buat task untuk cetak laporan
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String reportPath = "";
                Map<String, Object> parameters = new HashMap<>();
                Connection conn = ConnectionDB.getConnection();

                switch (jenisDataAktif) {
                    case PASIEN:
                        reportPath = "/com/example/appsaldi/report/laporanpasien.jasper";
                        if (dpTglAwal.getValue() != null && dpTglAkhir.getValue() != null) {
                            parameters.put("TanggalMulai", java.sql.Date.valueOf(dpTglAwal.getValue()));
                            parameters.put("TanggalSelesai", java.sql.Date.valueOf(dpTglAkhir.getValue()));
                        } else {
                            Platform.runLater(() -> {
                                loadingStage.close();
                                AlertUtils.showAlert(Alert.AlertType.WARNING, "Gagal", "Tanggal awal dan akhir harus diisi.");
                            });
                            return null;
                        }
                        break;

                    case PENYAKIT:
                        reportPath = "/com/example/appsaldi/report/laporanpenyakit.jasper";
                        break;

                    case GEJALA:
                        reportPath = "/com/example/appsaldi/report/laporangejala.jasper";
                        break;

                    case RIWAYAT:
                        reportPath = "/com/example/appsaldi/report/laporanriwayat.jasper";
                        if (dpTglAwal.getValue() != null && dpTglAkhir.getValue() != null) {
                            parameters.put("TanggalMulai", java.sql.Date.valueOf(dpTglAwal.getValue()));
                            parameters.put("TanggalSelesai", java.sql.Date.valueOf(dpTglAkhir.getValue()));
                        } else {
                            Platform.runLater(() -> {
                                loadingStage.close();
                                AlertUtils.showAlert(Alert.AlertType.WARNING, "Gagal", "Tanggal awal dan akhir harus diisi.");
                            });
                            return null;
                        }
                        break;
                }

                InputStream reportStream = getClass().getResourceAsStream(reportPath);
                if (reportStream == null) {
                    throw new RuntimeException("Laporan tidak ditemukan: " + reportPath);
                }
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, conn);

                // Buka JasperViewer di thread UI
                Platform.runLater(() -> {
                    loadingStage.close();
                    JasperViewer viewer = new JasperViewer(jasperPrint, false);
                    viewer.setVisible(true);
                });

                return null;
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    loadingStage.close();
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "Kesalahan", "Gagal mencetak laporan: " + getException().getMessage());
                    getException().printStackTrace();
                });
            }
        };

        // Tampilkan loading dan mulai task
        loadingStage.show();
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void datePickerHidden() {
        dpTglAkhir.setVisible(false);
        dpTglAkhir.setManaged(false);
        dpTglAwal.setVisible(false);
        dpTglAwal.setManaged(false);
        lblSampai.setVisible(false);
    }

    private void datePickerShow() {
        dpTglAkhir.setVisible(true);
        dpTglAkhir.setManaged(true);
        dpTglAwal.setVisible(true);
        dpTglAwal.setManaged(true);
        lblSampai.setVisible(true);
    }

}