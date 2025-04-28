package com.chatapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class DemoThemeServer {
    @FXML
    private Button sendButton;

    @FXML
    private TextField messageField;

    @FXML
    private ScrollPane messagesPane;

    @FXML
    private VBox messagesBox;

    @FXML
    public void initialize() {
        // Initialize your controller here
        sendButton.setOnAction(event -> sendMessage());
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            // Add the message to the VBox
            javafx.scene.control.Label messageLabel = new javafx.scene.control.Label(message);
            messagesBox.getChildren().add(messageLabel);
            
            // Clear the text field
            messageField.clear();
            
            // Scroll to the bottom
            messagesPane.setVvalue(1.0);
        }
    }
} 