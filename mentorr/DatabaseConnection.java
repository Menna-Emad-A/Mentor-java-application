package com.example.mentorr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/mentor"; // Replace with your database URL
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "1404@Mimi"; // Replace with your MySQL password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error connecting to the database");
        }
        return connection;
    }
}
