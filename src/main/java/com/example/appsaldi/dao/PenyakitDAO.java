package com.example.appsaldi.dao;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.model.Penyakit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenyakitDAO {

    public void insertDataPenyakit(Penyakit penyakit) throws SQLException {
        String sql = "INSERT INTO penyakit (nama_penyakit, solusi) VALUES (?,?)";
        try (Connection conn = ConnectionDB.getConnection()){
            PreparedStatement insertStatement = conn.prepareStatement(sql);
            insertStatement.setString(1, penyakit.getNamaPenyakit());
            insertStatement.setString(2, penyakit.getSolusi());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan data penyakit: " + e.getMessage());
        }
    }

    public void updatetDataPenyakit(Penyakit penyakit) throws SQLException {
        String sql = "UPDATE penyakit SET nama_penyakit = ?, solusi = ? WHERE id_penyakit = ?";
        try (Connection conn = ConnectionDB.getConnection()){
            PreparedStatement updateStatement = conn.prepareStatement(sql);
            updateStatement.setString(1, penyakit.getNamaPenyakit());
            updateStatement.setString(2, penyakit.getSolusi());
            updateStatement.setInt(3, penyakit.getIdPenyakit());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan data penyakit: " + e.getMessage());
        }
    }

    public void deletePenyakit(String idPenyakit) throws SQLException {
        String sql = "DELETE FROM penyakit WHERE id_penyakit = ?";
        try (Connection conn = ConnectionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPenyakit);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menghapus data penyakit: " + e.getMessage());
        }
    }

    public List<Penyakit> getAllPenyakit() throws SQLException {
        List<Penyakit> list = new ArrayList<>();
        String sql = "SELECT * FROM penyakit";

        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Penyakit p = new Penyakit(
                        rs.getInt("id_penyakit"),
                        rs.getString("nama_penyakit"),
                        rs.getString("solusi")
                );
                list.add(p);
            }
        }
        return list;
    }

    public List<Penyakit> searchPenyakit(String keyword) throws SQLException {
        List<Penyakit> list = new ArrayList<>();
        String sql = "SELECT * FROM penyakit WHERE " +
                "LOWER (id_penyakit) LIKE ? OR " +
                "LOWER (nama_penyakit) LIKE ? OR " +
                "LOWER (solusi) LIKE ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            String wildcard = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, wildcard);
            stmt.setString(2, wildcard);
            stmt.setString(3, wildcard);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Penyakit p = new Penyakit(
                        rs.getInt("id_penyakit"),
                        rs.getString("nama_penyakit"),
                        rs.getString("solusi")
                );
                list.add(p);
            }
        }
        return list;
    }

}
