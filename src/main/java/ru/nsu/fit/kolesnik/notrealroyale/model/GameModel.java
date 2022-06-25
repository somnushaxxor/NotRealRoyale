package ru.nsu.fit.kolesnik.notrealroyale.model;

import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.*;
import ru.nsu.fit.kolesnik.notrealroyale.model.subscriber.Subscriber;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.WorldMap;

import java.util.*;

public class GameModel {
    private final static String MAP_NAME = "default";

    private final List<Subscriber> subscribers;
    private final Timer timer;

    private final WorldMap worldMap;
    private final List<Player> players;
    private final List<Bullet> bullets;

    public GameModel() {
        subscribers = new ArrayList<>();
        timer = new Timer();
        worldMap = new WorldMap();
        worldMap.loadMap(MAP_NAME);
        bullets = new ArrayList<>();
        players = new ArrayList<>();
    }

    public synchronized void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
        //Player newPlayer = new Player(subscriber.getName(), 1 + Math.random() * (worldMap.getWidth() - 2), 1 + Math.random() * (worldMap.getHeight() - 2));
        Player newPlayer = new Player(subscriber.getName(), 51, 51);
        players.add(newPlayer);
    }

    public synchronized void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
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
        for (Bullet bullet : bullets) {
            bullet.update();
            boolean isBulletCollided = false;
            int bulletOccupiedTileX1 = (int) Math.floor(bullet.getX() + bullet.getCollidableRectPaddingX());
            int bulletOccupiedTileX2 = (int) Math.floor(bullet.getX() + 1 - bullet.getCollidableRectPaddingX());
            int bulletOccupiedTileY1 = (int) Math.floor(bullet.getY() + bullet.getCollidableRectPaddingY());
            int bulletOccupiedTileY2 = (int) Math.floor(bullet.getY() + 1 - bullet.getCollidableRectPaddingY());
            if (worldMap.getTile(bulletOccupiedTileX1, bulletOccupiedTileY1).isCollidable() || worldMap.getTile(bulletOccupiedTileX1, bulletOccupiedTileY2).isCollidable() || worldMap.getTile(bulletOccupiedTileX2, bulletOccupiedTileY1).isCollidable() || worldMap.getTile(bulletOccupiedTileX2, bulletOccupiedTileY2).isCollidable()) {
                isBulletCollided = true;
            } else {
                for (Chest chest : worldMap.getChests()) {
                    if (bullet.isColliding(chest)) {
                        chest.receiveDamage(bullet.getDamage());
                        if (!chest.isAlive()) {
                            double randomNumber = Math.random();
                            if (randomNumber < 0.25) {
                                RevolverBooster revolverBooster = new RevolverBooster(chest.getX(), chest.getY());
                                worldMap.getBoosters().add(revolverBooster);
                            } else if (randomNumber < 0.5) {
                                HealingSalve healingSalve = new HealingSalve(chest.getX(), chest.getY());
                                worldMap.getHealingSalves().add(healingSalve);
                            }
                        }
                        isBulletCollided = true;
                        break;
                    }
                }
            }
            if (isBulletCollided) {
                bullet.setAlive(false);
            }
        }
        bullets.removeIf(bullet -> (!bullet.isAlive()));
        worldMap.getChests().removeIf(chest -> (!chest.isAlive()));
        notifyAllSubscribers();
    }

    public void stop() {
        timer.cancel();
    }

    private void notifySubscriber(Subscriber subscriber) {
        subscriber.update();
    }

    private void notifyAllSubscribers() {
        for (Subscriber subscriber : subscribers) {
            notifySubscriber(subscriber);
        }
    }

    public synchronized void movePlayerByDirection(Direction direction, String playerName) {
        Player player = getPlayerByName(playerName);

        boolean isMoveAvailable = true;

        if (direction == Direction.UP) {
            int playerTopTilesY = (int) (Math.floor(player.getY() - player.getVelocity() + player.getCollidableRectPaddingY()));
            int playerOccupiedTile1 = (int) Math.floor(player.getX() + player.getCollidableRectPaddingX());
            int playerOccupiedTile2 = (int) Math.floor(player.getX() + 1 - player.getCollidableRectPaddingX());
            if (worldMap.getTile(playerOccupiedTile1, playerTopTilesY).isCollidable() || worldMap.getTile(playerOccupiedTile2, playerTopTilesY).isCollidable()) {
                isMoveAvailable = false;
            } else {
                for (Chest chest : worldMap.getChests()) {
                    if ((chest.getX() == playerOccupiedTile1 || chest.getX() == playerOccupiedTile2) && chest.getY() == playerTopTilesY) {
                        isMoveAvailable = false;
                        break;
                    }
                }
            }
        } else if (direction == Direction.DOWN) {
            int playerBottomTilesY = (int) (Math.floor(player.getY() + 1 + player.getVelocity() - player.getCollidableRectPaddingY()));
            int playerOccupiedTile1 = (int) Math.floor(player.getX() + player.getCollidableRectPaddingX());
            int playerOccupiedTile2 = (int) Math.floor(player.getX() + 1 - player.getCollidableRectPaddingX());
            if (worldMap.getTile(playerOccupiedTile1, playerBottomTilesY).isCollidable() || worldMap.getTile(playerOccupiedTile2, playerBottomTilesY).isCollidable()) {
                isMoveAvailable = false;
            } else {
                for (Chest chest : worldMap.getChests()) {
                    if ((chest.getX() == playerOccupiedTile1 || chest.getX() == playerOccupiedTile2) && chest.getY() == playerBottomTilesY) {
                        isMoveAvailable = false;
                        break;
                    }
                }
            }
        } else if (direction == Direction.LEFT) {
            int playerLeftTilesX = (int) (Math.floor(player.getX() - player.getVelocity() + player.getCollidableRectPaddingX()));
            int playerOccupiedTile1 = (int) Math.floor(player.getY() + player.getCollidableRectPaddingY());
            int playerOccupiedTile2 = (int) Math.floor(player.getY() + 1 - player.getCollidableRectPaddingY());
            if (worldMap.getTile(playerLeftTilesX, playerOccupiedTile1).isCollidable() || worldMap.getTile(playerLeftTilesX, playerOccupiedTile2).isCollidable()) {
                isMoveAvailable = false;
            } else {
                for (Chest chest : worldMap.getChests()) {
                    if (chest.getX() == playerLeftTilesX && (chest.getY() == playerOccupiedTile1 || chest.getY() == playerOccupiedTile2)) {
                        isMoveAvailable = false;
                        break;
                    }
                }
            }
        } else if (direction == Direction.RIGHT) {
            int playerRightTilesX = (int) (Math.floor(player.getX() + 1 + player.getVelocity() - player.getCollidableRectPaddingX()));
            int playerOccupiedTile1 = (int) Math.floor(player.getY() + player.getCollidableRectPaddingY());
            int playerOccupiedTile2 = (int) Math.floor(player.getY() + 1 - player.getCollidableRectPaddingY());
            if (worldMap.getTile(playerRightTilesX, playerOccupiedTile1).isCollidable() || worldMap.getTile(playerRightTilesX, playerOccupiedTile2).isCollidable()) {
                isMoveAvailable = false;
            } else {
                for (Chest chest : worldMap.getChests()) {
                    if (chest.getX() == playerRightTilesX && (chest.getY() == playerOccupiedTile1 || chest.getY() == playerOccupiedTile2)) {
                        isMoveAvailable = false;
                        break;
                    }
                }
            }
        }

        if (isMoveAvailable) {
            player.moveByDirection(direction);
        }

        RevolverBooster revolverBoosterPickedUp = null;
        for (RevolverBooster revolverBooster : worldMap.getBoosters()) {
            if (player.isColliding(revolverBooster)) {
                revolverBoosterPickedUp = revolverBooster;
                player.powerUpRevolver();
                break;
            }
        }
        worldMap.getBoosters().remove(revolverBoosterPickedUp);
        HealingSalve healingSalvePickedUp = null;
        for (HealingSalve healingSalve : worldMap.getHealingSalves()) {
            if (player.isColliding(healingSalve)) {
                healingSalvePickedUp = healingSalve;
                player.pickHealingSalveUp();
                break;
            }
        }
        worldMap.getHealingSalves().remove(healingSalvePickedUp);
    }

    public synchronized void shoot(double mouseX, double mouseY, String playerName) {
        Player player = getPlayerByName(playerName);
        double length = Math.sqrt(mouseX * mouseX + mouseY * mouseY);
        double vectorX = mouseX / length;
        double vectorY = mouseY / length;
        Bullet newBullet = new Bullet(player.getX(), player.getY(), vectorX, vectorY, player);
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

    private Player getPlayerByName(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }
}
