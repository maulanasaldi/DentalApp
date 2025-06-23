package com.example.appsaldi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Riwayat {
    private int idRiwayat;
    private int idPasien;
    private String namaPasien;
    private List<Integer> idPenyakitList = new ArrayList<>();
    private List<String> daftarPenyakit = new ArrayList<>();
    private List<Integer> idGejalaList = new ArrayList<>();
    private List<String> daftarGejala = new ArrayList<>();
    private LocalDate tglDiagnosa;
    private int idDokter;
    private String namaDokter;

    public Riwayat(int idPasien, String namaPasien, List<String> daftarPenyakit, List<String> daftarGejala, LocalDate tglDiagnosa, int idDokter, String namaDokter) {
        this.idPasien = idPasien;
        this.namaPasien = namaPasien;
        this.daftarPenyakit = daftarPenyakit;
        this.daftarGejala = daftarGejala;
        this.tglDiagnosa = tglDiagnosa;
        this.idDokter = idDokter;
        this.namaDokter = namaDokter;
    }

    public String getGejalaFormated() {
        return daftarGejala.stream()
                .map(s -> "- " + s)
                .collect(Collectors.joining("\n"));
    }

    public String getPenyakitFormated() {
        return daftarPenyakit.stream()
                .map(s -> "- " + s)
                .collect(Collectors.joining("\n"));
    }
}