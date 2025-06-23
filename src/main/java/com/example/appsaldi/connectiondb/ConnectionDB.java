package com.example.appsaldi.connectiondb;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

    private static final String URL  = "jdbc:mysql://localhost:3306/saldi";
    private static final String USER = "root";
    private static final String PASSWORD = "maulana22";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (Exception e) {
            System.out.println("Koneksi Gagal : " + e.getMessage());
        }
        return null;
    }


}
