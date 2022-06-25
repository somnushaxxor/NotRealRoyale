package ru.nsu.fit.kolesnik.notrealroyale;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.notrealroyale.controller.InetGameController;
import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.networking.Client;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;
import ru.nsu.fit.kolesnik.notrealroyale.view.javafx.GraphicGameView;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class ClientApplication extends Application {
    private static String clientUsername;
    private static Client client;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() {
        clientUsername = "Artem" + UUID.randomUUID();
        try {
            Socket socket = new Socket("localhost", 12000);
            client = new Client(socket, clientUsername);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(GraphicGameView.GAME_WINDOW_WIDTH, GraphicGameView.GAME_WINDOW_HEIGHT);
        Group root = new Group(canvas);
        Scene scene = new Scene(root);

        GameController controller = new InetGameController(clientUsername, client.getObjectOutputStream());
        client.setGameController(controller);
        GameView view = new GraphicGameView(clientUsername, controller, canvas, scene);
        client.setGameView(view);

        primaryStage.setOnCloseRequest(event -> client.closeConnectionToServer());
        primaryStage.setTitle("NotRealRoyale");
        primaryStage.setScene(scene);
        primaryStage.show();

        client.receiveMessagesFromServer();
    }
}
