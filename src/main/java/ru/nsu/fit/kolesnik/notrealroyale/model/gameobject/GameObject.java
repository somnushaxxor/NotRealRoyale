package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

import java.io.Serializable;
import java.util.UUID;

public class GameObject implements Serializable {
    private double x;
    private double y;
    private final double width;
    private final double height;
    private final UUID id;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        id = UUID.randomUUID();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public UUID getId() {
        return id;
    }
}
