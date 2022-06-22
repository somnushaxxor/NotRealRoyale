package ru.nsu.fit.kolesnik.notrealroyale.controller;

import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Direction;

import java.util.UUID;

public class DefaultGameController implements GameController {
    private final GameModel gameModel;
    private UUID clientPlayerId;

    public DefaultGameController(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public void onKeyPressed(Key key) {
        switch (key) {
            case W -> gameModel.movePlayerByDirection(Direction.UP, clientPlayerId);
            case S -> gameModel.movePlayerByDirection(Direction.DOWN, clientPlayerId);
            case A -> gameModel.movePlayerByDirection(Direction.LEFT, clientPlayerId);
            case D -> gameModel.movePlayerByDirection(Direction.RIGHT, clientPlayerId);
        }
    }

    @Override
    public void setClientPlayerId(UUID clientPlayerId) {
        this.clientPlayerId = clientPlayerId;
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY) {
        gameModel.shoot(mouseX, mouseY, clientPlayerId);
    }
}
