package ru.nsu.fit.kolesnik.notrealroyale.networking;

import javafx.application.Platform;
import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Chest;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Player;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Client extends Thread {
    private final String clientName;
    private InetAddress serverIP;
    private final int serverPort;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private GameController controller;
    private GameView view;

    public Client(String clientName, String serverIPString, int serverPort) {
        this.clientName = clientName;
        this.serverPort = serverPort;
        try {
            this.serverIP = InetAddress.getByName(serverIPString);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String receivedString = inputStream.readUTF();
                if (receivedString.equals("UPDATE")) {
                    List<Player> players = (List<Player>) inputStream.readObject();
                    List<Chest> сhests = (List<Chest>) inputStream.readObject();
                    Platform.runLater(() -> {
                        view.drawFrame(players, сhests);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectToServer() {
        try {
            socket = new Socket(serverIP, serverPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeUTF("CONNECT " + clientName);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGameController(GameController controller) {
        this.controller = controller;
    }

    public void setGameView(GameView view) {
        this.view = view;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return outputStream;
    }
}
