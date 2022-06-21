package ru.nsu.fit.kolesnik.notrealroyale;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.notrealroyale.controller.DefaultGameController;
import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;
import ru.nsu.fit.kolesnik.notrealroyale.view.javafx.GraphicGameView;

public class GameApplication extends Application {
    static private GameModel entity;
    static private GameView view;
    static private GameController controller;

    public static void main(String[] args) {
        entity = new GameModel();
        controller = new DefaultGameController(entity);

        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root);

        view = new GraphicGameView(entity, controller, root, scene);

        entity.addSubscriber(view);
        entity.start();

        primaryStage.setOnCloseRequest(event -> entity.stop());
        primaryStage.setTitle("NotRealRoyale");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
