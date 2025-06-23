package com.example.appsaldi.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Penyakit {
    private Integer idPenyakit;
    private String namaPenyakit;
    private String solusi;

    public Penyakit(String namaPenyakit, String solusi) {
        this.namaPenyakit = namaPenyakit;
        this.solusi = solusi;
    }

    @Override
    public String toString() {
        return namaPenyakit + "(" + idPenyakit + ")";
    }
}
