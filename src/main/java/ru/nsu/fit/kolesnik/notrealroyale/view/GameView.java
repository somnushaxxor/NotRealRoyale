package ru.nsu.fit.kolesnik.notrealroyale.view;

import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.*;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.WorldMap;

import java.util.List;

public interface GameView {
    void drawFrame(WorldMap worldMap, List<Player> players, List<Chest> chests, List<Bullet> bullets, List<RevolverBooster> revolverBoosters, List<HealingSalve> healingSalves);

    void startHandlingUserInput(GameController controller);
}
