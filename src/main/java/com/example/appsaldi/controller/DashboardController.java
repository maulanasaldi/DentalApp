package com.example.appsaldi.controller;
import com.example.appsaldi.controller.formcontroller.FormRegistrasiController;
import com.example.appsaldi.dao.PasienDAO;
import com.example.appsaldi.dao.RiwayatDAO;
import com.example.appsaldi.model.Pasien;
import com.example.appsaldi.model.UsersModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import java.util.Map;

public class DashboardController {

    @Setter
    private UsersModel currentUser;
    @Setter
    private TampilanUtamaController utamaController;

    @FXML private Button btnOpenFormRegistrasi, btnStartDiagnosis, btnViewHistory;
    @FXML private PieChart pieChart;
    @FXML private TableView pasien;
    @FXML private Label lblJumlahPasien, lblPenyakitTerbanyak;
    @FXML private HBox mainBox;

    @FXML
    public void initialize() {
        tampilkanPieChartPasienByPekerjaan();
        tampilkanDataPasien();
        tampilkanStatistikDiagnosa();
        Platform.runLater(this::sembunyikanTombolJikaResepsionis);
    }

    public void sembunyikanTombolJikaResepsionis() {
        if (currentUser != null && "resepsionis".equalsIgnoreCase(currentUser.getStatus())) {
            btnStartDiagnosis.setVisible(false);
            btnStartDiagnosis.setManaged(false);
            btnViewHistory.setVisible(false);
            btnViewHistory.setManaged(false);
        }
    }

    private void tampilkanStatistikDiagnosa() {
        RiwayatDAO dao = new RiwayatDAO();
        int jumlahPasienBulanIni = dao.getJumlahDiagnosaBulanIni();
        String penyakitTerbanyak = dao.getPenyakitTerbanyak();
        lblJumlahPasien.setText((jumlahPasienBulanIni + " Pasien"));
        lblPenyakitTerbanyak.setText(penyakitTerbanyak);
    }

    @FXML
    public void showStartDiagnosis() {
        if (utamaController != null) {
            Object controller = utamaController.setContent("/com/example/appsaldi/view/menunavbar/diagnosa.fxml");
            utamaController.lblWelcome.setText("Diagnosa");
            if (controller instanceof DiagnosisController diagnosisController) {
                diagnosisController.setUsersModel(currentUser);
            }
        }
    }

    @FXML
    private void showFormRegistrasi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appsaldi/view/form/formregistrasi.fxml"));
            Parent root = loader.load();
            Stage mainStage = (Stage) btnOpenFormRegistrasi.getScene().getWindow();
            FormRegistrasiController controller = loader.getController();
            controller.setFromCariPopup(true);
            controller.setupButtonCariVisibility();
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
            popupStage.setOnHidden(e -> {
                mainStage.getScene().getRoot().setEffect(null);
                tampilkanDataPasien();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showHistory() {
        if (utamaController != null) {
            utamaController.setContent("/com/example/appsaldi/view/menunavbar/data.fxml");
            Platform.runLater(() -> {
                Object object = utamaController.getCurrentController();
                if (object instanceof DataController dataController) {
                    dataController.tampilkanRiwayatDiagnosa();
                }
            });
            utamaController.lblWelcome.setText("Data");
            utamaController.btnData.requestFocus();
            utamaController.btnData.setStyle("-fx-background-color: #D4EAF7; -fx-font-weight: bold;");
        } else {
            System.out.println("utamaController belum diset!");
        }
    }

    private void tampilkanPieChartPasienByPekerjaan() {
        PasienDAO pasienDAO = new PasienDAO();
        Map<String, Integer> dataPekerjaan = pasienDAO.getJumlahPasienPerPekerjaan();
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : dataPekerjaan.entrySet()) {
            String pekerjaan = entry.getKey();
            int jumlah = entry.getValue();
            String label = String.format("%s\n%d pasien", pekerjaan, jumlah);
            chartData.add(new PieChart.Data(label, jumlah));
        }
        pieChart.setData(chartData);
        pieChart.setTitle("Statistik Pasien Berdasarkan Pekerjaan");
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        Platform.runLater(() -> {
            for (Node node : pieChart.lookupAll("Text.chart-pie-label")) {
                node.setStyle("-fx-font-family: 'Poppins'; -fx-font-size: 12px; -fx-text-alignment: center;");
            }
        });
    }

    public void tampilkanDataPasien() {
        pasien.getColumns().clear();
        pasien.getColumns().addAll(
                createResizableColumn("ID", "idPasien", 0.05),
                createResizableColumn("Nama Pasien", "namaPasien", 0.20),
                createResizableColumn("Tanggal Daftar", "tglPendaftaran", 0.10),
                createResizableColumn("Status Diagnosa", "statusDiagnosa", 0.13),
                createResizableColumn("Pekerjaan", "pekerjaan", 0.15),
                createResizableColumn("No. Telepon", "noTlpPasien", 0.12),
                createResizableColumn("Alamat", "alamatPasien", 0.25)
        );
        PasienDAO pasienDAO = new PasienDAO();
        ObservableList<Pasien> data = FXCollections.observableArrayList();
        try {
            data.addAll(pasienDAO.getPasienTerbaru(8)); // Hanya tampilkan 5 pasien terbaru
        } catch (Exception e) {
            e.printStackTrace();
        }
        pasien.setItems(data);
    }

    private <T, R> TableColumn<T, R> createResizableColumn(String title, String property, double percentage) {
        TableColumn<T, R> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.prefWidthProperty().bind(pasien.widthProperty().multiply(percentage));
        return col;
    }
}
