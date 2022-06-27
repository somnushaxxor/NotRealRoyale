package ru.nsu.fit.kolesnik.notrealroyale.networking;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    private final GameModel model;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.model = new GameModel();
    }

    public void listenForServerRequest() {
        model.start();
        System.out.println("Server waiting for requests...");
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client accepted!");
                ClientHandler clientHandler = new ClientHandler(socket, model);
                clientHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
                model.stop();
                closeServerSocket();
            }
        }
    }

    private void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

