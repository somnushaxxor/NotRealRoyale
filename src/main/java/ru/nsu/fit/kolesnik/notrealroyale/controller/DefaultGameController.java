package ru.nsu.fit.kolesnik.notrealroyale.controller;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Direction;

import java.util.UUID;

public class DefaultGameController implements GameController {
    private final GameModel entity;
    private UUID clientPlayerId;

    public DefaultGameController(GameModel entity) {
        this.entity = entity;
    }

    @Override
    public void onKeyPressed(Key key) {
        switch (key) {
            case W -> entity.movePlayerByDirection(Direction.UP, clientPlayerId);
            case S -> entity.movePlayerByDirection(Direction.DOWN, clientPlayerId);
            case A -> entity.movePlayerByDirection(Direction.LEFT, clientPlayerId);
            case D -> entity.movePlayerByDirection(Direction.RIGHT, clientPlayerId);
        }
    }

    @Override
    public void setClientPlayerId(UUID clientPlayerId) {
        this.clientPlayerId = clientPlayerId;
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY) {
        entity.shoot(mouseX, mouseY, clientPlayerId);
    }
}
