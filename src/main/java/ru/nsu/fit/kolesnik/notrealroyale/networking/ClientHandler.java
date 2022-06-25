package ru.nsu.fit.kolesnik.notrealroyale.networking;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Direction;
import ru.nsu.fit.kolesnik.notrealroyale.model.subscriber.InetSubscriber;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Server server;
    private final GameModel model;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientHandler(Socket socket, Server server, GameModel model) {
        this.server = server;
        this.model = model;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String receivedString = inputStream.readUTF();
                String[] receivedStringSplitted = receivedString.split(" ");
                if (receivedStringSplitted[0].equals("CONNECT")) {
                    String clientName = receivedStringSplitted[1];
                    InetSubscriber subscriber = new InetSubscriber(clientName, server, model, outputStream);
                    model.addSubscriber(subscriber);
                } else if (receivedStringSplitted[0].equals("MOVED")) {
                    String clientName = receivedStringSplitted[2];
                    switch (receivedStringSplitted[1]) {
                        case "UP" -> model.movePlayerByDirection(Direction.UP, clientName);
                        case "DOWN" -> model.movePlayerByDirection(Direction.DOWN, clientName);
                        case "LEFT" -> model.movePlayerByDirection(Direction.LEFT, clientName);
                        case "RIGHT" -> model.movePlayerByDirection(Direction.RIGHT, clientName);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
