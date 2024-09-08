package com.example.mentorr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HomeController {
    @FXML
    private Label usrtnameField;
    @FXML
    private HBox message;
    @FXML
    private HBox Dashboard;
    @FXML
    private void handleBrowseButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("browse.fxml"));
        Parent browseView = loader.load();
        Scene browseScene = new Scene(browseView);

        // Get the stage (window) from any component in the current scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(browseScene);
    }
    @FXML
    void handleMessageButtonClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UI.fxml"));
        Parent browseView = loader.load();
        Scene browseScene = new Scene(browseView);

        // Get the stage (window) from any component in the current scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(browseScene);
    }
    @FXML
    void DashboardClick(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent browseView = loader.load();
        Scene browseScene = new Scene(browseView);

        // Get the stage (window) from any component in the current scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(browseScene);
    }
    public void initialize() {
        usrtnameField.setText(UserSession.getUsername());

    }}

