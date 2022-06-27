package ru.nsu.fit.kolesnik.notrealroyale.view.javafx;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.*;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.Tile;
import ru.nsu.fit.kolesnik.notrealroyale.model.worldmap.WorldMap;
import ru.nsu.fit.kolesnik.notrealroyale.view.GameView;

import java.util.List;

public class GraphicGameView implements GameView {
    public static final double GAME_WINDOW_WIDTH = 1120;
    public static final double GAME_WINDOW_HEIGHT = 736;
    private static final double DEFAULT_MODEL_UNIT_SIZE = 32;

    private final Scene scene;
    private final Canvas canvas;
    private final GraphicsContext graphicsContext;

    private final String clientName;
    private double cameraX;
    private double cameraY;

    private final Image backgroundImage = new Image("background.jpg");
    private final Image playerImage = new Image("player.png");
    private final Image sandImage = new Image("sand.png");
    private final Image wallImage = new Image("wall.png");
    private final Image rockImage = new Image("rock.png");
    private final Image cactusImage = new Image("cactus.png");
    private final Image bulletImage = new Image("bullet.png");
    private final Image chestImage = new Image("chest.png");
    private final Image healingSalveImage = new Image("salve.png");
    private final Image boosterImage = new Image("booster.png");

    public GraphicGameView(String clientName, Canvas canvas, Scene scene) {
        this.clientName = clientName;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.scene = scene;
        scene.setCursor(new ImageCursor(new Image("cursor.png")));
    }

    public void startHandlingUserInput(GameController controller) {
        KeyHandler keyHandler = new KeyHandler(this);
        keyHandler.start(controller);
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void drawFrame(WorldMap worldMap, List<Player> players, List<Chest> chests, List<Bullet> bullets, List<RevolverBooster> revolverBoosters, List<HealingSalve> healingSalves) {
        for (Player player : players) {
            if (player.getName().equals(clientName)) {
                cameraX = player.getX();
                cameraY = player.getY();
                break;
            }
        }
        drawBackground();
        drawVisibleTiles(worldMap);
        drawBullets(bullets);
        drawChests(chests);
        drawRevolverBoosters(revolverBoosters);
        drawHealingSalves(healingSalves);
        drawPlayers(players);
    }

    private void drawBackground() {
        graphicsContext.drawImage(backgroundImage, 0, 0);
    }

    private void drawVisibleTiles(WorldMap worldMap) {
        double horizontalVisibleTilesPadding = (GAME_WINDOW_WIDTH - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        double verticalVisibleTilesPadding = (GAME_WINDOW_HEIGHT - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
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
        graphicsContext.drawImage(tileImage, getScreenX(tileX), getScreenY(tileY));
    }

    private void drawPlayers(List<Player> players) {
        for (Player player : players) {
            double playerX = player.getX();
            double playerY = player.getY();
            graphicsContext.drawImage(playerImage, getScreenX(playerX), getScreenY(playerY));
        }
    }

    private void drawBullets(List<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            double bulletX = bullet.getX();
            double bulletY = bullet.getY();
            graphicsContext.drawImage(bulletImage, getScreenX(bulletX), getScreenY(bulletY));
        }
    }

    private void drawChests(List<Chest> chests) {
        double horizontalVisibleTilesPadding = (GAME_WINDOW_WIDTH - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        double verticalVisibleTilesPadding = (GAME_WINDOW_HEIGHT - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        for (Chest chest : chests) {
            double chestX = chest.getX();
            double chestY = chest.getY();
            if (chestX > cameraX - horizontalVisibleTilesPadding - 1 && chestX < cameraX + horizontalVisibleTilesPadding && chestY > cameraY - verticalVisibleTilesPadding - 1 && chestY < cameraY + verticalVisibleTilesPadding) {
                graphicsContext.drawImage(chestImage, getScreenX(chestX), getScreenY(chestY));
            }
        }
    }

    private void drawRevolverBoosters(List<RevolverBooster> revolverBoosters) {
        double horizontalVisibleTilesPadding = (GAME_WINDOW_WIDTH - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        double verticalVisibleTilesPadding = (GAME_WINDOW_HEIGHT - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        for (RevolverBooster revolverBooster : revolverBoosters) {
            double boosterX = revolverBooster.getX();
            double boosterY = revolverBooster.getY();
            if (boosterX > cameraX - horizontalVisibleTilesPadding - 1 && boosterX < cameraX + horizontalVisibleTilesPadding && boosterY > cameraY - verticalVisibleTilesPadding - 1 && boosterY < cameraY + verticalVisibleTilesPadding) {
                graphicsContext.drawImage(boosterImage, getScreenX(boosterX), getScreenY(boosterY));
            }
        }
    }

    private void drawHealingSalves(List<HealingSalve> healingSalves) {
        double horizontalVisibleTilesPadding = (GAME_WINDOW_WIDTH - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        double verticalVisibleTilesPadding = (GAME_WINDOW_HEIGHT - DEFAULT_MODEL_UNIT_SIZE) / (2 * DEFAULT_MODEL_UNIT_SIZE) + 1;
        for (HealingSalve healingSalve : healingSalves) {
            double healingSalveX = healingSalve.getX();
            double healingSalveY = healingSalve.getY();
            if (healingSalveX > cameraX - horizontalVisibleTilesPadding - 1 && healingSalveX < cameraX + horizontalVisibleTilesPadding && healingSalveY > cameraY - verticalVisibleTilesPadding - 1 && healingSalveY < cameraY + verticalVisibleTilesPadding) {
                graphicsContext.drawImage(healingSalveImage, getScreenX(healingSalveX), getScreenY(healingSalveY));
            }
        }
    }

    private double getScreenX(double modelX) {
        return (modelX - cameraX) * DEFAULT_MODEL_UNIT_SIZE + canvas.getWidth() / 2;
    }

    private double getScreenY(double modelY) {
        return (modelY - cameraY) * DEFAULT_MODEL_UNIT_SIZE + canvas.getHeight() / 2;
    }
}
