package com.chatapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class controllar extends Application implements Initializable {
    @FXML
    private Button sendButton;
    @FXML
    private TextField messageField;
    @FXML
    private ScrollPane messagesPane;
    @FXML
    private VBox messagesBox;
    private Server server;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/chatapp/chat.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Chat Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            server = new Server(new ServerSocket(12345));
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Server not started");
        }

        messagesBox.heightProperty().addListener((obs, oldValue, newValue) -> {
            messagesPane.setVvalue(newValue.doubleValue());
        });

        server.receiveMessageFromClient(messagesBox);
    }

    @FXML
    private void handleSendButton(ActionEvent event) {
        String messageToSend = messageField.getText();
        if(!messageToSend.isEmpty()) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(239,242,255)" +
                "-fx-background-color:rgb(15,125,242)" +
                "-fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.945, 0.996));
            hbox.getChildren().add(textFlow);
            messagesBox.getChildren().add(hbox);
            server.sendMessageToClient(messageToSend);
            messageField.clear();
        }
    }

    public static void addLabel(String messageFromClient, VBox vbox) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 5, 5, 10));
        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
            "-fx-background-color: rgb(233,233,235)" +
            "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hbox.getChildren().add(textFlow);
        Platform.runLater(() -> {
            vbox.getChildren().add(hbox);
        });
    }
}
