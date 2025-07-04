package com.example.appsaldi.controller;
import com.example.appsaldi.controller.formcontroller.*;
import com.example.appsaldi.dao.*;
import com.example.appsaldi.model.*;
import com.example.appsaldi.utils.AlertUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataController {

    @Setter private UsersModel currentUser;
    @Setter private TampilanUtamaController utamaController;
    @Setter private String modeAwal = "Pasien";

    @FXML private Label lblNamaData;
    @FXML private Button btnTambahData, btnEditData;
    @FXML private HBox menu;
    @FXML private TextField txtCariData;
    @FXML private TableView tblData;

    public void setAsRiwayatDiagnosa(){
        tampilkanRiwayatDiagnosa();
    }
    public enum JenisData {
        PASIEN, PENYAKIT, GEJALA, RIWAYAT, ATURAN
    }
    private JenisData jenisDataAktif;

    @FXML public void initialize() {
        Platform.runLater(() -> {
            if (currentUser != null) {
                String status = currentUser.getStatus().toLowerCase();
                switch (status) {
                    case "resepsionis" -> {
                        menu.setVisible(false);
                        menu.setManaged(false);
                        lblNamaData.setVisible(false);
                        tampilkanDataPasien();
                    }

                    case "dokter" -> {
                        try {
                            tampilkanDataPenyakit();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else {
                System.out.println("Status user null");
            }
            txtCariData.textProperty().addListener((obs, oldVal, newVal) -> cariData(newVal));
        });
    }

    @FXML public void showRiwayatDiagnosa(){
        hiddenButton();
        tampilkanRiwayatDiagnosa();
        tblData.refresh();
    }

    @FXML public void showDataPenyakit() throws SQLException {
        showButton();
        tampilkanDataPenyakit();
        tblData.refresh();
    }

    @FXML private void showDataGejala() throws SQLException {
        showButton();
        tampilkanDataGejala();
        tblData.refresh();
    }

    @FXML private void showDataAturan() throws SQLException {
        showButton();
        tampilkanDataAturan();
        tblData.refresh();
    }

    private void showButton() {
        btnTambahData.setVisible(true);
        btnEditData.setVisible(true);
    }

    private void hiddenButton() {
        btnTambahData.setVisible(false);
        btnTambahData.setManaged(false);
    }

    public void tampilkanDataPasien() {
        try {
            jenisDataAktif = JenisData.PASIEN;
            tblData.getColumns().clear();
            tblData.getColumns().addAll(
                    createResizableColumn("ID", "idPasien", 5),
                    createResizableColumn("Nama Pasien", "namaPasien", 20),
                    createResizableColumn("Tanggal Lahir", "tglLahirPasien", 10),
                    createResizableColumn("NIK", "nik", 13),
                    createResizableColumn("Pekerjaan", "pekerjaan", 10),
                    createResizableColumn("No. Telepon", "noTlpPasien", 12),
                    createResizableColumn("Alamat", "alamatPasien", 30)
            );
            setTableData(FXCollections.observableArrayList(new PasienDAO().getAllPasien()), "Data Pasien");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public void tampilkanDataGejala() throws SQLException {
        jenisDataAktif = JenisData.GEJALA;
        tblData.getColumns().clear();
        tblData.getColumns().addAll(
                createResizableColumn("ID", "id_gejala", 5),
                createWrappingColumn("Gejala", "nama_gejala", 95)
        );
        setTableData(FXCollections.observableArrayList(new GejalaDAO().getAllGejala()), "Data Gejala");
    }

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
            createResizableColumn("Dokter", "namaDokter", 15)
        );
        setTableData(FXCollections.observableArrayList(new RiwayatDAO().getRiwayat()), "Data Riwayat Diagnosa");
    }

    public void tampilkanDataAturan() {
        jenisDataAktif = JenisData.ATURAN;
        tblData.getColumns().clear();
        TableColumn<Aturan, String> colIdAturan = new TableColumn<>("ID Aturan");
        colIdAturan.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getIdAturan()))
        );
        colIdAturan.setCellFactory(tc -> createWrappingCell());
        TableColumn<Aturan, String> colIdPenyakit = new TableColumn<>("ID Penyakit");
        colIdPenyakit.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getIdPenyakit()))
        );
        colIdPenyakit.setCellFactory(tc -> createWrappingCell());
        TableColumn<Aturan, String> colIDGejala = new TableColumn<>("ID Gejala");
        colIDGejala.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getGejalaFormated()))
        );
        colIdPenyakit.setCellFactory(tc -> createWrappingCell());
        tblData.getColumns().addAll(
                colIdAturan, colIdPenyakit, colIDGejala
        );
        try {
            setTableData(FXCollections.observableArrayList(new KnowladgeDAO().getAllAturan()), "Data Aturan");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void cariData(String keyword) {
        try {
            if (jenisDataAktif == JenisData.PASIEN) {
                ObservableList<Pasien> filtered = FXCollections.observableArrayList(new PasienDAO().searchPasien(keyword));
                tblData.setItems(filtered);
            } else if (jenisDataAktif == JenisData.RIWAYAT) {
                ObservableList<Riwayat> filteredRiwayat = FXCollections.observableArrayList(new RiwayatDAO().searchRiwayat(keyword));
                tblData.setItems(filteredRiwayat);
            } else if (jenisDataAktif == JenisData.PENYAKIT) {
                ObservableList<Penyakit> filteredPenyakit = FXCollections.observableArrayList(new PenyakitDAO().searchPenyakit(keyword));
                tblData.setItems(filteredPenyakit);
            } else if (jenisDataAktif == JenisData.GEJALA) {
                ObservableList<Gejala> filterGejala = FXCollections.observableArrayList(new GejalaDAO().searchGejala(keyword));
                tblData.setItems(filterGejala);
            } else if (jenisDataAktif == JenisData.ATURAN) {
                ObservableList<Aturan> filterAturan = FXCollections.observableArrayList(new KnowladgeDAO().searchAturan(keyword));
                tblData.setItems(filterAturan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showFromTambahData() {
        switch (jenisDataAktif) {
            case PASIEN:
                try {
                    showPopup("/com/example/appsaldi/view/form/formregistrasi.fxml", fxmlLoader -> {
                        FormRegistrasiController controller = fxmlLoader.getController();
                        controller.setDataController(this);
                    });
                    tampilkanDataPasien();
                    tblData.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PENYAKIT:
                try {
                    showPopup("/com/example/appsaldi/view/form/formdatapenyakit.fxml", null);
                    tampilkanDataPenyakit();
                    tblData.refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case GEJALA:
                showPopup(
                        "/com/example/appsaldi/view/form/formdatagejala.fxml",
                        loader -> {
                            FormGejalaController controller = loader.getController();
                            controller.resetForm();
                        }
                );
                try {
                    tampilkanDataGejala();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ATURAN:
                showPopup("/com/example/appsaldi/view/form/formdataaturan.fxml", null);
                tampilkanDataAturan();
                tblData.refresh();
                break;
        }
    }

    @FXML
    private void showFromEditData() {
        switch (jenisDataAktif) {
            case PASIEN:
                editData(
                        "/com/example/appsaldi/view/form/formeditpasien.fxml",
                        FormEditPasienController.class,
                        controller -> {
                            controller.setDataPasien((Pasien) tblData.getSelectionModel().getSelectedItem());
                            controller.setDataController(this);
                        },
                        this::tampilkanDataPasien
                );
                break;
            case PENYAKIT:
                editData(
                        "/com/example/appsaldi/view/form/formdatapenyakit.fxml",
                        FormPenyakitController.class,
                        controller -> controller.setDataPenyakit((Penyakit) tblData.getSelectionModel().getSelectedItem()),
                        () -> {
                            try {
                                tampilkanDataPenyakit();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                );
                break;
            case GEJALA:
                editData(
                        "/com/example/appsaldi/view/form/formdatagejala.fxml",
                        FormGejalaController.class,
                        controller -> controller.setDataGejala((Gejala) tblData.getSelectionModel().getSelectedItem()),
                        () -> {
                            try {
                                tampilkanDataGejala();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                );
                break;
            case RIWAYAT:
                editData(
                        "/com/example/appsaldi/view/form/formriwayat.fxml",
                        FormRiwayatController.class,
                        controller -> Platform.runLater(() -> controller.setRiwayat((Riwayat) tblData.getSelectionModel().getSelectedItem())),
                        this::tampilkanRiwayatDiagnosa
                );
                break;

            case ATURAN:
                editData(
                        "/com/example/appsaldi/view/form/formdataaturan.fxml",
                        FormAturanController.class,
                        controller -> Platform.runLater(() -> controller.setDataAturan((Aturan) tblData.getSelectionModel().getSelectedItem())),
                        this::tampilkanDataAturan
                );
                break;
        }
    }


    @FXML
    private void handelDeleteData() {
        switch (jenisDataAktif) {
            case PASIEN -> handleDelete(
                    "pasien",
                    tblData.getSelectionModel().getSelectedItem(),
                    obj -> ((Pasien) obj).getIdPasien(),
                    id -> {
                        try {
                            new PasienDAO().deletePasien(String.valueOf(id));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            case PENYAKIT -> handleDelete(
                    "penyakit",
                    tblData.getSelectionModel().getSelectedItem(),
                    obj -> ((Penyakit) obj).getIdPenyakit(),
                    id -> {
                        try {
                            new PenyakitDAO().deletePenyakit(String.valueOf(id));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            case GEJALA -> handleDelete(
                    "gejala",
                    tblData.getSelectionModel().getSelectedItem(),
                    obj -> ((Gejala) obj).getId_gejala(),
                    id -> {
                        try {
                            new GejalaDAO().deleteGejala(String.valueOf(id));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            case RIWAYAT -> handleDelete(
                    "riwayat",
                    tblData.getSelectionModel().getSelectedItem(),
                    obj -> ((Riwayat) obj).getIdRiwayat(),
                    id -> new RiwayatDAO().deleteRiwayat((Integer) id)
            );
            case ATURAN -> handleDelete(
                    "basis_pengetahuan",
                    tblData.getSelectionModel().getSelectedItem(),
                    obj -> ((Aturan) obj).getIdAturan(),
                    id -> {
                        try {
                            new KnowladgeDAO().deleteAturan(String.valueOf(id));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }
    }

    private <T> void handleDelete(String jenis, Object selectedItem, Function<Object, Object> getId, Consumer<Object> deleteFunction) {
        if (selectedItem == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Peringatan", "Silahkan pilih data terlebih dahulu");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Apakah anda yakin ingin menghapus data " + jenis + " ini?");
        alert.setContentText("Dengan ID: " + getId.apply(selectedItem));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                deleteFunction.accept(getId.apply(selectedItem));
                tblData.getItems().remove(selectedItem);
                AlertUtils.showNotificationSuccess("Data berhasil dihapus");
            } catch (RuntimeException e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLException sqlEx) {
                    if (sqlEx.getMessage().contains("a foreign key constraint fails")) {
                        AlertUtils.showAlert(Alert.AlertType.WARNING, "Peringatan",
                                "Data ini masih memiliki hubungan dengan data lain. Silahkan pilih data yang terhubung dengan data pada tabel lain");
                    } else {
                        AlertUtils.showNotificationWarning("Gagal menghapus data " + jenis);
                    }
                } else {
                    AlertUtils.showNotificationError("Gagal menghapus data " + jenis);
                }
            }
        }
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

    private void showPopup(String fxmlPath, Consumer<FXMLLoader> controllerConsumer) {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
           Parent root = loader.load();
           if (controllerConsumer != null) {
               controllerConsumer.accept(loader);
           }
           Stage mainStage = (Stage) btnTambahData.getScene().getWindow();
           Stage popuStage = new Stage();
           popuStage.initModality(Modality.WINDOW_MODAL);
           popuStage.initOwner(mainStage);
           popuStage.initStyle(StageStyle.TRANSPARENT);
           mainStage.getScene().getRoot().setEffect(new GaussianBlur(10));
           popuStage.setOnHidden(e -> mainStage.getScene().getRoot().setEffect(null));
           Scene scene = new Scene(root);
           scene.setFill(Color.TRANSPARENT);
           popuStage.setScene(scene);
           popuStage.centerOnScreen();
           Platform.runLater(() -> {
               popuStage.setX(mainStage.getX() + (mainStage.getWidth() / 2) - (popuStage.getWidth() / 2));
               popuStage.setY(mainStage.getY() + (mainStage.getHeight() / 2) - (popuStage.getHeight() / 2));
           });
           popuStage.showAndWait();
       } catch (Exception e) {
            e.printStackTrace();
       }
    }

    private <T> void editData(
            String fxmlPath,
            Class<T> clazz,
            Consumer<T> controllerConsumer,
            Runnable afterShow
    ) {
        Object selected = tblData.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", "Pilih data terlebih dahulu!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            T controller = loader.getController();
            controllerConsumer.accept(controller);
            Stage mainStage = (Stage) btnTambahData.getScene().getWindow();
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
            if (afterShow != null) afterShow.run();
            tblData.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}