package ru.nsu.fit.kolesnik.notrealroyale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.notrealroyale.networking.Client;

import java.io.IOException;
import java.net.URL;

public class ClientApplication extends Application {
    public static final int PORT = 12000;
    public static Client client;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() {
        client = new Client();
    }

    @Override
    public void start(Stage mainMenuStage) {
        try {
            URL mainMenuURL = getClass().getResource("/fxmls/main-menu.fxml");
            if (mainMenuURL != null) {
                Parent root = FXMLLoader.load(mainMenuURL);
                Scene scene = new Scene(root);
                mainMenuStage.setTitle("NotRealRoyale");
                mainMenuStage.getIcons().add(new Image("images/icon.png"));
                mainMenuStage.setResizable(false);
                mainMenuStage.setScene(scene);
                mainMenuStage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
