package com.example.appsaldi.controller.poopupcontroller;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.controller.DiagnosisController;
import com.example.appsaldi.controller.TampilanUtamaController;
import com.example.appsaldi.dao.PasienDAO;
import com.example.appsaldi.model.DiagnosaModel;
import com.example.appsaldi.model.UsersModel;
import com.example.appsaldi.utils.AlertUtils;
import com.example.appsaldi.utils.Session;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupDiagnosisResultController {

    @Setter
    private UsersModel usersModel;
    @Setter
    private TampilanUtamaController utamaController;
    @Setter
    private DiagnosisController diagnosisController;



    @FXML private VBox rootVBox;
    @FXML private Button btnClose, btnCetak;
    @FXML private Text lblIdPasien, patientName, diseaseName, diseaseSolution;

    private int patientId;
    private List<Integer> diseaseId;
    private List<Integer> patientSymptoms;
    private DiagnosaModel diagnosaModel;
    private boolean isDiagnosisSaved = false;


    public void setDiagnosisData(int patientId, List<Integer> diseaseId, List<Integer> patientSymptoms, DiagnosaModel diagnosaModel) {
        this.patientId = patientId;
        this.diseaseId = diseaseId;
        this.patientSymptoms = patientSymptoms;
        this.diagnosaModel = diagnosaModel;
    }

    @FXML
    private void handleSave() {
        try {
            int idDokter = Session.getCurrentUserId();
            int idRiwayatDiagnosa = diagnosaModel.simpanRiwayatDiagnosa(patientId, idDokter);

            diagnosaModel.simpanRiwayatPenyakit(idRiwayatDiagnosa, diseaseId);
            diagnosaModel.simpanRiwayatGejala(List.of(idRiwayatDiagnosa), patientSymptoms);

            PasienDAO pasienDAO = new PasienDAO();
            pasienDAO.updateStatusNotifById(patientId);
            Platform.runLater(() -> {
                if (utamaController != null) {
                    utamaController.cekPasienBaru(); // REFRESH NOTIFIKASI UTAMA
                }
            });

            isDiagnosisSaved = true;

            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data diagnosa berhasil disimpan.");
            btnCetak.setVisible(false);
            btnCetak.setManaged(false);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan data diagnosis.");
        }
    }


    @FXML
    private void handelCetakHasilDiagnosa() {
        if (!isDiagnosisSaved) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Data Belum Disimpan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan simpan data diagnosis terlebih dahulu sebelum mencetak.");
            alert.showAndWait();
            return;
        }

        // Buat dan tampilkan loading stage
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
        loadingStage.show();

        // Jalankan proses cetak di background thread
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    String reportPath = "/com/example/appsaldi/report/HasilDiagnosa.jasper";
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("id_pasien", patientId);

                    InputStream reportStream = getClass().getResourceAsStream(reportPath);
                    if (reportStream == null) {
                        throw new RuntimeException("Laporan tidak ditemukan: " + reportPath);
                    }

                    Connection conn = ConnectionDB.getConnection();
                    JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, conn);

                    Platform.runLater(() -> {
                        loadingStage.close();
                        JasperViewer viewer = new JasperViewer(jasperPrint, false);
                        viewer.setVisible(true);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        loadingStage.close();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Gagal Cetak");
                        alert.setHeaderText(null);
                        alert.setContentText("Terjadi kesalahan saat mencetak laporan.");
                        alert.showAndWait();
                    });
                }
                return null;
            }
        };

        new Thread(task).start();
    }


    @FXML
    private void handleCloseForm() {
        if (diagnosisController != null) {
            diagnosisController.resetDataDiagnosa();
        }
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        rootVBox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            Rectangle clip = new Rectangle(newBounds.getWidth(), newBounds.getHeight());
            clip.setArcWidth(40);
            clip.setArcHeight(40);
            rootVBox.setClip(clip);
        });
        diseaseSolution.setWrappingWidth(559);
        btnCetak.setVisible(false);
        btnCetak.setManaged(false);
    }

    public void setData(int idPasien, String namaPasienStr, List<String> namaPenyakitStr, List<String> solusiStr) {
        lblIdPasien.setText(String.valueOf(idPasien));
        patientName.setText(namaPasienStr);

        StringBuilder penyakitBuilder = new StringBuilder();
        StringBuilder solusiBuilder = new StringBuilder();

        for (int i = 0; i < namaPenyakitStr.size(); i++) {
            penyakitBuilder.append((i + 1)).append(". ").append(namaPenyakitStr.get(i)).append("\n");
            solusiBuilder.append((i + 1)).append(". ").append(solusiStr.get(i)).append("\n");
        }

        diseaseName.setText(penyakitBuilder.toString());
        diseaseSolution.setText(solusiBuilder.toString());
    }

}
