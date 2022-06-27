package ru.nsu.fit.kolesnik.notrealroyale;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.notrealroyale.networking.Client;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;
import ru.nsu.fit.kolesnik.notrealroyale.view.javafx.GraphicGameView;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class ClientApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Socket socket = new Socket("localhost", 12000);
            Client client = new Client(socket);
            String clientUsername = "Artem" + UUID.randomUUID();
            if (client.joinGameSession(clientUsername)) {
                // got map name + the username is valid and not already taken
                Canvas canvas = new Canvas(GraphicGameView.GAME_WINDOW_WIDTH, GraphicGameView.GAME_WINDOW_HEIGHT);
                Group root = new Group(canvas);
                Scene scene = new Scene(root);

                GameView view = new GraphicGameView(clientUsername, canvas, scene);

                primaryStage.setOnCloseRequest(event -> client.stop());
                primaryStage.setTitle("NotRealRoyale");
                primaryStage.setScene(scene);

                client.receiveMessagesFromServer(view);
                primaryStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
