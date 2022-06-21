package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Chest extends GameObject {
    private final static double DEFAULT_HP = 20;
    private final static double DEFAULT_WIDTH = 1;
    private final static double DEFAULT_HEIGHT = 1;
    private final static double DEFAULT_COLLIDABLE_RECT_PADDING_X = 0;
    private final static double DEFAULT_COLLIDABLE_RECT_PADDING_Y = 0;

    private double hp;
    private boolean alive;
    private boolean damaged;

    public Chest(double x, double y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLLIDABLE_RECT_PADDING_X, DEFAULT_COLLIDABLE_RECT_PADDING_Y);
        hp = DEFAULT_HP;
        alive = true;
        damaged = false;
    }

    public void receiveDamage(double damage) {
        hp -= damage;
        damaged = true;
        if (hp <= 0) {
            setAlive(false);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getHp() {
        return hp;
    }

    public boolean isDamaged() {
        return damaged;
    }
}
