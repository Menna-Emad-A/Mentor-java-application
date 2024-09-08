package com.example.mentorr;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;

public class CardController {
    @FXML
    private ImageView mentorImage;

    @FXML
    private Label mentorNameLabel;

    @FXML
    private Label mentorFieldLabel;

    @FXML
    private HBox box;

    @FXML
    private ImageView mentorIcon;

    public void setMentorName(String name) {
        mentorNameLabel.setText(name);
    }

    public void setMentorField(String field) {
        mentorFieldLabel.setText(field);
    }

    public void setCardColor(String color) {
        box.setStyle("-fx-background-color: " + color + ";");
    }

    // This method sets all mentor data at once

    public void setMentorData(String name, String field, String color, String imageUrl) {
        mentorNameLabel.setText(name);
        mentorFieldLabel.setText(field);
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");

        // Load image
        try {
            File file = new File(imageUrl);
            Image image;
            if (file.exists()) {
                image = new Image(file.toURI().toString());
            } else {
                // Use a default placeholder image if the file doesn't exist
                image = new Image(getClass().getResourceAsStream("/com/example/mentorr/icons/default_image.png"));
            }
            mentorIcon.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
