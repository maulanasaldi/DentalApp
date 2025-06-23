package com.example.appsaldi.dao;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.model.Pasien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasienDAO {

    public void insertDataPasien(Pasien pasien) throws SQLException {
        String sql = "INSERT INTO pasien (nama_pasien, tgl_lahir, nik, pekerjaan, no_tlpn_pasien, alamat_pasien, status_notif) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionDB.getConnection()){
            PreparedStatement insertStatement = conn.prepareStatement(sql);
            insertStatement.setString(1, pasien.getNamaPasien());
            insertStatement.setDate(2, Date.valueOf(pasien.getTglLahirPasien()));
            insertStatement.setString(3, pasien.getNik());
            insertStatement.setString(4, pasien.getPekerjaan());
            insertStatement.setString(5, pasien.getNoTlpPasien());
            insertStatement.setString(6, pasien.getAlamatPasien());
            insertStatement.setBoolean(7, false); // status_notif default
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan data pasien: " + e.getMessage());
        }
    }


    public void updateStatusNotif() {
        String query = "UPDATE pasien SET status_notif = TRUE WHERE status_notif = FALSE";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatetDataPasien(Pasien pasien) throws SQLException {
        String sql = "UPDATE pasien SET nama_pasien = ?, tgl_lahir = ?, nik = ?, pekerjaan = ?, no_tlpn_pasien = ?, alamat_pasien = ? " +
                "WHERE id_pasien = ?";
        try (Connection conn = ConnectionDB.getConnection()){
            PreparedStatement updateStatement = conn.prepareStatement(sql);
            updateStatement.setString(1, pasien.getNamaPasien());
            updateStatement.setDate(2, Date.valueOf(pasien.getTglLahirPasien()));
            updateStatement.setString(3, pasien.getNik());
            updateStatement.setString(4, pasien.getPekerjaan());
            updateStatement.setString(5, pasien.getNoTlpPasien());
            updateStatement.setString(6, pasien.getAlamatPasien());
            updateStatement.setString(7, String.valueOf(pasien.getIdPasien()));
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan data pasien: " + e.getMessage());
        }
    }

    public void deletePasien(String idPasien) throws SQLException {
        String sql = "DELETE FROM pasien WHERE id_pasien = ?";
        try (Connection conn = ConnectionDB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idPasien);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Gagal menghapus data pasien: " + e.getMessage());
        }
    }


    public List<Pasien> getAllPasien() throws SQLException {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien";

        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pasien p = new Pasien(
                        rs.getInt("id_pasien"),
                        rs.getString("nama_pasien"),
                        rs.getDate("tgl_lahir").toLocalDate(),
                        rs.getString("nik"),
                        rs.getString("pekerjaan"),
                        rs.getString("no_tlpn_pasien"),
                        rs.getString("alamat_pasien"),
                        rs.getTimestamp("tgl_pendaftaran")
                );
                list.add(p);
            }
        }
        return list;
    }

    public List<Pasien> searchPasien(String keyword) throws SQLException {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien WHERE " +
                "id_pasien LIKE ? OR " +
                "nama_pasien LIKE ? OR " +
                "nik LIKE ? OR " +
                "pekerjaan LIKE ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            String wildcard = "%" + keyword + "%";
            stmt.setString(1, wildcard);
            stmt.setString(2, wildcard);
            stmt.setString(3, wildcard);
            stmt.setString(4, wildcard);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pasien p = new Pasien(
                        rs.getInt("id_pasien"),
                        rs.getString("nama_pasien"),
                        rs.getDate("tgl_lahir").toLocalDate(),
                        rs.getString("nik"),
                        rs.getString("pekerjaan"),
                        rs.getString("no_tlpn_pasien"),
                        rs.getString("alamat_pasien"),
                        rs.getTimestamp("tgl_pendaftaran")
                );
                list.add(p);
            }
        }
        return list;
    }

    public List<Pasien> getPasienByTanggal(LocalDate awal, LocalDate akhir) throws SQLException {
        List<Pasien> list = new ArrayList<>();
        String query = "SELECT * FROM pasien WHERE tgl_pendaftaran BETWEEN ? AND ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(awal));
            stmt.setDate(2, Date.valueOf(akhir));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pasien pasien = new Pasien();
                pasien.setIdPasien(rs.getInt("id_pasien"));
                pasien.setNamaPasien(rs.getString("nama_pasien"));

                Date tglLahir = rs.getDate("tgl_lahir");
                pasien.setTglLahirPasien(tglLahir != null ? tglLahir.toLocalDate() : null);

                pasien.setNik(rs.getString("nik"));
                pasien.setPekerjaan(rs.getString("pekerjaan"));
                pasien.setNoTlpPasien(rs.getString("no_tlpn_pasien"));
                pasien.setAlamatPasien(rs.getString("alamat_pasien"));
                pasien.setTglPendaftaran(rs.getTimestamp("tgl_pendaftaran"));

                list.add(pasien);
            }
        }
        return list;
    }

    public Map<String, Integer> getJumlahPasienPerPekerjaan() {
        Map<String, Integer> data = new HashMap<>();
        String query = "SELECT pekerjaan, COUNT(*) AS jumlah FROM pasien GROUP BY pekerjaan";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String pekerjaan = rs.getString("pekerjaan");
                int jumlah = rs.getInt("jumlah");

                if (pekerjaan == null || pekerjaan.isBlank()) {
                    pekerjaan = "Tidak Diketahui";
                }

                data.put(pekerjaan, jumlah);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<Pasien> getPasienTerbaru(int limit) {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien ORDER BY id_pasien DESC LIMIT ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pasien pasien = new Pasien();
                pasien.setIdPasien(rs.getInt("id_pasien"));
                pasien.setNamaPasien(rs.getString("nama_pasien"));
                pasien.setTglLahirPasien(rs.getDate("tgl_lahir").toLocalDate());
                pasien.setNik(rs.getString("nik"));
                pasien.setPekerjaan(rs.getString("pekerjaan"));
                pasien.setNoTlpPasien(rs.getString("no_tlpn_pasien"));
                pasien.setAlamatPasien(rs.getString("alamat_pasien"));
                list.add(pasien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Pasien> getPasienBelumDidiagnosa() {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien WHERE status_notif = 0 ORDER BY tgl_pendaftaran ASC"; // Ini yang benar!

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pasien pasien = new Pasien(
                        rs.getInt("id_pasien"),
                        rs.getString("nama_pasien"),
                        rs.getDate("tgl_lahir").toLocalDate(),
                        rs.getString("nik"),
                        rs.getString("pekerjaan"),
                        rs.getString("no_tlpn_pasien"),
                        rs.getString("alamat_pasien"),
                        rs.getTimestamp("tgl_pendaftaran")
                );
                list.add(pasien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void updateStatusNotifById(int idPasien) {
        String query = "UPDATE pasien SET status_notif = TRUE WHERE id_pasien = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idPasien);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
