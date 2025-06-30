package com.example.appsaldi.controller.poopupcontroller;

import com.example.appsaldi.controller.formcontroller.FormRiwayatController;
import com.example.appsaldi.dao.PenyakitDAO;
import com.example.appsaldi.model.Penyakit;
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
import java.util.List;
import java.util.function.Consumer;

public class PopupDataPenyakitController implements PopupMultiSelectController<Penyakit> {

    @FXML private Button btnKeluar, btnPilih;
    @FXML private TextField txtCariDataPenyakit;
    @FXML private TableView<Penyakit> tblDataPenyakit;

    @Setter private FormRiwayatController parentFormRiwayatController;

    private final ObservableList<Penyakit> masterData = FXCollections.observableArrayList();
    private Consumer<List<Penyakit>> listener;
    @Setter private PopupMode mode = PopupMode.FORM_RIWAYAT; // default

    @Override
    public void setListener(Consumer<List<Penyakit>> listener) {
        this.listener = listener;
    }

    @FXML
    private void initialize() {
        tblDataPenyakit.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        setupTable();
        loadDataFromDatabase();

        // Handle tombol pilih
        if (mode == PopupMode.FORM_ATURAN) {
            btnPilih.setDisable(true); // nonaktifkan tombol pilih
        } else {
            btnPilih.setDisable(false);
            btnPilih.setOnAction(event -> {
                ObservableList<Penyakit> selected = tblDataPenyakit.getSelectionModel().getSelectedItems();
                if (!selected.isEmpty() && listener != null) {
                    listener.accept(selected);
                    closeWindow();
                }
            });
        }

        // Handle klik dua kali
        tblDataPenyakit.setRowFactory(tv -> {
            TableRow<Penyakit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Penyakit selectedPenyakit = row.getItem();
                    if (listener != null) {
                        listener.accept(List.of(selectedPenyakit));
                        closeWindow();
                    }
                }
            });
            return row;
        });
    }


    private void setupTable() {
        TableColumn<Penyakit, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("idPenyakit"));

        TableColumn<Penyakit, String> colNamaPenyakit = new TableColumn<>("Penyakit");
        colNamaPenyakit.setCellValueFactory(new PropertyValueFactory<>("namaPenyakit"));

        TableColumn<Penyakit, String> colSolusi = new TableColumn<>("Solusi");
        colSolusi.setCellValueFactory(new PropertyValueFactory<>("solusi"));

        tblDataPenyakit.getColumns().addAll(colId, colNamaPenyakit, colSolusi);
        tblDataPenyakit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colId.prefWidthProperty().bind(tblDataPenyakit.widthProperty().multiply(0.1));
        colNamaPenyakit.prefWidthProperty().bind(tblDataPenyakit.widthProperty().multiply(0.3));
        colSolusi.prefWidthProperty().bind(tblDataPenyakit.widthProperty().multiply(0.6));
    }


    private void loadDataFromDatabase() {
        PenyakitDAO userDAO = new PenyakitDAO();
        try {
            masterData.setAll(userDAO.getAllPenyakit()); // Ambil user dengan role = "dokter"

            FilteredList<Penyakit> filteredData = new FilteredList<>(masterData, p -> true);

            txtCariDataPenyakit.textProperty().addListener((obs, oldVal, newVal) -> {
                String filter = newVal.toLowerCase();
                filteredData.setPredicate(penyakit ->
                        penyakit.getNamaPenyakit().toLowerCase().contains(filter)
                                || penyakit.getSolusi().toLowerCase().contains(filter)
                );
            });

            SortedList<Penyakit> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblDataPenyakit.comparatorProperty());
            tblDataPenyakit.setItems(sortedData);

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
