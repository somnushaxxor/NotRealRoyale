package ru.nsu.fit.kolesnik.notrealroyale.networking;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Direction;
import ru.nsu.fit.kolesnik.notrealroyale.model.subscriber.InetSubscriber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final GameModel model;
    private InetSubscriber inetSubscriber;

    public ClientHandler(Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream, GameModel model) {
        this.socket = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.model = model;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                String messageFromClient = inputStream.readUTF();
                String[] messageFromClientSplitted = messageFromClient.split(" ");
                if (messageFromClientSplitted[0].equals("JOIN")) {
                    String clientUsername = messageFromClientSplitted[1];
                    boolean clientUsernameIsAlreadyInUse = false;
                    synchronized (model) {
                        if (!model.hasSubscriberNamed(clientUsername)) {
                            inetSubscriber = new InetSubscriber(clientUsername, model, outputStream);
                            model.addSubscriber(inetSubscriber);
                        } else {
                            clientUsernameIsAlreadyInUse = true;
                        }
                    }
                    if (!clientUsernameIsAlreadyInUse) {
                        synchronized (outputStream) {
                            outputStream.writeUTF("OK");
                            outputStream.writeUTF(model.getWorldMap().getMapName());
                            outputStream.flush();
                        }
                    } else {
                        outputStream.writeUTF("NAME ALREADY IN USE");
                        outputStream.flush();
                    }
                } else if (messageFromClientSplitted[0].equals("MOVED")) {
                    String clientUsername = messageFromClientSplitted[2];
                    switch (messageFromClientSplitted[1]) {
                        case "UP" -> model.movePlayerByDirection(Direction.UP, clientUsername);
                        case "DOWN" -> model.movePlayerByDirection(Direction.DOWN, clientUsername);
                        case "LEFT" -> model.movePlayerByDirection(Direction.LEFT, clientUsername);
                        case "RIGHT" -> model.movePlayerByDirection(Direction.RIGHT, clientUsername);
                    }
                } else if (messageFromClientSplitted[0].equals("CLICKED")) {
                    double mouseX = Double.parseDouble(messageFromClientSplitted[1]);
                    double mouseY = Double.parseDouble(messageFromClientSplitted[2]);
                    String clientUsername = messageFromClientSplitted[3];
                    model.shoot(mouseX, mouseY, clientUsername);
                } else if (messageFromClientSplitted[0].equals("HEAL")) {
                    String clientUsername = messageFromClientSplitted[1];
                    model.healPlayer(clientUsername);
                }
            } catch (IOException e) {
                e.printStackTrace();
                stopHandlingClient();
            }
        }
        model.removeSubscriber(inetSubscriber);
    }

    private void stopHandlingClient() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
