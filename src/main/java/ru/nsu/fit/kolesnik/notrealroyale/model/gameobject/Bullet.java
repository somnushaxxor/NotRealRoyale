package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Bullet extends GameObject {
    private final static double WIDTH = 1;
    private final static double HEIGHT = 1;
    private final static double COLLIDABLE_RECT_PADDING_X = 0.30;
    private final static double COLLIDABLE_RECT_PADDING_Y = 0.25;

    private double range;
    private final double damage;
    private final Player player;
    private final double velocity;
    private final double vectorX;
    private final double vectorY;
    private boolean alive;

    public Bullet(double x, double y, double vectorX, double vectorY, Player player) {
        super(x, y, WIDTH, HEIGHT, COLLIDABLE_RECT_PADDING_X, COLLIDABLE_RECT_PADDING_Y);
        this.vectorX = vectorX;
        this.vectorY = vectorY;
        this.player = player;
        range = player.getRevolverBulletMaxRange();
        velocity = player.getRevolverBulletSpeed();
        damage = player.getRevolverBulletDamage();
        alive = true;
    }

    public void update() {
        moveX(vectorX * getVelocity());
        moveY(vectorY * getVelocity());
        range -= getVelocity();
        if (range <= 0) {
            setAlive(false);
        }
    }

    public void moveX(double dx) {
        double currentX = getX();
        setX(currentX + dx);
    }

    public void moveY(double dy) {
        double currentY = getY();
        setY(currentY + dy);
    }

    public double getDamage() {
        return damage;
    }

    public double getVelocity() {
        return velocity;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
