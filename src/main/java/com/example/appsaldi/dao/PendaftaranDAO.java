package com.example.appsaldi.dao;

import com.example.appsaldi.connectiondb.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PendaftaranDAO {

    public void insert(int idPasien) throws SQLException {
        String sql = "INSERT INTO pendaftaran (id_pasien, tanggal_daftar, status_diagnosa) VALUES (?, NOW(), 'BELUM')";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPasien);
            stmt.executeUpdate();
        }
    }

}
