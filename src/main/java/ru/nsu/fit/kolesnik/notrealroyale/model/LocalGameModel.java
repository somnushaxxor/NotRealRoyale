package ru.nsu.fit.kolesnik.notrealroyale.model;

import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.*;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.util.*;

public class LocalGameModel implements GameModel {
    private final static String DEFAULT_MAP_NAME = "default.map";

    private final List<GameView> subscribers;
    private final Timer timer;

    private final WorldMap worldMap;
    private final List<Player> players;
    private final List<Bullet> bullets;

    public LocalGameModel() {
        subscribers = new ArrayList<>();
        timer = new Timer();
        worldMap = new WorldMap();
        worldMap.loadMap(DEFAULT_MAP_NAME);
        bullets = new ArrayList<>();
        players = new ArrayList<>();
    }

    public void addSubscriber(GameView view) {
        subscribers.add(view);
        Player newPlayer = new Player("JohnSmith", 26, 26);
        players.add(newPlayer);
        notifySubscriber(view, "PLAYER_ADDED " + newPlayer.getId().toString()); // connected???
    }

    @Override
    public void start() {
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };
        timer.scheduleAtFixedRate(updateTask, 0, 80);
    }

    private void update() {
        for (Bullet bullet : bullets) {
            bullet.update();
            if (bullet.isAlive()) {
                for (Wall wall : worldMap.getWalls()) {
                    if (bullet.isColliding(wall)) {
                        bullet.setAlive(false);
                    }
                }
            }
            if (bullet.isAlive()) {
                for (Chest chest : worldMap.getChests()) {
                    if (bullet.isColliding(chest)) {
                        chest.receiveDamage(bullet.getDamage());
                        bullet.setAlive(false);
                    }
                }
            }
        }

        bullets.removeIf(bullet -> (!bullet.isAlive()));
        worldMap.getChests().removeIf(chest -> (!chest.isAlive()));

//        StringBuilder eventStringBuilder = new StringBuilder();
//        eventStringBuilder.append("GAME_UPDATED").append(" ");
//        for (Player player : players) {
//            eventStringBuilder.append("PLAYER").append(" ");
//            eventStringBuilder.append(player.getId()).append(" ");
//            eventStringBuilder.append(player.getX()).append(" ");
//            eventStringBuilder.append(player.getY()).append(" ");
//            eventStringBuilder.append(player.getName()).append(" ");
//            eventStringBuilder.append(player.getWidth()).append(" ");
//            eventStringBuilder.append(player.getHeight()).append(" ");
//            eventStringBuilder.append(player.getHp()).append(" ");
//            eventStringBuilder.append("END").append(" ");
//        }
//        for (GameObject wall : map.getWalls()) {
//            eventStringBuilder.append("WALL").append(" ");
//            eventStringBuilder.append(wall.getX()).append(" ");
//            eventStringBuilder.append(wall.getY()).append(" ");
//            eventStringBuilder.append("END").append(" ");
//        }
//        for (Chest chest : map.getChests()) {
//            eventStringBuilder.append("CHEST").append(" ");
//            eventStringBuilder.append(chest.getX()).append(" ");
//            eventStringBuilder.append(chest.getY()).append(" ");
//            eventStringBuilder.append(chest.getWidth()).append(" ");
//            eventStringBuilder.append(chest.getHp()).append(" ");
//            eventStringBuilder.append(chest.isDamaged()).append(" ");
//            eventStringBuilder.append("END").append(" ");
//        }
//        for (Bullet bullet : bullets) {
//            eventStringBuilder.append("BULLET").append(" ");
//            eventStringBuilder.append(bullet.getX()).append(" ");
//            eventStringBuilder.append(bullet.getY()).append(" ");
//            eventStringBuilder.append("END").append(" ");
//        }
//        eventStringBuilder.append("FINISH");
//        notifyAllSubscribers(eventStringBuilder.toString());

        notifyAllSubscribers("GAME_UPDATED");
    }

    @Override
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
        double playersVelocity = player.getVelocity();
        double steps = playersVelocity;

        // map borders collision
        if (direction == Direction.UP) {
            if (0 > player.getY() - playersVelocity) {
                steps = player.getY();
            }
        } else if (direction == Direction.DOWN) {
            if (worldMap.getHeight() < player.getY() + player.getHeight() + playersVelocity) {
                steps = worldMap.getHeight() - player.getY() - player.getHeight();
            }
        } else if (direction == Direction.LEFT) {
            if (0 > player.getX() - playersVelocity) {
                steps = player.getX();
            }
        } else if (direction == Direction.RIGHT) {
            if (worldMap.getWidth() < player.getX() + player.getWidth() + playersVelocity) {
                steps = worldMap.getWidth() - player.getX() - player.getWidth();
            }
        }

        player.moveByDirection(direction, steps);

        // collision with map objects
        for (GameObject wall : worldMap.getWalls()) {
            if (player.isColliding(wall)) {
                if (direction == Direction.DOWN) {
                    player.setY(wall.getY() - player.getHeight());
                } else if (direction == Direction.UP) {
                    player.setY(wall.getY() + wall.getHeight());
                } else if (direction == Direction.LEFT) {
                    player.setX(wall.getX() + wall.getWidth());
                } else if (direction == Direction.RIGHT) {
                    player.setX(wall.getX() - player.getWidth());
                }
            }
        }
        for (Chest chest : worldMap.getChests()) {
            if (player.isColliding(chest)) {
                if (direction == Direction.DOWN) {
                    player.setY(chest.getY() - player.getHeight());
                } else if (direction == Direction.UP) {
                    player.setY(chest.getY() + chest.getHeight());
                } else if (direction == Direction.LEFT) {
                    player.setX(chest.getX() + chest.getWidth());
                } else if (direction == Direction.RIGHT) {
                    player.setX(chest.getX() - player.getWidth());
                }
            }
        }
    }

    @Override
    public void shoot(double mouseX, double mouseY, UUID playerId) {
        Player player = getPlayerById(playerId);
        double length = Math.sqrt(mouseX * mouseX + mouseY * mouseY);
        double vectorX = mouseX / length;
        double vectorY = mouseY / length;
        Bullet newBullet = new Bullet(player.getX(), player.getY(), vectorX, vectorY, player.getPistolDamage(), player);
        bullets.add(newBullet);
    }

    @Override
    public WorldMap getWorldMap() {
        return worldMap;
    }

    @Override
    public List<Bullet> getBullets() {
        return bullets;
    }

    @Override
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
