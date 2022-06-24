package ru.nsu.fit.kolesnik.notrealroyale.view.javafx;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.model.GameModel;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.*;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.Tile;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.WorldMap;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.util.UUID;

public class GraphicGameView implements GameView {
    private static final double GAME_WINDOW_WIDTH = 1120;
    private static final double GAME_WINDOW_HEIGHT = 736;
    private static final double DEFAULT_MODEL_UNIT_SIZE = 32;

    private final GameModel gameModel;

    private final GameController controller;
    private UUID clientPlayerId;
    private final Group root;
    private final Scene scene;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;
    private final KeyHandler keyHandler;

    private double cameraX;
    private double cameraY;

    Image backgroundImage = new Image("background.jpg");
    Image playerImage = new Image("player.png");
    Image sandImage = new Image("sand.png");
    Image wallImage = new Image("wall.png");
    Image rockImage = new Image("rock.png");
    Image cactusImage = new Image("cactus.png");
    Image bulletImage = new Image("bullet.png");
    Image chestImage = new Image("chest.png");
    Image healingSalveImage = new Image("salve.png");
    Image boosterImage = new Image("booster.png");

    public GraphicGameView(GameModel gameModel, GameController controller, Group root, Scene scene) {
        this.gameModel = gameModel;
        this.controller = controller;
        this.root = root;
        this.scene = scene;
        scene.setCursor(new ImageCursor(new Image("cursor.png")));
        this.canvas = new Canvas(GAME_WINDOW_WIDTH, GAME_WINDOW_HEIGHT);
        root.getChildren().add(canvas);
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
                clientPlayerId = UUID.fromString(eventStringSplitted[1]);
                controller.setClientPlayerId(clientPlayerId);
            } else if (eventStringSplitted[0].equals("GAME_UPDATED")) {
                Player clientPlayer = gameModel.getPlayerById(clientPlayerId);
                cameraX = clientPlayer.getX();
                cameraY = clientPlayer.getY();
                drawBackground();
                drawVisibleTiles();
                drawChests();
                drawBoosters();
                drawHealingSalves();
                drawBullets();
                drawPlayers();
            }
        });
    }

    private void drawBackground() {
        graphicsContext.drawImage(backgroundImage, 0, 0);
    }

    private void drawVisibleTiles() {
        double horizontalVisibleTilesPadding = (GAME_WINDOW_WIDTH - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        double verticalVisibleTilesPadding = (GAME_WINDOW_HEIGHT - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        WorldMap worldMap = gameModel.getWorldMap();
        for (int y = 0; y < worldMap.getHeight(); y++) {
            for (int x = 0; x < worldMap.getWidth(); x++) {
                if (x > cameraX - horizontalVisibleTilesPadding - 1 && x < cameraX + horizontalVisibleTilesPadding && y > cameraY - verticalVisibleTilesPadding - 1 && y < cameraY + verticalVisibleTilesPadding) {
                    drawTile(worldMap.getTile(x, y), x, y);
                }
            }
        }
    }

    private void drawTile(Tile tile, int tileX, int tileY) {
        Image tileImage = null;
        if (tile == Tile.SAND) {
            tileImage = sandImage;
        } else if (tile == Tile.WALL) {
            tileImage = wallImage;
        } else if (tile == Tile.ROCK) {
            tileImage = rockImage;
        } else if (tile == Tile.CACTUS) {
            tileImage = cactusImage;
        }
        graphicsContext.drawImage(tileImage, getGameObjectScreenX(tileX), getGameObjectScreenY(tileY));
    }

    private void drawPlayers() {
        for (Player player : gameModel.getPlayers()) {
            double playerX = player.getX();
            double playerY = player.getY();
            graphicsContext.drawImage(playerImage, getGameObjectScreenX(playerX), getGameObjectScreenY(playerY));
        }
    }

    private void drawBullets() {
        for (Bullet bullet : gameModel.getBullets()) {
            double bulletX = bullet.getX();
            double bulletY = bullet.getY();
            graphicsContext.drawImage(bulletImage, getGameObjectScreenX(bulletX), getGameObjectScreenY(bulletY));
        }
    }

    private void drawChests() {
        double horizontalVisibleTilesPadding = (GAME_WINDOW_WIDTH - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        double verticalVisibleTilesPadding = (GAME_WINDOW_HEIGHT - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        for (Chest chest : gameModel.getWorldMap().getChests()) {
            double chestX = chest.getX();
            double chestY = chest.getY();
            if (chestX > cameraX - horizontalVisibleTilesPadding - 1 && chestX < cameraX + horizontalVisibleTilesPadding && chestY > cameraY - verticalVisibleTilesPadding - 1 && chestY < cameraY + verticalVisibleTilesPadding) {
                graphicsContext.drawImage(chestImage, getGameObjectScreenX(chestX), getGameObjectScreenY(chestY));
            }
        }
    }

    private void drawBoosters() {
        double horizontalVisibleTilesPadding = (GAME_WINDOW_WIDTH - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        double verticalVisibleTilesPadding = (GAME_WINDOW_HEIGHT - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        for (RevolverBooster revolverBooster : gameModel.getWorldMap().getBoosters()) {
            double boosterX = revolverBooster.getX();
            double boosterY = revolverBooster.getY();
            if (boosterX > cameraX - horizontalVisibleTilesPadding - 1 && boosterX < cameraX + horizontalVisibleTilesPadding && boosterY > cameraY - verticalVisibleTilesPadding - 1 && boosterY < cameraY + verticalVisibleTilesPadding) {
                graphicsContext.drawImage(boosterImage, getGameObjectScreenX(boosterX), getGameObjectScreenY(boosterY));
            }
        }
    }

    private void drawHealingSalves() {
        double horizontalVisibleTilesPadding = (GAME_WINDOW_WIDTH - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        double verticalVisibleTilesPadding = (GAME_WINDOW_HEIGHT - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        for (HealingSalve healingSalve : gameModel.getWorldMap().getHealingSalves()) {
            double healingSalveX = healingSalve.getX();
            double healingSalveY = healingSalve.getY();
            if (healingSalveX > cameraX - horizontalVisibleTilesPadding - 1 && healingSalveX < cameraX + horizontalVisibleTilesPadding && healingSalveY > cameraY - verticalVisibleTilesPadding - 1 && healingSalveY < cameraY + verticalVisibleTilesPadding) {
                graphicsContext.drawImage(healingSalveImage, getGameObjectScreenX(healingSalveX), getGameObjectScreenY(healingSalveY));
            }
        }
    }

    private double getGameObjectScreenX(double gameObjectX) {
        return (gameObjectX - cameraX) * DEFAULT_MODEL_UNIT_SIZE + canvas.getWidth() / 2;
    }

    private double getGameObjectScreenY(double gameObjectY) {
        return (gameObjectY - cameraY) * DEFAULT_MODEL_UNIT_SIZE + canvas.getHeight() / 2;
    }
}
