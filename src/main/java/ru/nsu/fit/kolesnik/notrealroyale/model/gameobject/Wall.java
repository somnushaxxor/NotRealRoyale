package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Wall extends GameObject {
    public final static int DEFAULT_WIDTH = 1;
    public final static int DEFAULT_HEIGHT = 1;


    public Wall(double x, double y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
