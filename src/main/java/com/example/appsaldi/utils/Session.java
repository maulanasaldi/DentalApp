package com.example.appsaldi.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
public class Session {

    @Getter
    private static int currentUserId;

    @Getter
    private static String currentRole; // Tambahkan ini

    public static void setCurrentUserId(int currentUserId) {
        Session.currentUserId = currentUserId;
    }

    public static void setCurrentRole(String currentRole) { // Tambahkan ini
        Session.currentRole = currentRole;
    }

    public static void clear() {
        currentUserId = 0;
        currentRole = null; // Tambahkan ini supaya role juga dihapus saat logout
    }
}
