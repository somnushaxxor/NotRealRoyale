package ru.nsu.fit.kolesnik.notrealroyale.view.javafx;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import ru.nsu.fit.kolesnik.notrealroyale.controller.GameController;
import ru.nsu.fit.kolesnik.notrealroyale.controller.Key;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class KeyHandler {

    private final GraphicGameView view;
    private final HashMap<KeyCode, Boolean> keys;

    public KeyHandler(GraphicGameView view) {
        this.view = view;
        keys = new HashMap<>();
    }

    public void start(GameController controller) {
        Scene scene = view.getScene();
        AtomicBoolean isHandledKeyE = new AtomicBoolean(false);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false);
            if (event.getCode() == KeyCode.E) {
                isHandledKeyE.set(false);
            }
        });
        scene.setOnMouseClicked(
                event -> {
                    double mouseX = event.getX() - view.getScene().getWidth() / 2;
                    double mouseY = event.getY() - view.getScene().getHeight() / 2;
                    controller.onMouseClicked(mouseX, mouseY);
                });
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (keys.getOrDefault(KeyCode.W, false)) {
                    controller.onKeyPressed(Key.W);
                }
                if (keys.getOrDefault(KeyCode.S, false)) {
                    controller.onKeyPressed(Key.S);
                }
                if (keys.getOrDefault(KeyCode.A, false)) {
                    controller.onKeyPressed(Key.A);
                }
                if (keys.getOrDefault(KeyCode.D, false)) {
                    controller.onKeyPressed(Key.D);
                }
                if (keys.getOrDefault(KeyCode.E, false) && !isHandledKeyE.get()) {
                    controller.onKeyPressed(Key.E);
                    isHandledKeyE.set(true);
                }
            }
        };
        timer.start();
    }

}
