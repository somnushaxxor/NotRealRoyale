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

import java.util.UUID;

public class ClientApplication extends Application {
    private static Client client;
    private static GameController controller;

    String clientName = "Artem" + UUID.randomUUID();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() {
        client = new Client(clientName, "localhost", 12000);
        client.connectToServer();
        controller = new InetGameController(clientName, client.getObjectOutputStream());
        client.setGameController(controller);
        client.start();
    }

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(GraphicGameView.GAME_WINDOW_WIDTH, GraphicGameView.GAME_WINDOW_HEIGHT);
        Group root = new Group(canvas);
        Scene scene = new Scene(root);

        GameView view = new GraphicGameView(clientName, controller, canvas, scene);
        client.setGameView(view);


        primaryStage.setTitle("NotRealRoyale");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
