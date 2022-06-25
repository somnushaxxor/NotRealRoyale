package ru.nsu.fit.kolesnik.notrealroyale.view;

import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Chest;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Player;

import java.util.List;

public interface GameView {
    void drawFrame(List<Player> players, List<Chest> chests);
}
