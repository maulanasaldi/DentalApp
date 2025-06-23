package com.example.appsaldi.controller.poopupcontroller;

import com.example.appsaldi.controller.formcontroller.FormRiwayatController;
import com.example.appsaldi.dao.UserDAO;
import com.example.appsaldi.model.UsersModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.SQLException;
import java.util.function.Consumer;

public class PopupDataDokterController implements PopupSingleSelectController<UsersModel> {

    @FXML private Button btnKeluar;
    @FXML private TextField txtCariDataDokter;
    @FXML private TableView<UsersModel> tblDataDokter;

    @Setter private FormRiwayatController parentFormRiwayatController;

    private final ObservableList<UsersModel> masterData = FXCollections.observableArrayList();
    private Consumer<UsersModel> listener;

    @Override
    public void setListener(Consumer<UsersModel> listener) {
        this.listener = listener;
    }

    @FXML
    private void initialize() {
        setupTable();
        loadDataFromDatabase();

        tblDataDokter.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                UsersModel selected = tblDataDokter.getSelectionModel().getSelectedItem();
                if (selected != null && listener != null) {
                    listener.accept(selected);
                    closeWindow();
                }
            }
        });
    }

    private void setupTable() {
        TableColumn<UsersModel, Integer> colId = new TableColumn<>("ID Dokter");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<UsersModel, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<UsersModel, String> colNoTlp = new TableColumn<>("No. Telepon");
        colNoTlp.setCellValueFactory(new PropertyValueFactory<>("notlp"));

        TableColumn<UsersModel, String> colHari = new TableColumn<>("Hari Praktik");
        colHari.setCellValueFactory(new PropertyValueFactory<>("haripraktik"));

        TableColumn<UsersModel, String> colJam = new TableColumn<>("Jam Praktik");
        colJam.setCellValueFactory(new PropertyValueFactory<>("jampraktik"));

        tblDataDokter.getColumns().addAll(colId, colNama, colNoTlp, colHari, colJam);
    }

    private void loadDataFromDatabase() {
        UserDAO userDAO = new UserDAO();
        try {
            masterData.setAll(userDAO.getAllUserPopupData()); // Ambil user dengan role = "dokter"

            FilteredList<UsersModel> filteredData = new FilteredList<>(masterData, p -> true);

            txtCariDataDokter.textProperty().addListener((obs, oldVal, newVal) -> {
                String filter = newVal.toLowerCase();
                filteredData.setPredicate(dokter ->
                        dokter.getNama().toLowerCase().contains(filter)
                                || dokter.getNotlp().toLowerCase().contains(filter)
                                || dokter.getHaripraktik().toLowerCase().contains(filter)
                                || dokter.getJampraktik().toLowerCase().contains(filter)
                                || String.valueOf(dokter.getId()).contains(filter)
                );
            });

            SortedList<UsersModel> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblDataDokter.comparatorProperty());
            tblDataDokter.setItems(sortedData);

        } catch (SQLException e) {
            e.printStackTrace(); // Bisa diganti dengan alert error
        }
    }

    @FXML
    private void handelKeluar() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnKeluar.getScene().getWindow();
        stage.close();
    }
}
