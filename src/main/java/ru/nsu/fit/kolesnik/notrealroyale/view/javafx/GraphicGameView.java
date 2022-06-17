package ru.nsu.fit.kolesnik.notrealroyale.view.javafx;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Bullet;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Chest;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Player;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Wall;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.util.UUID;

public class GraphicGameView implements GameView {
    private static final double DEFAULT_MODEL_UNIT_SIZE = 32;

    private final GameModel gameModel;

    private final GameController controller;
    private UUID playerId;
    private final Group root;
    private final Scene scene;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final KeyHandler keyHandler;

    private double cameraX;
    private double cameraY;

    public GraphicGameView(GameModel gameModel, GameController controller, Group root, Scene scene, Canvas canvas) {
        this.gameModel = gameModel;
        this.controller = controller;
        this.root = root;
        this.scene = scene;
        scene.setCursor(new ImageCursor(new Image("cursor.png")));
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.keyHandler = new KeyHandler(this);
        keyHandler.startHandling();
    }

    public Scene getScene() {
        return scene;
    }

    public GameController getController() {
        return controller;
    }

    @Override
    public void update(String eventString) {
        Platform.runLater(() -> {
            String[] eventStringSplitted = eventString.split(" ");
            if (eventStringSplitted[0].equals("PLAYER_ADDED")) {
                playerId = UUID.fromString(eventStringSplitted[1]);
                controller.setPlayerId(playerId);
            } else if (eventStringSplitted[0].equals("GAME_UPDATED")) {
                drawBackground();
                for (Wall wall : gameModel.getWorldMap().getWalls()) {
                    Image wallImage = new Image("wall.png");
                    graphicsContext.drawImage(wallImage, getGameObjectScreenX(wall.getX()), getGameObjectScreenY(wall.getY()));
                }
                for (Chest chest : gameModel.getWorldMap().getChests()) {
                    Image chestImage = new Image("chest.png");
                    graphicsContext.drawImage(chestImage, getGameObjectScreenX(chest.getX()), getGameObjectScreenY(chest.getY()));
                    if (chest.isDamaged()) {
                        graphicsContext.setFill(Color.RED);
                        graphicsContext.fillRect(getGameObjectScreenX(chest.getX()), getGameObjectScreenY(chest.getY()) - 10, chest.getWidth() / 20.0 * chest.getHp() * DEFAULT_MODEL_UNIT_SIZE, 5);
                    }
                }
                for (Bullet bullet : gameModel.getBullets()) {
                    Image bulletImage = new Image("bullet.png");
                    graphicsContext.drawImage(bulletImage, getGameObjectScreenX(bullet.getX()), getGameObjectScreenY(bullet.getY()));
                }
                for (Player player : gameModel.getPlayers()) {
                    Image playerImage = new Image("duck.png");
                    if (playerId.equals(player.getId())) {
                        cameraX = player.getX();
                        cameraY = player.getY();
                    }
                    graphicsContext.drawImage(playerImage, getGameObjectScreenX(player.getX()), getGameObjectScreenY(player.getY()));
                }


//                final String DEFAULT_PLAYER_NAME = "UNKNOWN";
//                String interfacePlayerName = DEFAULT_PLAYER_NAME;
//                String interfacePlayerHp = "";
//                List<String> buffer = new ArrayList<>();
//                for (int i = 1; i < eventStringSplitted.length; i++) {
//                    String elem = eventStringSplitted[i];
//                    if (elem.equals("END")) {
//                        if (buffer.get(0).equals("WALL")) {
//                            Image wallImage = new Image("wall.png");
//                            double wallX = Double.parseDouble(buffer.get(1));
//                            double wallY = Double.parseDouble(buffer.get(2));
//                            graphicsContext.drawImage(wallImage, getGameObjectScreenX(wallX), getGameObjectScreenY(wallY));
//                        } else if (buffer.get(0).equals("CHEST")) {
//                            Image chestImage = new Image("chest.png");
//                            double chestX = Double.parseDouble(buffer.get(1));
//                            double chestY = Double.parseDouble(buffer.get(2));
//                            int chestWidth = Integer.parseInt(buffer.get(3));
//                            double chestHp = Double.parseDouble(buffer.get(4));
//                            boolean chestDamaged = Boolean.parseBoolean(buffer.get(5));
//                            graphicsContext.drawImage(chestImage, getGameObjectScreenX(chestX), getGameObjectScreenY(chestY));
//                            if (chestDamaged) {
//                                graphicsContext.setFill(Color.RED);
//                                graphicsContext.fillRect(getGameObjectScreenX(chestX), getGameObjectScreenY(chestY) - 10, chestWidth / 20.0 * chestHp * DEFAULT_ENTITY_UNIT_SIZE, 5);
//                            }
//                        } else if (buffer.get(0).equals("BULLET")) {
//                            Image bulletImage = new Image("bullet.png");
//                            double bulletX = Double.parseDouble(buffer.get(1));
//                            double bulletY = Double.parseDouble(buffer.get(2));
//                            graphicsContext.drawImage(bulletImage, getGameObjectScreenX(bulletX), getGameObjectScreenY(bulletY));
//                        } else if (buffer.get(0).equals("PLAYER")) {
//                            int playerWidth = Integer.parseInt(buffer.get(5));
//                            int playerHeight = Integer.parseInt(buffer.get(6));
//                            Image playerImage = new Image("duck.png", playerWidth * DEFAULT_ENTITY_UNIT_SIZE, playerHeight * DEFAULT_ENTITY_UNIT_SIZE, true, true);
//                            UUID currentPlayerId = UUID.fromString(buffer.get(1));
//                            double playerX = Double.parseDouble(buffer.get(2));
//                            double playerY = Double.parseDouble(buffer.get(3));
//                            if (currentPlayerId.equals(playerId)) {
//                                cameraX = playerX;
//                                cameraY = playerY;
//                            }
//                            graphicsContext.drawImage(playerImage, getGameObjectScreenX(playerX), getGameObjectScreenY(playerY));
//                            String playerName = buffer.get(4);
//                            if (!currentPlayerId.equals(playerId)) {
//                                graphicsContext.setFill(Color.WHITE);
//                                graphicsContext.setFont(new Font("Arial", 25));
//                                graphicsContext.fillText(playerName, getGameObjectScreenX(playerX), getGameObjectScreenY(playerY));
//                            } else {
//                                interfacePlayerName = playerName;
//                                interfacePlayerHp = buffer.get(7);
//                            }
//                        }
//                        buffer.clear();
//                    } else {
//                        buffer.add(elem);
//                    }
//                }
                // draw interface
//                graphicsContext.setFill(Color.WHITE);
//                graphicsContext.setFont(new Font("Roboto", 25));
//                graphicsContext.fillText(interfacePlayerName, 5, 25);
//                graphicsContext.drawImage(new Image("hp.png"), 5, 30);
//                graphicsContext.fillText(interfacePlayerHp, 40, 55);
            }
        });
    }

    private void drawBackground() {
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private double getGameObjectScreenX(double gameObjectX) {
        return (gameObjectX - cameraX) * DEFAULT_MODEL_UNIT_SIZE + canvas.getWidth() / 2;
    }

    private double getGameObjectScreenY(double gameObjectY) {
        return (gameObjectY - cameraY) * DEFAULT_MODEL_UNIT_SIZE + canvas.getHeight() / 2;
    }
}
