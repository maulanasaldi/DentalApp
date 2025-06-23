package com.example.appsaldi.dao;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.model.Pasien;
import com.example.appsaldi.model.UsersModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public UsersModel login(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                int id = set.getInt("id_user");
                String fotoPath = set.getString("foto");
                String nama = set.getString("nama_lengkap");
                String status = set.getString("role");
                return new UsersModel(id, username, password, nama, status, fotoPath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UsersModel> getAllUser() throws SQLException {
        List<UsersModel> list = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UsersModel p = new UsersModel(
                        rs.getInt("id_user"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("nama_lengkap"),
                        rs.getString("role"),
                        rs.getString("foto")
                );
                list.add(p);
            }
        }
        return list;
    }

    public List<UsersModel> getAllUserPopupData() throws SQLException {
        List<UsersModel> list = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UsersModel p = new UsersModel(
                        rs.getInt("id_user"),
                        rs.getString("nama_lengkap"),
                        rs.getString("no_tlp"),
                        rs.getString("hari_praktik"),
                        rs.getString("jam_praktik")
                );
                list.add(p);
            }
        }
        return list;
    }

}
