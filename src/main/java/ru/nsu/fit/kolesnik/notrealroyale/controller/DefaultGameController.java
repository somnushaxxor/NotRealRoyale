package ru.nsu.fit.kolesnik.notrealroyale.controller;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Direction;

import java.util.UUID;

public class DefaultGameController implements GameController {
    private final GameModel entity;
    private UUID playerId;

    public DefaultGameController(GameModel entity) {
        this.entity = entity;
    }

    @Override
    public void onKeyPressed(Key key) {
        switch (key) {
            case W -> entity.movePlayerByDirection(Direction.UP, playerId);
            case S -> entity.movePlayerByDirection(Direction.DOWN, playerId);
            case A -> entity.movePlayerByDirection(Direction.LEFT, playerId);
            case D -> entity.movePlayerByDirection(Direction.RIGHT, playerId);
        }
    }

    @Override
    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY) {
        entity.shoot(mouseX, mouseY, playerId);
    }
}
