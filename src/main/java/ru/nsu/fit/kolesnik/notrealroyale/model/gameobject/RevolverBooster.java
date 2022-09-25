package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class RevolverBooster extends GameObject {

    private final static double WIDTH = 1;
    private final static double HEIGHT = 1;
    private final static double COLLIDABLE_RECT_PADDING_X = 0;
    private final static double COLLIDABLE_RECT_PADDING_Y = 0;
    public final static double REVOLVER_BULLET_DAMAGE_MULTIPLIER = 1.15;
    public final static double REVOLVER_BULLET_MAX_RANGE_MULTIPLIER = 1.20;
    public final static double REVOLVER_BULLET_SPEED_MULTIPLIER = 1.1;

    public RevolverBooster(double x, double y) {
        super(x, y, WIDTH, HEIGHT, COLLIDABLE_RECT_PADDING_X, COLLIDABLE_RECT_PADDING_Y);
    }

}
