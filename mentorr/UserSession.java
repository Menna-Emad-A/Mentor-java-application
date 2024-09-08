package com.example.mentorr;

public class UserSession {
    private static String username; // Static field to hold the username

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String newUsername) {
        username = newUsername;
    }
}
