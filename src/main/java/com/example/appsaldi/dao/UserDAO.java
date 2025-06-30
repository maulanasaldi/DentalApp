package com.example.appsaldi.dao;
import com.example.appsaldi.connectiondb.ConnectionDB;
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

    public boolean updateUser(UsersModel user) {
        String sql = "UPDATE user SET nama_lengkap = ?, username = ?, password = ?, foto = ? WHERE id_user = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNama());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getFotoPath());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UsersModel getById(String id) {
        UsersModel user = null;
        String query = "SELECT * FROM user WHERE id_user = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new UsersModel();
                user.setId(rs.getInt("id_user"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNama(rs.getString("nama_lengkap"));
                user.setFotoPath(rs.getString("foto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
