package ru.nsu.fit.kolesnik.notrealroyale.model.subscriber;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class InetSubscriber implements Subscriber {

    private final String name;
    private final GameModel model;
    private final ObjectOutputStream outputStream;

    public InetSubscriber(String name, GameModel model, ObjectOutputStream outputStream) {
        this.name = name;
        this.model = model;
        this.outputStream = outputStream;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update() {
        try {
            synchronized (outputStream) {
                outputStream.writeUTF("UPDATE");
                outputStream.writeObject(model.getPlayers());
                outputStream.writeObject(model.getChests());
                outputStream.writeObject(model.getBullets());
                outputStream.writeObject(model.getRevolverBoosters());
                outputStream.writeObject(model.getHealingSalves());
                outputStream.flush();
                outputStream.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
