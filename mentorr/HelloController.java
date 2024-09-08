package com.example.mentorr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {
    @FXML
    private ImageView mentorImage;
    @FXML
    private HBox cardlayout;
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private HBox Dashboard;

    @FXML
    private Label usrtnameField;
    @FXML
    private HBox rankcontainer;

    @FXML
    private HBox recentMentorsContainer;

    @FXML
    private BorderPane rootPane;
    @FXML
    private HBox messagehold;

    public void initialize() {
        loadRecentMentors();
        loadRankedMentors();
        usrtnameField.setText(UserSession.getUsername());

    }

    private void loadRecentMentors() {
        String query = "SELECT u.name, m.field, m.color FROM Mentors m JOIN Users u ON m.mentor_id = u.user_id ORDER BY m.mentor_id DESC LIMIT 10";

        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Connection established: " + (conn != null));

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                System.out.println("No mentors found in the database.");
            }

            while (rs.next()) {
                System.out.println("Inside loop, mentor found.");

                String mentorName = rs.getString("name");
                String mentorField = rs.getString("field");
                String color = rs.getString("color");

                System.out.println("Loading mentor: " + mentorName + ", Field: " + mentorField + ", Color: " + color);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("card.fxml"));
                Node card = loader.load();


                System.out.println("Loaded card.fxml for mentor: " + mentorName);

                CardController controller = loader.getController();
                controller.setMentorName(mentorName);
                controller.setMentorField(mentorField);
                controller.setCardColor(color);

                recentMentorsContainer.getChildren().add(card);

                System.out.println("Added card to HBox for mentor: " + mentorName);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Print any exceptions to the console
        }
    }


    @FXML
    void messageclicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
        stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void DashboardClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void loadRankedMentors() {
        String query = "SELECT u.name, m.field, m.color FROM Mentors m JOIN Users u ON m.mentor_id = u.user_id ORDER BY m.ranks DESC LIMIT 10";

        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Connection established: " + (conn != null));

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
                System.out.println("No mentors found in the database.");
            }

            while (rs.next()) {
                System.out.println("Inside loop, mentor found.");

                String mentorName = rs.getString("name");
                String mentorField = rs.getString("field");
                String color = rs.getString("color");

                System.out.println("Loading mentor: " + mentorName + ", Field: " + mentorField + ", Color: " + color);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("card.fxml"));
                Node card = loader.load();


                System.out.println("Loaded card.fxml for mentor: " + mentorName);

                CardController controller = loader.getController();
                controller.setMentorName(mentorName);
                controller.setMentorField(mentorField);
                controller.setCardColor(color);

                rankcontainer.getChildren().add(card);

                System.out.println("Added card to HBox for mentor: " + mentorName);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Print any exceptions to the console
        }
    }
    }
