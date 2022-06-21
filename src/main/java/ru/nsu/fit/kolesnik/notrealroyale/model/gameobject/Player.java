package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Player extends ActiveObject {
    private final static double DEFAULT_HP = 100;
    private final static double DEFAULT_VELOCITY = 0.1;
    private final static double DEFAULT_WIDTH = 1;
    private final static double DEFAULT_HEIGHT = 1;
    private final static double DEFAULT_COLLIDABLE_RECT_PADDING_X = 0.1;
    private final static double DEFAULT_COLLIDABLE_RECT_PADDING_Y = 0.1;

    private double hp;
    private double pistolDamage;
    private final String name;

    public Player(String name, double x, double y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_COLLIDABLE_RECT_PADDING_X, DEFAULT_COLLIDABLE_RECT_PADDING_Y, DEFAULT_VELOCITY);
        this.hp = DEFAULT_HP;
        this.name = name;
        pistolDamage = 5;
    }

    public void moveByDirection(Direction direction) {
        if (direction == Direction.UP) {
            moveY(-getVelocity());
        } else if (direction == Direction.DOWN) {
            moveY(getVelocity());
        } else if (direction == Direction.LEFT) {
            moveX(-getVelocity());
        } else if (direction == Direction.RIGHT) {
            moveX(getVelocity());
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
