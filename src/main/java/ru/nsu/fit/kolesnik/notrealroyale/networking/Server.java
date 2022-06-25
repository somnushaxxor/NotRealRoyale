package ru.nsu.fit.kolesnik.notrealroyale.networking;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private List<Socket> sockets;
    private List<ClientHandler> clientHandlers;

    private final GameModel model;

    public Server(int port, GameModel model) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sockets = new ArrayList<>();
        clientHandlers = new ArrayList<>();
        this.model = model;
    }

    private void listenForRequest() {
        try {
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            ClientHandler clientHandler = new ClientHandler(socket, this, model);
            clientHandlers.add(clientHandler);
            clientHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        model.start();
        while (true) {
            listenForRequest();
        }
    }
}

