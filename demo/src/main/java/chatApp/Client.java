package chatApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.layout.VBox;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Client initialized successfully");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating client");
            closeEverything(socket, bufferedReader, printWriter);
        }
    }

    public void sendMessageToServer(String message) {
        try {
            System.out.println("Sending message to server: " + message);
            printWriter.println(message);
            printWriter.flush();
            System.out.println("Message sent to server successfully");
        } catch (Exception e) {
            System.out.println("Error sending message to server: " + e.getMessage());
            e.printStackTrace();
            closeEverything(socket, bufferedReader, printWriter);
        }
    }

    public void receiveMessageFromServer(VBox vbox) {
        System.out.println("Starting message receiver thread");
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String messageFromServer = bufferedReader.readLine();
                    if (messageFromServer != null) {    
                        System.out.println("Received message from server: " + messageFromServer);
                        Controller.addLabel(messageFromServer, vbox);
                    }
                } catch (IOException e) {
                    System.out.println("Error receiving message from server: " + e.getMessage());
                    e.printStackTrace();
                    closeEverything(socket, bufferedReader, printWriter);
                    break;
                }
            }
        }).start();
    }   

    public void closeEverything(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("All resources closed successfully");
        } catch (IOException e) {
            System.out.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 