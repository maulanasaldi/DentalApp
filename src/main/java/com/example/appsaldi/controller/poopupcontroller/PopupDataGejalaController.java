package com.example.appsaldi.controller.poopupcontroller;

import com.example.appsaldi.controller.formcontroller.FormAturanController;
import com.example.appsaldi.controller.formcontroller.FormRiwayatController;
import com.example.appsaldi.dao.GejalaDAO;
import com.example.appsaldi.dao.PenyakitDAO;
import com.example.appsaldi.model.Gejala;
import com.example.appsaldi.model.Penyakit;
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
import java.util.List;
import java.util.function.Consumer;

public class PopupDataGejalaController implements PopupMultiSelectController<Gejala> {

    @FXML private Button btnKeluar, btnPilih;
    @FXML private TextField txtCariDataGejala;
    @FXML private TableView<Gejala> tblDataGejala;

    @Setter private FormRiwayatController parentFormRiwayatController;
    @Setter private FormAturanController parenFormAturanController;
    @Setter private PopupMode mode = PopupMode.FORM_RIWAYAT;

    private final ObservableList<Gejala> masterData = FXCollections.observableArrayList();
    private Consumer<List<Gejala>> listener;

    @Override
    public void setListener(Consumer<List<Gejala>> listener) {
        this.listener = listener;
    }

    @FXML
    private void initialize() {
        tblDataGejala.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        setupTable();
        loadDataFromDatabase();

        btnPilih.setOnAction(event -> {
            ObservableList<Gejala> selected = tblDataGejala.getSelectionModel().getSelectedItems();
            if (!selected.isEmpty() && listener != null) {
                listener.accept(selected);
                closeWindow();
            }
        });
    }

    private void setupTable() {
        TableColumn<Gejala, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id_gejala"));

        TableColumn<Gejala, String> colNamaGejala = new TableColumn<>("Penyakit");
        colNamaGejala.setCellValueFactory(new PropertyValueFactory<>("nama_gejala"));

        tblDataGejala.getColumns().addAll(colId, colNamaGejala);
        tblDataGejala.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colId.prefWidthProperty().bind(tblDataGejala.widthProperty().multiply(0.1));
        colNamaGejala.prefWidthProperty().bind(tblDataGejala.widthProperty().multiply(0.9));
    }


    private void loadDataFromDatabase() {
        GejalaDAO gejalaDAO = new GejalaDAO();
        masterData.setAll(gejalaDAO.getAllGejala()); // Ambil user dengan role = "dokter"

        FilteredList<Gejala> filteredData = new FilteredList<>(masterData, p -> true);

        txtCariDataGejala.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            filteredData.setPredicate(penyakit ->
                    penyakit.getNama_gejala().toLowerCase().contains(filter)
            );
        });

        SortedList<Gejala> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblDataGejala.comparatorProperty());
        tblDataGejala.setItems(sortedData);

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
