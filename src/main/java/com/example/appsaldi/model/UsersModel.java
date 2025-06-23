package com.example.appsaldi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersModel {
    private Integer id;
    private String username;
    private String password;
    private String nama;
    private String status;
    private String fotoPath;
    private String notlp;
    private String haripraktik;
    private String jampraktik;

    public UsersModel(Integer id, String username, String password, String nama, String status, String fotoPath) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.status = status;
        this.fotoPath = fotoPath;
    }

    public UsersModel(Integer id, String nama, String notlp, String haripraktik, String jampraktik) {
        this.id = id;
        this.nama = nama;
        this.notlp = notlp;
        this.haripraktik = haripraktik;
        this.jampraktik = jampraktik;
    }

    @Override
    public String toString() {
        return nama + "(" + id + ")";

    }
}
