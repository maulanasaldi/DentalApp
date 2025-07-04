package com.example.appsaldi.model;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pasien {

    private Integer idPasien;
    private String namaPasien;
    private LocalDate tglLahirPasien;
    private String nik;
    private String pekerjaan;
    private String noTlpPasien;
    private String alamatPasien;
    private Timestamp tglPendaftaran;
    private Integer idPendaftaran;
    private String statusDiagnosa;

    public Pasien(String namaPasien, LocalDate tglLahirPasien, String nik, String pekerjaan, String noTlpPasien, String alamatPasien) {
        this.namaPasien = namaPasien;
        this.tglLahirPasien = tglLahirPasien;
        this.nik = nik;
        this.pekerjaan = pekerjaan;
        this.noTlpPasien = noTlpPasien;
        this.alamatPasien = alamatPasien;
    }

    public Pasien(Integer idPasien, String namaPasien, LocalDate tglLahirPasien, String nik, String pekerjaan, String noTlpPasien, String alamatPasien, Timestamp tglPendaftaran) {
        this.idPasien = idPasien;
        this.namaPasien = namaPasien;
        this.tglLahirPasien = tglLahirPasien;
        this.nik = nik;
        this.pekerjaan = pekerjaan;
        this.noTlpPasien = noTlpPasien;
        this.alamatPasien = alamatPasien;
        this.tglPendaftaran = tglPendaftaran;
    }
}
