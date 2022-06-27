package ru.nsu.fit.kolesnik.notrealroyale.model;

import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.*;
import ru.nsu.fit.kolesnik.notrealroyale.model.subscriber.Subscriber;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.WorldMap;

import java.util.*;

public class GameModel {
    private final static String MAP_NAME = "default";

    private final HashMap<String, Subscriber> subscribers;
    private final Timer timer;

    private final WorldMap worldMap;
    private final List<Player> players;
    private final List<Bullet> bullets;
    private final List<Chest> chests;
    private final List<RevolverBooster> revolverBoosters;
    private final List<HealingSalve> healingSalves;

    public GameModel() {
        subscribers = new HashMap<>();
        timer = new Timer();
        worldMap = new WorldMap(MAP_NAME);
        chests = worldMap.placeChests();
        players = new ArrayList<>();
        bullets = new ArrayList<>();
        revolverBoosters = new ArrayList<>();
        healingSalves = new ArrayList<>();
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.put(subscriber.getName(), subscriber);
        //Player newPlayer = new Player(subscriber.getName(), 1 + Math.random() * (worldMap.getWidth() - 2), 1 + Math.random() * (worldMap.getHeight() - 2));
        Player newPlayer = new Player(subscriber.getName(), 51, 51);
        players.add(newPlayer);
    }

    public boolean hasSubscriberNamed(String subscriberName) {
        return subscribers.containsKey(subscriberName);
    }

    public synchronized void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber.getName());
    }

    public synchronized void start() {
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };
        timer.scheduleAtFixedRate(updateTask, 0, 20);
    }

    private synchronized void update() {
        for (Bullet bullet : bullets) {
            bullet.update();
            for (Player player : players) {
                if (bullet.isColliding(player) && !bullet.getPlayer().equals(player)) {
                    player.receiveDamage(bullet.getDamage());
                    if (!player.isAlive()) {
                        bullet.getPlayer().score();
                        players.remove(player);
                    }
                    bullet.setAlive(false);
                }
            }
            if (!bullet.isAlive()) {
                continue;
            }
            int bulletOccupiedTileX1 = (int) Math.floor(bullet.getX() + bullet.getCollidableRectPaddingX());
            int bulletOccupiedTileX2 = (int) Math.floor(bullet.getX() + 1 - bullet.getCollidableRectPaddingX());
            int bulletOccupiedTileY1 = (int) Math.floor(bullet.getY() + bullet.getCollidableRectPaddingY());
            int bulletOccupiedTileY2 = (int) Math.floor(bullet.getY() + 1 - bullet.getCollidableRectPaddingY());
            if (worldMap.getTile(bulletOccupiedTileX1, bulletOccupiedTileY1).isCollidable() || worldMap.getTile(bulletOccupiedTileX1, bulletOccupiedTileY2).isCollidable() || worldMap.getTile(bulletOccupiedTileX2, bulletOccupiedTileY1).isCollidable() || worldMap.getTile(bulletOccupiedTileX2, bulletOccupiedTileY2).isCollidable()) {
                bullet.setAlive(false);
            } else {
                for (Chest chest : chests) {
                    if (bullet.isColliding(chest)) {
                        chest.receiveDamage(bullet.getDamage());
                        if (!chest.isAlive()) {
                            double randomNumber = Math.random();
                            if (randomNumber < 0.25) {
                                RevolverBooster revolverBooster = new RevolverBooster(chest.getX(), chest.getY());
                                revolverBoosters.add(revolverBooster);
                            } else if (randomNumber < 0.5) {
                                HealingSalve healingSalve = new HealingSalve(chest.getX(), chest.getY());
                                healingSalves.add(healingSalve);
                            }
                        }
                        bullet.setAlive(false);
                        break;
                    }
                }
            }
        }
        bullets.removeIf(bullet -> (!bullet.isAlive()));
        chests.removeIf(chest -> (!chest.isAlive()));
        notifyAllSubscribers();
    }

    public synchronized void stop() {
        timer.cancel();
    }

    private void notifySubscriber(Subscriber subscriber) {
        subscriber.update();
    }

    private synchronized void notifyAllSubscribers() {
        for (Map.Entry<String, Subscriber> entry : subscribers.entrySet()) {
            notifySubscriber(entry.getValue());
        }
    }

    public synchronized void movePlayerByDirection(Direction direction, String playerName) {
        Player player = getPlayerByName(playerName);
        if (player == null) {
            return;
        }

        boolean isMoveAvailable = true;

        if (direction == Direction.UP) {
            int playerTopTilesY = (int) (Math.floor(player.getY() - player.getVelocity() + player.getCollidableRectPaddingY()));
            int playerOccupiedTile1 = (int) Math.floor(player.getX() + player.getCollidableRectPaddingX());
            int playerOccupiedTile2 = (int) Math.floor(player.getX() + 1 - player.getCollidableRectPaddingX());
            if (worldMap.getTile(playerOccupiedTile1, playerTopTilesY).isCollidable() || worldMap.getTile(playerOccupiedTile2, playerTopTilesY).isCollidable()) {
                isMoveAvailable = false;
            } else {
                for (Chest chest : chests) {
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
                for (Chest chest : chests) {
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
                for (Chest chest : chests) {
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
                for (Chest chest : chests) {
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
        for (RevolverBooster revolverBooster : revolverBoosters) {
            if (player.isColliding(revolverBooster)) {
                revolverBoosterPickedUp = revolverBooster;
                player.powerUpRevolver();
                break;
            }
        }
        revolverBoosters.remove(revolverBoosterPickedUp);
        HealingSalve healingSalvePickedUp = null;
        for (HealingSalve healingSalve : healingSalves) {
            if (player.isColliding(healingSalve)) {
                healingSalvePickedUp = healingSalve;
                player.pickHealingSalveUp();
                break;
            }
        }
        healingSalves.remove(healingSalvePickedUp);
    }

    public synchronized void shoot(double mouseX, double mouseY, String playerName) {
        Player player = getPlayerByName(playerName);
        if (player != null) {
            double length = Math.sqrt(mouseX * mouseX + mouseY * mouseY);
            double vectorX = mouseX / length;
            double vectorY = mouseY / length;
            Bullet newBullet = new Bullet(player.getX(), player.getY(), vectorX, vectorY, player);
            bullets.add(newBullet);
        }
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public synchronized List<Player> getPlayers() {
        return players;
    }

    public synchronized List<Chest> getChests() {
        return chests;
    }

    public synchronized List<Bullet> getBullets() {
        return bullets;
    }

    public synchronized List<RevolverBooster> getRevolverBoosters() {
        return revolverBoosters;
    }

    public synchronized List<HealingSalve> getHealingSalves() {
        return healingSalves;
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
