package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Player extends GameObject {
    private final static double MAX_HP = 100;
    private final static double VELOCITY = 0.07;
    private final static double WIDTH = 1;
    private final static double HEIGHT = 1;
    private final static double COLLIDABLE_RECT_PADDING_X = 0.15;
    private final static double COLLIDABLE_RECT_PADDING_Y = 0.15;
    private final static double DEFAULT_REVOLVER_BULLET_DAMAGE = 10;
    private final static double DEFAULT_REVOLVER_BULLET_SPEED = 0.35;
    private final static double DEFAULT_REVOLVER_BULLET_MAX_RANGE = 15;


    private double hp;
    private boolean alive;
    private double revolverBulletDamage;
    private double revolverBulletSpeed;
    private double revolverBulletMaxRange;
    private int healingSalvesNumber;
    private final String name;
    private final double velocity;

    public Player(String name, double x, double y) {
        super(x, y, WIDTH, HEIGHT, COLLIDABLE_RECT_PADDING_X, COLLIDABLE_RECT_PADDING_Y);
        this.hp = MAX_HP;
        this.name = name;
        revolverBulletDamage = DEFAULT_REVOLVER_BULLET_DAMAGE;
        revolverBulletMaxRange = DEFAULT_REVOLVER_BULLET_MAX_RANGE;
        revolverBulletSpeed = DEFAULT_REVOLVER_BULLET_SPEED;
        healingSalvesNumber = 0;
        velocity = VELOCITY;
        alive = true;
    }

    public void moveX(double dx) {
        double currentX = getX();
        setX(currentX + dx);
    }

    public void moveY(double dy) {
        double currentY = getY();
        setY(currentY + dy);
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

    public void pickHealingSalveUp() {
        healingSalvesNumber++;
    }

    public void useHealingSalve() {
        if (healingSalvesNumber > 0) {
            hp += HealingSalve.REGENERATABLE_HP;
            healingSalvesNumber--;
        }
    }

    public void powerUpRevolver() {
        revolverBulletDamage = revolverBulletDamage * RevolverBooster.REVOLVER_BULLET_DAMAGE_MULTIPLIER;
        revolverBulletSpeed = revolverBulletSpeed * RevolverBooster.REVOLVER_BULLET_SPEED_MULTIPLIER;
        revolverBulletMaxRange = revolverBulletMaxRange * RevolverBooster.REVOLVER_BULLET_MAX_RANGE_MULTIPLIER;
    }

    public String getName() {
        return name;
    }

    public double getRevolverBulletDamage() {
        return revolverBulletDamage;
    }

    public double getRevolverBulletSpeed() {
        return revolverBulletSpeed;
    }

    public double getRevolverBulletMaxRange() {
        return revolverBulletMaxRange;
    }

    public double getHp() {
        return hp;
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
