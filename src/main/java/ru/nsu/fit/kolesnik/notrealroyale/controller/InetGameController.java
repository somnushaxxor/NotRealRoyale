package ru.nsu.fit.kolesnik.notrealroyale.controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InetGameController implements GameController {
    private final String clientName;
    private final ObjectOutputStream outputStream;
    private final ExecutorService executorService;

    public InetGameController(String clientName, ObjectOutputStream outputStream) {
        this.clientName = clientName;
        this.outputStream = outputStream;
        executorService = Executors.newFixedThreadPool(1);
    }

    @Override
    public void onKeyPressed(Key key) {
        Runnable runnable = () -> {
            try {
                switch (key) {
                    case W -> outputStream.writeUTF("MOVED UP " + clientName);
                    case S -> outputStream.writeUTF("MOVED DOWN " + clientName);
                    case A -> outputStream.writeUTF("MOVED LEFT " + clientName);
                    case D -> outputStream.writeUTF("MOVED RIGHT " + clientName);
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        executorService.execute(runnable);
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY) {
        //outputStream.writeUTF("CLICKED " + mouseX + " " + mouseY + " " + clientName);
    }
}
