package com.example.appsaldi.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Gejala {
    private int id_gejala;
    private String nama_gejala;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public Gejala(int id_gejala, String nama_gejala) {
        this.id_gejala = id_gejala;
        this.nama_gejala = nama_gejala;
    }

    public Gejala(String nama_gejala) {
        this.nama_gejala = nama_gejala;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Gejala gejala = (Gejala) obj;
        return id_gejala == gejala.id_gejala;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id_gejala);
    }

    @Override
    public String toString() {
        return nama_gejala + "(" + id_gejala + ")";
    }
}
