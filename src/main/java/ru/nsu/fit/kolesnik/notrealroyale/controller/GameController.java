package ru.nsu.fit.kolesnik.notrealroyale.controller;

import java.util.UUID;

public interface GameController {

    void onKeyPressed(Key key);

    void setPlayerId(UUID playerId);

    void onMouseClicked(double mouseX, double mouseY);
}
