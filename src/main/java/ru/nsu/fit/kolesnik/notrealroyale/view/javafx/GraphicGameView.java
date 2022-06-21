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
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Player;
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
    Image playerImage = new Image("duck.png");
    Image grassImage = new Image("grass.png");
    Image wallImage = new Image("wall.png");
    Image rockImage = new Image("rock.png");

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
                drawBackground();
                Player clientPlayer = gameModel.getPlayerById(clientPlayerId);
                cameraX = clientPlayer.getX();
                cameraY = clientPlayer.getY();
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
                drawPlayers();
            }
        });
    }

    private void drawBackground() {
        graphicsContext.drawImage(backgroundImage, 0, 0);
    }

    private void drawTile(Tile tile, int tileX, int tileY) {
        Image tileImage = null;
        if (tile == Tile.GRASS) {
            tileImage = grassImage;
        } else if (tile == Tile.WALL) {
            tileImage = wallImage;
        } else if (tile == Tile.ROCK) {
            tileImage = rockImage;
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

    private double getGameObjectScreenX(double gameObjectX) {
        return (gameObjectX - cameraX) * DEFAULT_MODEL_UNIT_SIZE + canvas.getWidth() / 2;
    }

    private double getGameObjectScreenY(double gameObjectY) {
        return (gameObjectY - cameraY) * DEFAULT_MODEL_UNIT_SIZE + canvas.getHeight() / 2;
    }
}
