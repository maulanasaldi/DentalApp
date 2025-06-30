package com.example.appsaldi.dao;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.model.Aturan;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowladgeDAO {

    public void insertDataAturan(Aturan aturan) throws SQLException {
        String sql = "INSERT INTO basis_pengetahuan (id_penyakit, id_gejala) VALUES (?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (String idGejalaStr : aturan.getDaftarGejala()) {
                int idGejala = Integer.parseInt(idGejalaStr); // karena txtIDGejala isinya 1,2,3
                stmt.setInt(1, aturan.getIdPenyakit());
                stmt.setInt(2, idGejala);
                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan data aturan: " + e.getMessage());
        }
    }

    public void updateDataAturan(int idAturan, Aturan aturan) throws SQLException {
        // Contoh implementasi dasar: hapus dulu semua gejala, lalu insert ulang
        Connection conn = ConnectionDB.getConnection();

        // Hapus semua aturan lama
        String deleteQuery = "DELETE FROM basis_pengetahuan WHERE id_aturan = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, idAturan);
            ps.executeUpdate();
        }

        // Insert ulang gejala-gejala baru
        String insertQuery = "INSERT INTO basis_pengetahuan (id_aturan, id_penyakit, id_gejala) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
            for (String idGejala : aturan.getDaftarGejala()) {
                ps.setInt(1, idAturan);
                ps.setInt(2, aturan.getIdPenyakit());
                ps.setInt(3, Integer.parseInt(idGejala));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void deleteAturan(String idPenyakit) throws SQLException {
        String sql = "DELETE FROM basis_pengetahuan WHERE id_aturan = ?";
        try (Connection conn = ConnectionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPenyakit);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menghapus data penyakit: " + e.getMessage());
        }
    }

    public List<Aturan> getAllAturan() throws SQLException {
        Map<Integer, Aturan> mapAturan = new HashMap<>();

        String sql = "SELECT bp.id_aturan, bp.id_penyakit, g.id_gejala " +
                "FROM basis_pengetahuan bp " +
                "JOIN gejala g ON bp.id_gejala = g.id_gejala";

        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idAturan = rs.getInt("id_aturan");
                int idPenyakit = rs.getInt("id_penyakit");
                String idGejala = rs.getString("id_gejala"); // Ambil ID-nya (bukan nama)

                Aturan aturan = mapAturan.get(idAturan);
                if (aturan == null) {
                    aturan = new Aturan();
                    aturan.setIdAturan(idAturan);
                    aturan.setIdPenyakit(idPenyakit);
                    aturan.setDaftarGejala(new ArrayList<>());
                    mapAturan.put(idAturan, aturan);
                }

                aturan.getDaftarGejala().add(idGejala);
            }
        }

        return new ArrayList<>(mapAturan.values());
    }


    public List<Aturan> searchAturan(String keyword) throws SQLException {
        List<Aturan> list = new ArrayList<>();
        String sql = "SELECT bp.id_aturan, bp.id_penyakit, g.nama_gejala " +
                "FROM basis_pengetahuan bp " +
                "JOIN gejala g ON bp.id_gejala = g.id_gejala " +
                "WHERE CAST(bp.id_aturan AS CHAR) LIKE ? OR " +
                "CAST(bp.id_penyakit AS CHAR) LIKE ? OR " +
                "LOWER(g.nama_gejala) LIKE ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String wildcard = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, wildcard);
            stmt.setString(2, wildcard);
            stmt.setString(3, wildcard);

            ResultSet rs = stmt.executeQuery();
            Map<Integer, Aturan> mapAturan = new HashMap<>();

            while (rs.next()) {
                int idAturan = rs.getInt("id_aturan");
                int idPenyakit = rs.getInt("id_penyakit");
                String namaGejala = rs.getString("nama_gejala");

                Aturan aturan = mapAturan.get(idAturan);
                if (aturan == null) {
                    aturan = new Aturan();
                    aturan.setIdAturan(idAturan);
                    aturan.setIdPenyakit(idPenyakit);
                    aturan.setDaftarGejala(new ArrayList<>());
                    mapAturan.put(idAturan, aturan);
                }

                aturan.getDaftarGejala().add(namaGejala);
            }

            list.addAll(mapAturan.values());
        }

        return list;
    }

}
