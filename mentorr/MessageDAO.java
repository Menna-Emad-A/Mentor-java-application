package com.example.mentorr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public List<String> getMessages(int mentorId, int studentId) throws SQLException {
        List<String> messages = new ArrayList<>();
        String query = "SELECT content FROM Messages WHERE mentor_id = ? AND student_id = ? ORDER BY timestamp ASC";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, mentorId);
            statement.setInt(2, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                messages.add(resultSet.getString("content"));
            }
        }
        return messages;
    }

    public void sendMessage(int mentorId, int studentId, String content) throws SQLException {
        String query = "INSERT INTO Messages (mentor_id, student_id, content) VALUES (?, ?, ?)";
        try (Connection connection = new DatabaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, mentorId);
            statement.setInt(2, studentId);
            statement.setString(3, content);
            statement.executeUpdate();
        }
    }
}

