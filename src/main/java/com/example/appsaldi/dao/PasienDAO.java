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
            int idBaru = getLastInsertedId();
            new PendaftaranDAO().insert(idBaru);
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan data pasien: " + e.getMessage());
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
        String sql = """
            SELECT p.*, d.tanggal_daftar, d.id_pendaftaran
            FROM pasien p
            JOIN pendaftaran d ON p.id_pasien = d.id_pasien
            WHERE d.status_diagnosa = 'BELUM'
            ORDER BY d.tanggal_daftar ASC
        """;

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            String wildcard = "%" + keyword + "%";
            stmt.setString(1, wildcard);
            stmt.setString(2, wildcard);
            stmt.setString(3, wildcard);
            stmt.setString(4, wildcard);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pasien pasien = new Pasien(
                    rs.getInt("id_pasien"),
                    rs.getString("nama"),
                    rs.getDate("tgl_lahir").toLocalDate(),
                    rs.getString("nik"),
                    rs.getString("pekerjaan"),
                    rs.getString("no_tlp"),
                    rs.getString("alamat"),
                    rs.getTimestamp("tanggal_daftar")
                );
                pasien.setIdPendaftaran(rs.getInt("id_pendaftaran"));
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
        String sql = """
            SELECT
                pendaftaran.id_pendaftaran,
                pendaftaran.tanggal_daftar,
                pendaftaran.status_diagnosa,
                pasien.id_pasien,
                pasien.nama_pasien,
                pasien.tgl_lahir,
                pasien.nik,
                pasien.pekerjaan,
                pasien.no_tlpn_pasien,
                pasien.alamat_pasien
            FROM pendaftaran
            JOIN pasien ON pendaftaran.id_pasien = pasien.id_pasien
            ORDER BY pendaftaran.id_pendaftaran DESC
            LIMIT ?
        """;

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pasien pasien = new Pasien();
                pasien.setIdPendaftaran(rs.getInt("id_pendaftaran"));
                pasien.setIdPasien(rs.getInt("id_pasien"));
                pasien.setNamaPasien(rs.getString("nama_pasien"));
                pasien.setTglPendaftaran(rs.getTimestamp("tanggal_daftar"));
                pasien.setStatusDiagnosa(rs.getString("status_diagnosa"));
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
        String sql = """
        SELECT p.*, d.id_pendaftaran, d.tanggal_daftar
        FROM pasien p
        JOIN pendaftaran d ON p.id_pasien = d.id_pasien
        WHERE d.status_diagnosa = 'BELUM' AND d.status_notif = FALSE
        ORDER BY d.tanggal_daftar ASC
    """;

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
                pasien.setIdPendaftaran(rs.getInt("id_pendaftaran"));
                list.add(pasien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateStatusNotifById(String status, int idPendaftaran) {
        String query = "UPDATE pendaftaran SET status_diagnosa = ? WHERE id_pendaftaran = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, idPendaftaran);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLastInsertedId() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID()";
        try (Connection conn = ConnectionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public void updateStatusNotifPendaftaran(int idPendaftaran) {
        String query = "UPDATE pendaftaran SET status_notif = TRUE WHERE id_pendaftaran = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idPendaftaran);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}