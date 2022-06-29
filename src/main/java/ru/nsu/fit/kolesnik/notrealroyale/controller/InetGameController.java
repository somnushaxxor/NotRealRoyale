package ru.nsu.fit.kolesnik.notrealroyale.controller;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class InetGameController implements GameController {
    private final String clientUsername;
    private final ObjectOutputStream outputStream;

    public InetGameController(String clientUsername, ObjectOutputStream outputStream) {
        this.clientUsername = clientUsername;
        this.outputStream = outputStream;
    }

    @Override
    public synchronized void onKeyPressed(Key key) {
        try {
            switch (key) {
                case W -> outputStream.writeUTF("MOVED UP " + clientUsername);
                case S -> outputStream.writeUTF("MOVED DOWN " + clientUsername);
                case A -> outputStream.writeUTF("MOVED LEFT " + clientUsername);
                case D -> outputStream.writeUTF("MOVED RIGHT " + clientUsername);
                case E -> outputStream.writeUTF("HEAL " + clientUsername);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onMouseClicked(double mouseX, double mouseY) {
        try {
            outputStream.writeUTF("CLICKED " + mouseX + " " + mouseY + " " + clientUsername);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
