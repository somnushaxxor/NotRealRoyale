package ru.nsu.fit.kolesnik.notrealroyale.networking;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Direction;
import ru.nsu.fit.kolesnik.notrealroyale.model.subscriber.InetSubscriber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final GameModel model;
    private InetSubscriber inetSubscriber;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientHandler(Socket socket, GameModel model) {
        this.socket = socket;
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
        while (!socket.isClosed()) {
            try {
                String messageFromClient = inputStream.readUTF();
                String[] messageFromClientSplitted = messageFromClient.split(" ");
                if (messageFromClientSplitted[0].equals("JOINED")) {
                    String clientUsername = messageFromClientSplitted[1];
                    inetSubscriber = new InetSubscriber(clientUsername, model, outputStream);
                    model.addSubscriber(inetSubscriber);
                } else if (messageFromClientSplitted[0].equals("MOVED")) {
                    String clientUsername = messageFromClientSplitted[2];
                    switch (messageFromClientSplitted[1]) {
                        case "UP" -> model.movePlayerByDirection(Direction.UP, clientUsername);
                        case "DOWN" -> model.movePlayerByDirection(Direction.DOWN, clientUsername);
                        case "LEFT" -> model.movePlayerByDirection(Direction.LEFT, clientUsername);
                        case "RIGHT" -> model.movePlayerByDirection(Direction.RIGHT, clientUsername);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                closeConnectionToClient();
            }
        }
        model.removeSubscriber(inetSubscriber);
    }

    private void closeConnectionToClient() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
