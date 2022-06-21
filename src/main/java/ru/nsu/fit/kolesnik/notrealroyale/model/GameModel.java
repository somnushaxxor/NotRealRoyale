package ru.nsu.fit.kolesnik.notrealroyale.model;

import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.*;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.WorldMap;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.util.*;

public class GameModel {
    private final static String DEFAULT_MAP_NAME = "default.map";

    private final List<GameView> subscribers;
    private final Timer timer;

    private final WorldMap worldMap;
    private final List<Player> players;
    private final List<Bullet> bullets;

    public GameModel() {
        subscribers = new ArrayList<>();
        timer = new Timer();
        worldMap = new WorldMap();
        worldMap.loadMap(DEFAULT_MAP_NAME);
        bullets = new ArrayList<>();
        players = new ArrayList<>();
    }

    public void addSubscriber(GameView view) {
        subscribers.add(view);
        Player newPlayer = new Player("JohnSmith", 51, 51);
        players.add(newPlayer);
        notifySubscriber(view, "PLAYER_ADDED " + newPlayer.getId().toString()); // connected???
    }

    public void start() {
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };
        timer.scheduleAtFixedRate(updateTask, 0, 20);
    }

    private void update() {
        notifyAllSubscribers("GAME_UPDATED");
    }

    public void stop() {
        timer.cancel();
    }

    private void notifySubscriber(GameView view, String eventString) {
        view.update(eventString);
    }

    private void notifyAllSubscribers(String eventString) {
        for (GameView subscriber : subscribers) {
            notifySubscriber(subscriber, eventString);
        }
    }

    public void movePlayerByDirection(Direction direction, UUID playerId) {
        Player player = getPlayerById(playerId);

        // map borders collision
//        if (direction == Direction.UP) {
//            if (0 > player.getY() - playersVelocity) {
//                steps = player.getY();
//            }
//        } else if (direction == Direction.DOWN) {
//            if (worldMap.getHeight() < player.getY() + player.getHeight() + playersVelocity) {
//                steps = worldMap.getHeight() - player.getY() - player.getHeight();
//            }
//        } else if (direction == Direction.LEFT) {
//            if (0 > player.getX() - playersVelocity) {
//                steps = player.getX();
//            }
//        } else if (direction == Direction.RIGHT) {
//            if (worldMap.getWidth() < player.getX() + player.getWidth() + playersVelocity) {
//                steps = worldMap.getWidth() - player.getX() - player.getWidth();
//            }
//        }

        boolean availableMove = true;

        if (direction == Direction.UP) {
            int playerTopTilesY = (int) (Math.floor(player.getY() - player.getVelocity() + player.getCollidableRectPaddingY()));
            int playerOccupiedTile1 = (int) Math.floor(player.getX() + player.getCollidableRectPaddingX());
            int playerOccupiedTile2 = (int) Math.floor(player.getX() + 1 - player.getCollidableRectPaddingX());
            if (worldMap.getTile(playerOccupiedTile1, playerTopTilesY).isCollidable() || worldMap.getTile(playerOccupiedTile2, playerTopTilesY).isCollidable()) {
                availableMove = false;
            }
        } else if (direction == Direction.DOWN) {
            int playerBottomTilesY = (int) (Math.floor(player.getY() + 1 + player.getVelocity() - player.getCollidableRectPaddingY()));
            int playerOccupiedTile1 = (int) Math.floor(player.getX() + player.getCollidableRectPaddingX());
            int playerOccupiedTile2 = (int) Math.floor(player.getX() + 1 - player.getCollidableRectPaddingX());
            if (worldMap.getTile(playerOccupiedTile1, playerBottomTilesY).isCollidable() || worldMap.getTile(playerOccupiedTile2, playerBottomTilesY).isCollidable()) {
                availableMove = false;
            }
        } else if (direction == Direction.LEFT) {
            int playerLeftTilesX = (int) (Math.floor(player.getX() - player.getVelocity() + player.getCollidableRectPaddingX()));
            int playerOccupiedTile1 = (int) Math.floor(player.getY() + player.getCollidableRectPaddingY());
            int playerOccupiedTile2 = (int) Math.floor(player.getY() + 1 - player.getCollidableRectPaddingY());
            if (worldMap.getTile(playerLeftTilesX, playerOccupiedTile1).isCollidable() || worldMap.getTile(playerLeftTilesX, playerOccupiedTile2).isCollidable()) {
                availableMove = false;
            }
        } else if (direction == Direction.RIGHT) {
            int playerRightTilesX = (int) (Math.floor(player.getX() + 1 + player.getVelocity() - player.getCollidableRectPaddingX()));
            int playerOccupiedTile1 = (int) Math.floor(player.getY() + player.getCollidableRectPaddingY());
            int playerOccupiedTile2 = (int) Math.floor(player.getY() + 1 - player.getCollidableRectPaddingY());
            if (worldMap.getTile(playerRightTilesX, playerOccupiedTile1).isCollidable() || worldMap.getTile(playerRightTilesX, playerOccupiedTile2).isCollidable()) {
                availableMove = false;
            }
        }
        if (availableMove) {
            player.moveByDirection(direction);
        }
    }

    public void shoot(double mouseX, double mouseY, UUID playerId) {
        Player player = getPlayerById(playerId);
        double length = Math.sqrt(mouseX * mouseX + mouseY * mouseY);
        double vectorX = mouseX / length;
        double vectorY = mouseY / length;
        Bullet newBullet = new Bullet(player.getX(), player.getY(), vectorX, vectorY, player.getPistolDamage(), player);
        bullets.add(newBullet);
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayerById(UUID id) {
        for (Player player : players) {
            if (player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }
}
