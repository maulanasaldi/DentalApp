package com.example.appsaldi.dao;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.model.Gejala;
import com.example.appsaldi.model.Penyakit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GejalaDAO {

    public void insertDataGejala(Gejala gejala) throws SQLException {
        String sql = "INSERT INTO gejala (id_gejala, gejala) VALUES (?,?)";
        try (Connection conn = ConnectionDB.getConnection()){
            PreparedStatement insertStatement = conn.prepareStatement(sql);
            insertStatement.setInt(1, gejala.getId_gejala());
            insertStatement.setString(2, gejala.getNama_gejala());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan data gejala: " + e.getMessage());
        }
    }

    public void updatetDataGejala(Gejala gejala) throws SQLException {
        String sql = "UPDATE gejala SET gejala = ? WHERE id_gejala = ?";
        try (Connection conn = ConnectionDB.getConnection()){
            PreparedStatement updateStatement = conn.prepareStatement(sql);
            updateStatement.setString(1, gejala.getNama_gejala());
            updateStatement.setInt(2, gejala.getId_gejala());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan data gejala: " + e.getMessage());
        }
    }

    public void deleteGejala(String idGejala) throws SQLException {
        String sql = "DELETE FROM gejala WHERE id_gejala = ?";
        try (Connection conn = ConnectionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idGejala);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menghapus data gejala: " + e.getMessage());
        }
    }

    public List<Gejala> getAllGejala() {
        List<Gejala> list = new ArrayList<>();
        String query = "SELECT * FROM gejala";

        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                list.add(new Gejala(rs.getInt("id_gejala"), rs.getString("gejala")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Gejala> searchGejala(String keyword) throws SQLException {
        List<Gejala> list = new ArrayList<>();
        String sql = "SELECT * FROM gejala WHERE " +
                "LOWER (id_gejala) LIKE ? OR " +
                "LOWER (gejala) LIKE ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            String wildcard = "%" + keyword.toLowerCase() + "%";
            for (int i = 1; i <= 2; i++) {
                stmt.setString(i, wildcard);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Gejala p = new Gejala (
                        rs.getInt("id_gejala"),
                        rs.getString("gejala")
                );
                list.add(p);
            }
        }
        return list;
    }
}
