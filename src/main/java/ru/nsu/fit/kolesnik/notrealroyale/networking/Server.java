package ru.nsu.fit.kolesnik.notrealroyale.networking;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.thread.ThreadPool;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    private final GameModel model;
    private final ThreadPool threadPool;

    public Server(ServerSocket serverSocket, int maxClientsNumber) {
        this.serverSocket = serverSocket;
        model = new GameModel();
        threadPool = new ThreadPool(maxClientsNumber);
    }

    public void listenForServerRequest() {
        model.start();
        System.out.println("Server waiting for requests...");
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                if (threadPool.hasFreeThread()) {
                    System.out.println("Client accepted!");
                    outputStream.writeUTF("OK");
                    outputStream.flush();
                    ClientHandler clientHandler = new ClientHandler(socket, outputStream, inputStream, model);
                    threadPool.execute(clientHandler);
                } else {
                    outputStream.writeUTF("SERVER IS FULL");
                    outputStream.flush();
                    try {
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                model.stop();
                threadPool.shutdown();
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

