package com.example.mentorr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class UIController implements Initializable {
    @FXML
    private VBox chatBox;  // Add this to your controller

    @FXML
    private Label label;

    @FXML
    private HBox Home;

    @FXML
    private TextField messageInput; // The field where the user types their message

    @FXML
    private TextField messageDisplayField; // The field next to the circle where the message will be displayed


    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private MessageDAO messageDAO = new MessageDAO();
    private int currentMentorId = 1; // Set this based on the selected mentor
    private int currentStudentId = 6; // Set this based on the current student


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Connect to the server
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to listen for incoming messages
            new Thread(() -> {
                try {
                    String incomingMessage;
                    while ((incomingMessage = in.readLine()) != null) {
                        final String messageToShow = incomingMessage;
                        Platform.runLater(() -> {
                            // Determine if the message is from the current user or the other client
                            boolean isSentByUser = messageToShow.startsWith("You: ");
                            addMessageToChatBox(messageToShow, isSentByUser);
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMessages(int mentorId, int studentId) {
        try {
            // Load the latest message to display
            List<String> messages = messageDAO.getMessages(mentorId, studentId);
            if (!messages.isEmpty()) {
                messageDisplayField.setText(messages.get(messages.size() - 1)); // Display the latest message
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSendMessage() {
        String message = messageInput.getText();
        out.println(message);  // Send to server
        addMessageToChatBox("You: " + message, true);
        messageInput.clear();}

    private void handleIncomingMessage(String message) {
        Platform.runLater(() -> {
            addMessageToChatBox(message, false);  // This ensures UI updates are made on the JavaFX Application Thread
        });
    }


    @FXML
    void HomeClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent browseView = loader.load();
        Scene browseScene = new Scene(browseView);

        // Get the stage (window) from any component in the current scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(browseScene);
    }
    public void closeConnection() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
    private Node createChatBubble(String message, boolean isSentByUser) {
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(200);  // Limit the width for text wrapping

        Circle statusIndicator = new Circle(5);
        statusIndicator.setFill(isSentByUser ? Color.BLUE : Color.RED); // Blue for sent, Red for received

        HBox bubble = new HBox();
        bubble.setPadding(new Insets(5));
        bubble.setSpacing(10);
        if (isSentByUser) {
            bubble.setAlignment(Pos.CENTER_RIGHT);
            bubble.setStyle("-fx-background-color: #cfe9ff; -fx-background-radius: 15;");
            bubble.getChildren().addAll(messageLabel, statusIndicator); // Order for sent: text first, then circle
        } else {
            bubble.setAlignment(Pos.CENTER_LEFT);
            bubble.setStyle("-fx-background-color: #e9e9e9; -fx-background-radius: 15;");
            bubble.getChildren().addAll(statusIndicator, messageLabel); // Order for received: circle first, then text
        }

        return bubble;
    }


    private void addMessageToChatBox(String message, boolean isSentByUser) {
        Node chatBubble = createChatBubble(message, isSentByUser);
        chatBox.getChildren().add(chatBubble);
    }

}
