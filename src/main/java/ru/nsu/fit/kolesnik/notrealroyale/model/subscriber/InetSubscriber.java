package ru.nsu.fit.kolesnik.notrealroyale.model.subscriber;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Chest;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Player;
import ru.nsu.fit.kolesnik.notrealroyale.networking.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class InetSubscriber implements Subscriber {
    private final String name;
    private final Server server;
    private final GameModel model;
    private final ObjectOutputStream outputStream;

    public InetSubscriber(String name, Server server, GameModel model, ObjectOutputStream outputStream) {
        this.name = name;
        this.server = server;
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
            outputStream.writeUTF("UPDATE");
            List<Player> players = model.getPlayers();
            outputStream.writeObject(players);
            List<Chest> chests = model.getWorldMap().getChests();
            outputStream.writeObject(chests);
            outputStream.reset();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
