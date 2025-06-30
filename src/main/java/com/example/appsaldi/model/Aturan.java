package com.example.appsaldi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aturan {

    private int idAturan;
    private int idPenyakit;
    private List<String> daftarGejala = new ArrayList<>();

    public Aturan(int idPenyakit, List<String> daftarGejala) {
        this.idPenyakit = idPenyakit;
        this.daftarGejala = daftarGejala;
    }

    public String getGejalaFormated() {
        return daftarGejala.stream()
                .map(s -> " " + s)
                .collect(Collectors.joining("\n"));
    }
}
