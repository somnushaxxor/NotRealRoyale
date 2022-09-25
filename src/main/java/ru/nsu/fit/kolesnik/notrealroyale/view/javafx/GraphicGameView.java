package ru.nsu.fit.kolesnik.notrealroyale.view.javafx;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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

    private final String clientUsername;
    private double cameraX;
    private double cameraY;

    private final Image backgroundImage = new Image("images/background.jpg");
    private final Image playerImage = new Image("images/player.png");
    private final Image hpImage = new Image("images/hp.png");
    private final Image sandImage = new Image("images/sand.png");
    private final Image wallImage = new Image("images/wall.png");
    private final Image rockImage = new Image("images/rock.png");
    private final Image cactusImage = new Image("images/cactus.png");
    private final Image bulletImage = new Image("images/bullet.png");
    private final Image chestImage = new Image("images/chest.png");
    private final Image healingSalveImage = new Image("images/salve.png");
    private final Image boosterImage = new Image("images/booster.png");

    private final Font userInterfaceFont;
    private final Font gameSessionResultFont;

    public GraphicGameView(String clientUsername, Canvas canvas, Scene scene) {
        this.clientUsername = clientUsername;
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        this.scene = scene;
        scene.setCursor(new ImageCursor(new Image("images/cursor.png")));
        userInterfaceFont = new Font("Verdana", 24);
        gameSessionResultFont = new Font("Verdana", 64);
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
        Player clientPlayer = null;
        for (Player player : players) {
            if (player.getName().equals(clientUsername)) {
                clientPlayer = player;
                break;
            }
        }
        if (clientPlayer != null) {
            cameraX = clientPlayer.getX();
            cameraY = clientPlayer.getY();
        }
        drawBackground();
        drawVisibleTiles(worldMap);
        drawBullets(bullets);
        drawChests(chests);
        drawRevolverBoosters(revolverBoosters);
        drawHealingSalves(healingSalves);
        drawAlivePlayers(players);
        if (clientPlayer != null && clientPlayer.isAlive()) {
            drawUserInterface(clientPlayer);
        } else if (clientPlayer != null && !clientPlayer.isAlive()) {
            drawGameSessionResult(clientPlayer);
        }
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

    private void drawAlivePlayers(List<Player> players) {
        for (Player player : players) {
            if (player.isAlive()) {
                double playerX = player.getX();
                double playerY = player.getY();
                graphicsContext.drawImage(playerImage, getScreenX(playerX), getScreenY(playerY));
            }
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

    private void drawUserInterface(Player clientPlayer) {
        graphicsContext.setFont(userInterfaceFont);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.drawImage(hpImage, 1, 1);
        graphicsContext.fillText(String.format("%.2f", clientPlayer.getHp()), hpImage.getWidth() + 1, userInterfaceFont.getSize() + 1);
        graphicsContext.fillText("LVL: " + clientPlayer.getRevolverLevel(), 1, 2 * userInterfaceFont.getSize() + 4);
        graphicsContext.drawImage(healingSalveImage, 1, 2 * userInterfaceFont.getSize() + 5);
        graphicsContext.fillText(": " + clientPlayer.getHealingSalvesNumber(), healingSalveImage.getWidth() + 1, 3 * userInterfaceFont.getSize() + 6);
        graphicsContext.fillText("SCORE: " + clientPlayer.getScore(), 1, 4 * userInterfaceFont.getSize() + 8);
    }

    private void drawGameSessionResult(Player clientPlayer) {
        graphicsContext.setFont(gameSessionResultFont);
        graphicsContext.setFill(Color.RED);
        graphicsContext.fillText("GAME OVER", canvas.getWidth() / 2 - 200, canvas.getHeight() / 2);
        graphicsContext.fillText("SCORE: " + clientPlayer.getScore(), canvas.getWidth() / 2 - 150, canvas.getHeight() / 2 + 100);
    }

    private double getScreenX(double modelX) {
        return (modelX - cameraX) * DEFAULT_MODEL_UNIT_SIZE + canvas.getWidth() / 2;
    }

    private double getScreenY(double modelY) {
        return (modelY - cameraY) * DEFAULT_MODEL_UNIT_SIZE + canvas.getHeight() / 2;
    }

}
