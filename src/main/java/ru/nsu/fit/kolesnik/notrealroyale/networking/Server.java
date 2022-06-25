package ru.nsu.fit.kolesnik.notrealroyale.networking;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    private final GameModel model;

    public Server(ServerSocket serverSocket, GameModel model) {
        this.serverSocket = serverSocket;
        this.model = model;
    }

    public void listenForServerRequest() {
        System.out.println("Server waiting for requests...");
        try {
            Socket socket = serverSocket.accept();
            System.out.println("Client accepted!");
            ClientHandler clientHandler = new ClientHandler(socket, model);
            clientHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

