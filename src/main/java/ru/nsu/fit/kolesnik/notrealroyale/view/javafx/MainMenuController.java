package ru.nsu.fit.kolesnik.notrealroyale.view.javafx;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nsu.fit.kolesnik.notrealroyale.ClientApplication;
import ru.nsu.fit.kolesnik.notrealroyale.exception.UnavailableUsernameException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static ru.nsu.fit.kolesnik.notrealroyale.ClientApplication.client;

public class MainMenuController {
    private final String UNKNOWN_HOST_EXCEPTION_LABEL_TEXT = "IP adress of the server can not be determined!";
    private final String IO_EXCEPTION_LABEL_TEXT = "Can not connect to server!";
    private final String UNAVAILABLE_USERNAME_EXCEPTION_LABEL_TEXT = "Username is already taken!";
    @FXML
    public TextField usernameTextField;
    @FXML
    public TextField serverIPTextField;
    @FXML
    public Label errorMessageLabel;
    @FXML
    public Button playButton;
    @FXML
    private Button exitButton;

    @FXML
    public void playButtonOnClick() {
        Stage mainMenuStage = (Stage) playButton.getScene().getWindow();
        if (!usernameTextField.getText().isEmpty() && !serverIPTextField.getText().isEmpty()) {
            try {
                InetAddress serverInetAdrress = InetAddress.getByName(serverIPTextField.getText());
                Socket socket = new Socket(serverInetAdrress, ClientApplication.PORT);
                String clientUsername = usernameTextField.getText();
                client.joinGameSession(socket, clientUsername);
                Canvas canvas = new Canvas(GraphicGameView.GAME_WINDOW_WIDTH, GraphicGameView.GAME_WINDOW_HEIGHT);
                Group root = new Group(canvas);
                Scene scene = new Scene(root);
                Stage gameSessionStage = new Stage();
                GraphicGameView view = new GraphicGameView(clientUsername, canvas, scene);
                client.receiveMessagesFromServer(view);
                gameSessionStage.setTitle("NotRealRoyale");
                gameSessionStage.setScene(scene);
                gameSessionStage.setResizable(false);
                gameSessionStage.setOnCloseRequest(event -> {
                    client.leaveGameSession();
                    mainMenuStage.show();
                    gameSessionStage.close();
                });
                gameSessionStage.show();
                mainMenuStage.close();
                errorMessageLabel.setText("");
            } catch (UnknownHostException e) {
                e.printStackTrace();
                errorMessageLabel.setText(UNKNOWN_HOST_EXCEPTION_LABEL_TEXT);
                serverIPTextField.clear();
            } catch (IOException e) {
                e.printStackTrace();
                errorMessageLabel.setText(IO_EXCEPTION_LABEL_TEXT);
                serverIPTextField.clear();
            } catch (UnavailableUsernameException e) {
                e.printStackTrace();
                errorMessageLabel.setText(UNAVAILABLE_USERNAME_EXCEPTION_LABEL_TEXT);
                usernameTextField.clear();
            }
        }
    }

    @FXML
    public void exitButtonOnClick() {
        Stage mainMenuStage = (Stage) exitButton.getScene().getWindow();
        mainMenuStage.close();
    }
}
