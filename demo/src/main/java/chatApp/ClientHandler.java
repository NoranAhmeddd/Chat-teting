package chatApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error creating client handler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient != null) {
                    System.out.println("Received message from client: " + messageFromClient);
                    server.broadcastMessage(messageFromClient, this);
                }
            } catch (IOException e) {
                System.out.println("Error reading message: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
        closeEverything();
    }

    public void sendMessage(String message) {
        try {
            printWriter.println(message);
            printWriter.flush();
            System.out.println("Sent message to client: " + message);
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            closeEverything();
        }
    }

    private void closeEverything() {
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
        } catch (IOException e) {
            System.out.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
        server.removeClientHandler(this);
    }
} 