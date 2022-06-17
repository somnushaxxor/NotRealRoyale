package ru.nsu.fit.kolesnik.notrealroyale.model;


import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Bullet;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Direction;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Player;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.util.List;
import java.util.UUID;

public interface GameModel {

    void addSubscriber(GameView view);

    void start();

    void stop();

    Player getPlayerById(UUID id);

    void movePlayerByDirection(Direction direction, UUID playerId);

    void shoot(double mouseX, double mouseY, UUID playerId);

    WorldMap getWorldMap();

    List<Bullet> getBullets();

    List<Player> getPlayers();
}
