package ru.nsu.fit.kolesnik.notrealroyale;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.networking.Server;

public class ServerApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() {
        GameModel model = new GameModel();
        Server server = new Server(12000, model);
        server.start();
    }

    @Override
    public void start(Stage primaryStage) {
        Text text = new Text(10, 10, "SERVER");
        Group root = new Group(text);
        Scene scene = new Scene(root, 200, 200);

        primaryStage.setTitle("NotRealRoyale");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
