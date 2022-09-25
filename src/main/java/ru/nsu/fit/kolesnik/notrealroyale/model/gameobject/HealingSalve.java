package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class HealingSalve extends GameObject {

    private final static double WIDTH = 1;
    private final static double HEIGHT = 1;
    private final static double COLLIDABLE_RECT_PADDING_X = 0;
    private final static double COLLIDABLE_RECT_PADDING_Y = 0;
    public final static int REGENERATABLE_HP = 20;

    public HealingSalve(double x, double y) {
        super(x, y, WIDTH, HEIGHT, COLLIDABLE_RECT_PADDING_X, COLLIDABLE_RECT_PADDING_Y);
    }

}
