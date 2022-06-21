package ru.nsu.fit.kolesnik.notrealroyale.controller;

import java.util.UUID;

public interface GameController {

    void onKeyPressed(Key key);

    void setClientPlayerId(UUID clientPlayerId);

    void onMouseClicked(double mouseX, double mouseY);
}
