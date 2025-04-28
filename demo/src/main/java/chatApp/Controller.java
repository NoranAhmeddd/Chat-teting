package chatApp;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller implements Initializable {
    @FXML
    private AnchorPane mainAP;

    @FXML
    private Button sendButton;
    
    @FXML
    private TextField messageField;

    @FXML
    private ScrollPane messagesPane;
    
    @FXML
    private VBox messagesBox;
    
    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("Initializing controller...");
            client = new Client(new Socket("localhost", 12345));
            System.out.println("Connected to server");
            
            // Set up the messages box to auto-scroll
            messagesBox.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldVal, Number newVal) -> {
                messagesPane.setVvalue(newVal.doubleValue());
            });
            
            // Set up message receiving
            client.receiveMessageFromServer(messagesBox);
            
            // Set up send button click handler
            sendButton.setOnAction(event -> {
                System.out.println("Send button clicked");
                sendMessage();
            });
            
            // Set up Enter key handler for the message field
            messageField.setOnAction(event -> {
                System.out.println("Enter key pressed");
                sendMessage();
            });
            
        } catch (IOException e) {
            System.out.println("Could not connect to server. Please make sure the server is running.");
            e.printStackTrace();
        }
    }
    
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                System.out.println("Attempting to send message: " + message);
                
                // Create message display
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_RIGHT);
                hbox.setPadding(new Insets(5, 5, 5, 10));
                
                Text messageText = new Text(message);
                TextFlow textFlow = new TextFlow(messageText);
                textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                                "-fx-background-color: rgb(15,125,242);" +
                                "-fx-background-radius: 20px;");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                messageText.setFill(Color.color(0.934, 0.945, 0.996));
                
                hbox.getChildren().add(textFlow);
                
                // Add message to the chat
                Platform.runLater(() -> {
                    messagesBox.getChildren().add(hbox);
                    messageField.clear();
                });
                
                // Send message to server
                client.sendMessageToServer(message);
                System.out.println("Message sent successfully: " + message);
                
            } catch (Exception e) {
                System.out.println("Error sending message: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Message is empty, not sending");
        }
    }
    
    public static void addLabel(String message, VBox messagesBox) {
        System.out.println("Adding received message to chat: " + message);
        Platform.runLater(() -> {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(5, 5, 5, 10));
            
            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                             "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            
            hbox.getChildren().add(textFlow);
            messagesBox.getChildren().add(hbox);
        });
    }
}
