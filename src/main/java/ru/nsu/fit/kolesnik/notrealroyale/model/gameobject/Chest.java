package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Chest extends GameObject {
    public final static double DEFAULT_HP = 20;
    public final static double DEFAULT_WIDTH = 1;
    public final static double DEFAULT_HEIGHT = 1;

    private double hp;
    private boolean alive;
    private boolean damaged;

    public Chest(double x, double y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
