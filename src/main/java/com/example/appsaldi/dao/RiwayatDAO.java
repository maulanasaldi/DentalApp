package com.example.appsaldi.dao;

import com.example.appsaldi.connectiondb.ConnectionDB;
import com.example.appsaldi.model.Riwayat;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RiwayatDAO {

    public void updateRiwayat(Riwayat riwayat, boolean isPenyakitDiubah, boolean isGejalaDiubah) {
        String sqlUpdate = "UPDATE riwayat_diagnosa SET id_pasien = ?, tgl_diagnosa = ? WHERE id_riwayat_diagnosa = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

            conn.setAutoCommit(false); // Transaksi manual

            // Update data riwayat
            pstmt.setInt(1, riwayat.getIdPasien());
            pstmt.setDate(2, Date.valueOf(riwayat.getTglDiagnosa()));
            pstmt.setInt(3, riwayat.getIdRiwayat());
            pstmt.executeUpdate();

            // Jika penyakit diubah
            if (isPenyakitDiubah) {
                try (PreparedStatement pstmtDelete = conn.prepareStatement("DELETE FROM riwayat_penyakit WHERE id_riwayat_diagnosa = ?")) {
                    pstmtDelete.setInt(1, riwayat.getIdRiwayat());
                    pstmtDelete.executeUpdate();
                }

                if (riwayat.getIdPenyakitList() != null) {
                    for (Integer idPenyakit : riwayat.getIdPenyakitList()) {
                        try (PreparedStatement pstmtInsert = conn.prepareStatement("INSERT INTO riwayat_penyakit (id_riwayat_diagnosa, id_penyakit) VALUES (?, ?)")) {
                            pstmtInsert.setInt(1, riwayat.getIdRiwayat());
                            pstmtInsert.setInt(2, idPenyakit);
                            pstmtInsert.executeUpdate();
                        }
                    }
                }
            }

            // Jika gejala diubah
            if (isGejalaDiubah) {
                try (PreparedStatement pstmtDelete = conn.prepareStatement("DELETE FROM riwayat_gejala WHERE id_riwayat_diagnosa = ?")) {
                    pstmtDelete.setInt(1, riwayat.getIdRiwayat());
                    pstmtDelete.executeUpdate();
                }

                if (riwayat.getIdGejalaList() != null) {
                    for (Integer idGejala : riwayat.getIdGejalaList()) {
                        try (PreparedStatement pstmtInsert = conn.prepareStatement("INSERT INTO riwayat_gejala (id_riwayat_diagnosa, id_gejala) VALUES (?, ?)")) {
                            pstmtInsert.setInt(1, riwayat.getIdRiwayat());
                            pstmtInsert.setInt(2, idGejala);
                            pstmtInsert.executeUpdate();
                        }
                    }
                }
            }

            conn.commit(); // Simpan semua perubahan
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean deleteRiwayat(int idRiwayat) {
        String sqlDeleteGejala = "DELETE FROM riwayat_gejala WHERE id_riwayat_diagnosa = ?";
        String sqlDeletePenyakit = "DELETE FROM riwayat_penyakit WHERE id_riwayat_diagnosa = ?";
        String sqlDeleteRiwayat = "DELETE FROM riwayat_diagnosa WHERE id_riwayat_diagnosa = ?";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteGejala)) {
                stmt.setInt(1, idRiwayat);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeletePenyakit)) {
                stmt.setInt(1, idRiwayat);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteRiwayat)) {
                stmt.setInt(1, idRiwayat);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Riwayat> getRiwayat() {
        List<Riwayat> list = new ArrayList<>();
        String sql = "SELECT r.id_riwayat_diagnosa AS id_riwayat, r.tgl_diagnosa, p.id_pasien, p.nama_pasien, " +
                "GROUP_CONCAT(DISTINCT pk.nama_penyakit SEPARATOR ', ') AS daftar_penyakit, " +
                "GROUP_CONCAT(DISTINCT g.gejala SEPARATOR ', ') AS daftar_gejala, " +
                "d.nama_lengkap AS nama_dokter " +
                "FROM riwayat_diagnosa r " +
                "JOIN pasien p ON r.id_pasien = p.id_pasien " +
                "JOIN user d ON r.id_user = d.id_user " +
                "JOIN riwayat_gejala rg ON r.id_riwayat_diagnosa = rg.id_riwayat_diagnosa " +
                "JOIN gejala g ON rg.id_gejala = g.id_gejala " +
                "JOIN riwayat_penyakit rp ON r.id_riwayat_diagnosa = rp.id_riwayat_diagnosa " +
                "JOIN penyakit pk ON rp.id_penyakit = pk.id_penyakit " +
                "GROUP BY r.id_riwayat_diagnosa, r.tgl_diagnosa, p.id_pasien, d.nama_lengkap";

        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Riwayat riwayat = new Riwayat();
                riwayat.setIdRiwayat(resultSet.getInt("id_riwayat"));
                riwayat.setTglDiagnosa(resultSet.getDate("tgl_diagnosa").toLocalDate());
                riwayat.setIdPasien(resultSet.getInt("id_pasien"));
                riwayat.setNamaPasien(resultSet.getString("nama_pasien"));
                riwayat.setNamaDokter(resultSet.getString("nama_dokter"));
                riwayat.setDaftarPenyakit(Arrays.asList(resultSet.getString("daftar_penyakit").split(",\\s*")));
                riwayat.setDaftarGejala(Arrays.asList(resultSet.getString("daftar_gejala").split(",\\s*")));

                list.add(riwayat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> getGejalaByRiwayat(String idRiwayat) {
        List<String> gejala = new ArrayList<>();
        String sql = "SELECT g.gejala FROM riwayat_gejala rg " +
                "JOIN gejala g ON rg.id_gejala = g.id_gejala " +
                "WHERE rg.id_riwayat_diagnosa = ?";
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, idRiwayat);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                gejala.add(resultSet.getString("gejala"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gejala;
    }

    public List<Riwayat> searchRiwayat(String keyword) throws SQLException {
        List<Riwayat> list = new ArrayList<>();
        String sql = "SELECT r.id_riwayat_diagnosa, r.tgl_diagnosa, " +
                "p.nama_pasien, d.nama_lengkap, " +
                "GROUP_CONCAT(DISTINCT pk.nama_penyakit SEPARATOR ', ') AS daftar_penyakit, " +
                "r.id_pasien, r.id_user " +
                "FROM riwayat_diagnosa r " +
                "JOIN pasien p ON r.id_pasien = p.id_pasien " +
                "JOIN user d ON r.id_user = d.id_user " +
                "JOIN riwayat_penyakit rp ON r.id_riwayat_diagnosa = rp.id_riwayat_diagnosa " +
                "JOIN penyakit pk ON rp.id_penyakit = pk.id_penyakit " +
                "WHERE LOWER(p.nama_pasien) LIKE ? " +
                "OR LOWER(d.nama_lengkap) LIKE ? " +
                "OR LOWER(pk.nama_penyakit) LIKE ? " +
                "GROUP BY r.id_riwayat_diagnosa, r.tgl_diagnosa, p.nama_pasien, d.nama_lengkap, r.id_pasien, r.id_user";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String wildcard = "%" + keyword.toLowerCase() + "%";
            stmt.setString(1, wildcard);
            stmt.setString(2, wildcard);
            stmt.setString(3, wildcard);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idRiwayat = rs.getInt("id_riwayat_diagnosa");
                List<String> gejalaList = getGejalaByRiwayat(String.valueOf(idRiwayat));
                Riwayat p = new Riwayat(
                        rs.getInt("id_pasien"),
                        rs.getString("nama_pasien"),
                        Arrays.asList(rs.getString("daftar_penyakit").split(",\\s*")),
                        gejalaList,
                        rs.getDate("tgl_diagnosa").toLocalDate(),
                        rs.getInt("id_user"),
                        rs.getString("nama_lengkap")
                );
                p.setIdRiwayat(idRiwayat);
                list.add(p);
            }
        }
        return list;
    }

    // metode statistik tetap sama
    public int getJumlahDiagnosaBulanIni() {
        int jumlah = 0;
        String sql = "SELECT COUNT(*) FROM riwayat_diagnosa " +
                "WHERE MONTH(tgl_diagnosa) = MONTH(CURRENT_DATE()) " +
                "AND YEAR(tgl_diagnosa) = YEAR(CURRENT_DATE())";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                jumlah = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jumlah;
    }

    public String getPenyakitTerbanyak() {
        String penyakit = "Tidak ada data";
        String sql = "SELECT p.nama_penyakit, COUNT(*) as jumlah " +
                "FROM riwayat_penyakit rp " +
                "JOIN penyakit p ON rp.id_penyakit = p.id_penyakit " +
                "GROUP BY rp.id_penyakit " +
                "ORDER BY jumlah DESC LIMIT 1";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                penyakit = rs.getString("nama_penyakit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return penyakit;
    }
}
