package ru.nsu.fit.kolesnik.notrealroyale.networking;

import javafx.application.Platform;
import ru.nsu.fit.kolesnik.notrealroyale.controller.InetGameController;
import ru.nsu.fit.kolesnik.notrealroyale.exception.ServerIsFullException;
import ru.nsu.fit.kolesnik.notrealroyale.exception.UnavailableUsernameException;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.*;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.WorldMap;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client {
    private Socket socket;
    private String clientUsername;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean isStopCalled;

    private WorldMap worldMap;

    public Client() {
        isStopCalled = false;
        System.out.println("Client created!");
    }

    public void joinGameSession(Socket socket, String clientUsername) throws UnavailableUsernameException, IOException, ServerIsFullException {
        this.socket = socket;
        this.clientUsername = clientUsername;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            closeConnectionToServer();
            throw new IOException();
        }
        String serverStateAnswer = inputStream.readUTF();
        if (serverStateAnswer.equals("OK")) {
            outputStream.writeUTF("JOIN " + clientUsername);
            outputStream.flush();
            String joinRequestAnswer = inputStream.readUTF();
            if (joinRequestAnswer.equals("OK")) {
                this.clientUsername = clientUsername;
                String mapName = inputStream.readUTF();
                worldMap = new WorldMap(mapName);
            } else {
                closeConnectionToServer();
                throw new UnavailableUsernameException("Username " + clientUsername + " is already taken!");
            }
        } else {
            closeConnectionToServer();
            throw new ServerIsFullException("Try to join later!");
        }
    }

    @SuppressWarnings("unchecked")
    public void receiveMessagesFromServer(GameView view) {
        InetGameController controller = new InetGameController(clientUsername, outputStream);
        view.startHandlingUserInput(controller);
        Thread receiver = new Thread(() -> {
            while (!socket.isClosed()) {
                if (isStopCalled) {
                    System.out.println("Client stopped!");
                    closeConnectionToServer();
                    isStopCalled = false;
                    break;
                }
                try {
                    String messageFromServer = inputStream.readUTF();
                    if (messageFromServer.equals("UPDATE")) {
                        List<Player> players = (List<Player>) inputStream.readObject();
                        List<Chest> chests = (List<Chest>) inputStream.readObject();
                        List<Bullet> bullets = (List<Bullet>) inputStream.readObject();
                        List<RevolverBooster> revolverBoosters = (List<RevolverBooster>) inputStream.readObject();
                        List<HealingSalve> healingSalves = (List<HealingSalve>) inputStream.readObject();
                        Platform.runLater(() -> view.drawFrame(worldMap, players, chests, bullets, revolverBoosters, healingSalves));
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    closeConnectionToServer();
                }
            }
        });
        receiver.start();
    }

    public void leaveGameSession() {
        isStopCalled = true;
    }

    private void closeConnectionToServer() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client closed connection to the server!");
    }
}
