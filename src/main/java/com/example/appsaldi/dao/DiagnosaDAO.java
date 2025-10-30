package com.example.appsaldi.dao;

import com.example.appsaldi.model.Gejala;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DiagnosaDAO {

    private final Connection connection;

    public DiagnosaDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Integer> diagnosaPenyakit(List<Gejala> daftarGejalaDipilih) throws SQLException {
        List<Integer> penyakitTerdeteksi = new ArrayList<>();
        Map<Integer, List<Integer>> basisPengetahuan = new HashMap<>();

        // Ambil semua basis pengetahuan dalam satu query
        String sql = "SELECT id_penyakit, id_gejala FROM basis_pengetahuan";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int idPenyakit = resultSet.getInt("id_penyakit");
                int idGejala = resultSet.getInt("id_gejala");

                basisPengetahuan.computeIfAbsent(idPenyakit, k -> new ArrayList<>()).add(idGejala);
            }
        }

        Set<Integer> gejalaDipilihSet = daftarGejalaDipilih.stream()
                .map(Gejala::getId_gejala)
                .collect(Collectors.toSet());

        // Bandingkan gejala yang dipilih dengan basis pengetahuan
        for (Map.Entry<Integer, List<Integer>> entry : basisPengetahuan.entrySet()) {
            int idPenyakit = entry.getKey();
            List<Integer> gejalaPenyakit = entry.getValue();

            boolean cocok = gejalaPenyakit.stream().anyMatch(gejalaDipilihSet::contains);
            if (cocok) {
                penyakitTerdeteksi.add(idPenyakit);
            }
        }

        return penyakitTerdeteksi;
    }

    public String getNamaPenyakitById(int idPenyakit) throws SQLException {
        String sql = "SELECT nama_penyakit FROM penyakit WHERE id_penyakit = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPenyakit);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nama_penyakit");
                }
            }
        }
        return "Tidak Diketahui";
    }

    public String getSolusiById(int idPenyakit) throws SQLException {
        String sql = "SELECT solusi FROM penyakit WHERE id_penyakit = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPenyakit);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("solusi");
                }
            }
        }
        return "Tidak Diketahui";
    }

    public int simpanRiwayatDiagnosa(int idPasien, int idDokter) throws SQLException {
        String sqlDiagnosa = "INSERT INTO riwayat_diagnosa (id_pasien, id_user) VALUES (?, ?)";
        int idRiwayatDiagnosa = -1;

        try (PreparedStatement stmt = connection.prepareStatement(sqlDiagnosa, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idPasien);
            stmt.setInt(2, idDokter);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idRiwayatDiagnosa = rs.getInt(1);
                }
            }
        }

        return idRiwayatDiagnosa;
    }

    public void simpanRiwayatPenyakit(int idRiwayatDiagnosa, List<Integer> idPenyakitList) throws SQLException {
        String sql = "INSERT INTO riwayat_penyakit (id_riwayat_diagnosa, id_penyakit) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int idPenyakit : idPenyakitList) {
                stmt.setInt(1, idRiwayatDiagnosa);
                stmt.setInt(2, idPenyakit);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void simpanRiwayatGejala(List<Integer> idRiwayatDiagnosaList, List<Integer> idGejalaList) throws SQLException {
        String sql = "INSERT INTO riwayat_gejala (id_riwayat_diagnosa, id_gejala) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int idRiwayatDiagnosa : idRiwayatDiagnosaList) {
                for (int idGejala : idGejalaList) {
                    stmt.setInt(1, idRiwayatDiagnosa);
                    stmt.setInt(2, idGejala);
                    stmt.addBatch();
                }
            }
            stmt.executeBatch();
        }
    }
}