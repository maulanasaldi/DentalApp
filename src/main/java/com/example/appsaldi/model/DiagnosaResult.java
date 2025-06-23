package com.example.appsaldi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosaResult {
    private List<Integer> diseaseId;
    private String namaPasien;
    private String namaPenyakit;
    private String solusi;
}
