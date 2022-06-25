package ru.nsu.fit.kolesnik.notrealroyale.networking;

import javafx.application.Platform;
import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Chest;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Player;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client {
    private final Socket socket;
    private final String clientUsername;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private GameController controller;
    private GameView view;

    public Client(Socket socket, String clientUsername) {
        this.socket = socket;
        this.clientUsername = clientUsername;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeUTF("JOINED " + clientUsername);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void receiveMessagesFromServer() {
        Thread receiver = new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    String messageFromServer = inputStream.readUTF();
                    if (messageFromServer.equals("UPDATE")) {
                        List<Player> players = (List<Player>) inputStream.readObject();
                        List<Chest> chests = (List<Chest>) inputStream.readObject();
                        Platform.runLater(() -> view.drawFrame(players, chests));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        receiver.start();
    }

    public void closeConnectionToServer() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("Client closed!");
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
