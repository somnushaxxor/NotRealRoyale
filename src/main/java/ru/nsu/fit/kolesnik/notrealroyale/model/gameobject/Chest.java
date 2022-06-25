package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

import java.io.Serializable;

public class Chest extends GameObject implements Serializable {
    private final static double MAX_HP = 40;
    private final static double WIDTH = 1;
    private final static double HEIGHT = 1;
    private final static double COLLIDABLE_RECT_PADDING_X = 0;
    private final static double COLLIDABLE_RECT_PADDING_Y = 0;

    private double hp;
    private boolean alive;
    private boolean damaged;

    public Chest(double x, double y) {
        super(x, y, WIDTH, HEIGHT, COLLIDABLE_RECT_PADDING_X, COLLIDABLE_RECT_PADDING_Y);
        hp = MAX_HP;
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
