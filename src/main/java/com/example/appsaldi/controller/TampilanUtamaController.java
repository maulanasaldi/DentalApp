package com.example.appsaldi.controller;
import com.example.appsaldi.controller.pengaturan.ProfileAkunController;
import com.example.appsaldi.dao.PasienDAO;
import com.example.appsaldi.model.Pasien;
import com.example.appsaldi.model.UsersModel;
import com.example.appsaldi.utils.Session;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TampilanUtamaController {

    @Setter private UsersModel currentUser;
    @Getter private Object currentController;

    @FXML private Circle imageProfile;
    @FXML public Label lblWelcome, lblNama, lblStatus;
    @FXML public Button btnDiagnosa, btnLaporan, btnData, btnKeluar;
    @FXML private StackPane contentPane;
    @FXML private Button btnNotif;
    @FXML private Label badgeNotif;

    private Timer timer;
    private Popup notifPopup = new Popup();
    private VBox notifContainer = new VBox();

    @FXML
    private void initialize() {
        notifContainer.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-spacing: 10;");
        notifPopup.getContent().add(notifContainer);
        if ("resepsionis".equalsIgnoreCase(Session.getCurrentRole())) {
            btnNotif.setVisible(false);
            btnNotif.setManaged(false);
            badgeNotif.setVisible(false);
            badgeNotif.setManaged(false);
        }
    }

    public Object setContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node node = loader.load();
            Object controller = loader.getController();
            this.currentController = controller;
            if (controller instanceof DashboardController dashboardCtrl) {
                dashboardCtrl.setUtamaController(this);
                dashboardCtrl.setCurrentUser(currentUser);
            } else if (controller instanceof DataController dataController) {
                dataController.setUtamaController(this);
                if (fxmlPath.contains("data.fxml") && lblWelcome.getText().equals("Riwayat")) {
                    dataController.setAsRiwayatDiagnosa();
                }
                dataController.setCurrentUser(currentUser);
            } else if (controller instanceof LaporanController laporanController) {
                laporanController.setUtamaController(this);
                laporanController.setCurrentUser(currentUser);
            } else if (controller instanceof PengaturanController pengaturanController) {
                pengaturanController.setUtamaController(this);
            } else if (controller instanceof ProfileAkunController profileAkunController) {
                profileAkunController.setUtamaController(this);
            }
            StackPane.setMargin(node, Insets.EMPTY);
            StackPane.setAlignment(node, javafx.geometry.Pos.CENTER);
            if (node instanceof Region region) {
                region.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
                region.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            }
            node.setOpacity(0);
            contentPane.getChildren().setAll(node);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), node);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void resetButtonStyles() {
        btnDiagnosa.setStyle("");
        btnLaporan.setStyle("");
        btnData.setStyle("");
    }

    @FXML
    private void handleBellClick() {
        if (!notifPopup.isShowing()) {
            notifPopup.show(btnNotif, btnNotif.localToScreen(0, btnNotif.getHeight()).getX(), btnNotif.localToScreen(0, btnNotif.getHeight()).getY());
        } else {
            notifPopup.hide();
        }
    }

    @FXML
    public void showDashboard() {
        setContent("/com/example/appsaldi/view/menunavbar/dashboard.fxml");
        lblWelcome.setText("Beranda");
        resetButtonStyles();
    }

    @FXML
    public void showDiagnosa() {
        Object controller = setContent("/com/example/appsaldi/view/menunavbar/diagnosa.fxml");
        lblWelcome.setText("Diagnosa");
        resetButtonStyles();
        btnDiagnosa.requestFocus();
        btnDiagnosa.setStyle("-fx-background-color: #D4EAF7; -fx-font-weight: bold;");
        if (controller instanceof DiagnosisController diagnosisController) {
            diagnosisController.setUsersModel(currentUser);
            diagnosisController.setUtamaController(this);
        }
    }

    @FXML
    public void showLaporan() {
        if (currentUser != null && "resepsionis".equalsIgnoreCase(currentUser.getStatus())) {
            lblWelcome.setText("Laporan Data Pasien");
        } else {
            lblWelcome.setText("Laporan");
        }
        resetButtonStyles();
        btnLaporan.requestFocus();
        btnLaporan.setStyle("-fx-background-color: #D4EAF7; -fx-font-weight: bold;");
        setContent("/com/example/appsaldi/view/menunavbar/laporan.fxml");
    }

    @FXML
    public void showData() {
        setContent("/com/example/appsaldi/view/menunavbar/data.fxml");
        if (currentUser != null && "resepsionis".equalsIgnoreCase(currentUser.getStatus())) {
            lblWelcome.setText("Data Pasien");
        } else {
            lblWelcome.setText("Data");
        }
        resetButtonStyles();
        btnData.requestFocus();
        btnData.setStyle("-fx-background-color: #D4EAF7; -fx-font-weight: bold;");
    }

    @FXML
    public void handleClickProfile() {
        setContent("/com/example/appsaldi/view/menunavbar/pengaturan.fxml");
        lblWelcome.setText("Pengaturan");
        resetButtonStyles();
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah anda yakin ingin logout?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Session.clear();
            showLogin();
        }
    }

    public void showLogin() {
        if (timer != null) {
            timer.cancel();
        }
        try {
            Stage stage = (Stage) btnKeluar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/appsaldi/view/login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(UsersModel usersModel) {
        if (usersModel != null) {
            this.currentUser = usersModel;
            lblWelcome.setText("Selamat Datang " + usersModel.getUsername());
            if (usersModel.getFotoPath() != null && !usersModel.getFotoPath().isEmpty()) {
                File file = new File(usersModel.getFotoPath());
                if (!file.exists()) {
                    String userHome = System.getProperty("user.home");
                    file = new File(userHome, usersModel.getFotoPath());
                }
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageProfile.setFill(new ImagePattern(image));
                } else {
                    setDefaultImage();
                }
            } else {
                setDefaultImage();
            }
            lblNama.setText(usersModel.getNama());
            lblStatus.setText(usersModel.getStatus());
            if ("Resepsionis".equalsIgnoreCase(usersModel.getStatus())) {
                btnDiagnosa.setVisible(false);
                btnDiagnosa.setManaged(false);
            } else {
                btnDiagnosa.setVisible(true);
                btnDiagnosa.setManaged(true);
                startPasienPolling();
            }
            Platform.runLater(() -> showDashboard());
        } else {
            setDefaultImage();
        }
    }

    private void setDefaultImage() {
        Image defaultImage = new Image(getClass().getResource("/com/example/appsaldi/image/default.jpg").toString());
        imageProfile.setFill(new ImagePattern(defaultImage));
    }

    public void cekPasienBaru() {
        PasienDAO pasienDAO = new PasienDAO();
        List<Pasien> semuaPasienBelum = pasienDAO.getPasienBelumDidiagnosa();
        notifContainer.getChildren().clear(); // bersihkan isi popup dulu
        if (!semuaPasienBelum.isEmpty()) {
            for (Pasien pasien : semuaPasienBelum) {
                notifContainer.getChildren().add(createNotifItem(pasien));
            }
            badgeNotif.setVisible(true);
            badgeNotif.setManaged(true);
        } else {
            Label kosongLabel = new Label("Tidak ada pasien baru.");
            kosongLabel.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            notifContainer.getChildren().add(kosongLabel);
            badgeNotif.setVisible(false);
            badgeNotif.setManaged(false);
        }
    }

    private VBox createNotifItem(Pasien pasien) {
        VBox notifCard = new VBox();
        notifCard.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5;");
        notifCard.setOnMouseEntered(e -> notifCard.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 10; -fx-border-color: #aaa; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;"));
        notifCard.setOnMouseExited(e -> notifCard.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand;"));
        notifCard.setSpacing(5);
        Label lblNama = new Label("Nama: " + pasien.getNamaPasien());
        Label lblTglDaftar = new Label("Daftar: " + pasien.getTglPendaftaran().toString());
        notifCard.getChildren().addAll(lblNama, lblTglDaftar);
        notifCard.setOnMouseClicked(e -> {
            notifPopup.hide();
            PasienDAO dao = new PasienDAO();
            dao.updateStatusNotifPendaftaran(pasien.getIdPendaftaran()); // update status_notif menjadi TRUE
            pindahKeDiagnosaDenganPasien(pasien);
            cekPasienBaru(); // agar refresh popup setelah klik
        });
        return notifCard;
    }

    private void pindahKeDiagnosaDenganPasien(Pasien pasien) {
        showDiagnosa();
        if (currentController instanceof DiagnosisController diagnosisController) {
            diagnosisController.setDataPasein(pasien);
        }
    }

    private void startPasienPolling() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> cekPasienBaru());
            }
        }, 0, 5000);
    }

    public void bukaProfileAkun() {
        Object controller = setContent("/com/example/appsaldi/view/pengaturan/profiledanakun.fxml");
        if (controller instanceof ProfileAkunController profileAkunController) {
            profileAkunController.setCurrentUser(currentUser);
            Platform.runLater(profileAkunController::initFormData);
        }
        resetButtonStyles();
    }

    public void bukaTantangAplikasi() {
        setContent("/com/example/appsaldi/view/pengaturan/tentangaplikasi.fxml");
        lblWelcome.setText("Tentang Aplikasi");
        resetButtonStyles();
    }
}
