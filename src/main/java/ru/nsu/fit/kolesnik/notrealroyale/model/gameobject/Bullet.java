package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Bullet extends ActiveObject {
    private final static double DEFAULT_WIDTH = 1;
    private final static double DEFAULT_HEIGHT = 1;
    private final static double DEFAULT_RANGE = 30;
    private final static double DEFAULT_VELOCITY = 1;
    private final static double DEFAULT_COLLIDABLE_RECT_PADDING_X = 0.25;
    private final static double DEFAULT_COLLIDABLE_RECT_PADDING_Y = 0.25;

    private double range;
    private final double damage;
    private final Player player;

    private final double vectorX;
    private final double vectorY;

    public Bullet(double x, double y, double vectorX, double vectorY, double damage, Player player) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLLIDABLE_RECT_PADDING_X, DEFAULT_COLLIDABLE_RECT_PADDING_Y, DEFAULT_VELOCITY);
        this.vectorX = vectorX;
        this.vectorY = vectorY;
        range = DEFAULT_RANGE;
        this.damage = damage;
        this.player = player;
    }

    public void update() {
        moveX(vectorX * getVelocity());
        moveY(vectorY * getVelocity());
        range -= getVelocity();
        if (range <= 0) {
            setAlive(false);
        }
    }

    public double getDamage() {
        return damage;
    }
}
