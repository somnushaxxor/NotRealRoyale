package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Player extends ActiveObject {
    public final static double DEFAULT_HP = 100;
    public final static double DEFAULT_VELOCITY = 0.1;
    public final static double DEFAULT_WIDTH = 1;
    public final static double DEFAULT_HEIGHT = 1;

    private double hp;
    private double pistolDamage;
    private final String name;

    public Player(String name, double x, double y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_VELOCITY);
        this.hp = DEFAULT_HP;
        this.name = name;
        pistolDamage = 5;
    }

    public void moveByDirection(Direction direction, double steps) {
        if (direction == Direction.UP) {
            moveY(-steps);
        } else if (direction == Direction.DOWN) {
            moveY(steps);
        } else if (direction == Direction.LEFT) {
            moveX(-steps);
        } else if (direction == Direction.RIGHT) {
            moveX(steps);
        }
    }

    public String getName() {
        return name;
    }

    public double getPistolDamage() {
        return pistolDamage;
    }

    public double getHp() {
        return hp;
    }
}
