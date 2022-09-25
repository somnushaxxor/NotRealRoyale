package ru.nsu.fit.kolesnik.notrealroyale;

import ru.nsu.fit.kolesnik.notrealroyale.networking.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerApplication {

    public static final int PORT = 12000;
    public static final int SERVER_MAX_CLIENTS_NUMBER = 5;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Server server = new Server(serverSocket, SERVER_MAX_CLIENTS_NUMBER);
            server.listenForServerRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
