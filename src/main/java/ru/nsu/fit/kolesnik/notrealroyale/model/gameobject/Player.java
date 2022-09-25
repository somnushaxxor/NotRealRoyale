package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class Player extends GameObject {

    private final static double MAX_HP = 100;
    private final static double DEFAULT_VELOCITY = 0.05;
    private final static double WIDTH = 1;
    private final static double HEIGHT = 1;
    private final static double COLLIDABLE_RECT_PADDING_X = 0.15;
    private final static double COLLIDABLE_RECT_PADDING_Y = 0.15;
    private final static double DEFAULT_REVOLVER_BULLET_DAMAGE = 10;
    private final static double DEFAULT_REVOLVER_BULLET_SPEED = 0.35;
    private final static double DEFAULT_REVOLVER_BULLET_MAX_RANGE = 15;


    private double hp;
    private int score;
    private int revolverLevel;
    private boolean alive;
    private double revolverBulletDamage;
    private double revolverBulletSpeed;
    private double revolverBulletMaxRange;
    private int healingSalvesNumber;
    private final String name;
    private final double velocity;

    public Player(String name, double x, double y) {
        super(x, y, WIDTH, HEIGHT, COLLIDABLE_RECT_PADDING_X, COLLIDABLE_RECT_PADDING_Y);
        hp = MAX_HP;
        score = 0;
        revolverLevel = 1;
        this.name = name;
        revolverBulletDamage = DEFAULT_REVOLVER_BULLET_DAMAGE;
        revolverBulletMaxRange = DEFAULT_REVOLVER_BULLET_MAX_RANGE;
        revolverBulletSpeed = DEFAULT_REVOLVER_BULLET_SPEED;
        healingSalvesNumber = 0;
        velocity = DEFAULT_VELOCITY;
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

    public void receiveDamage(double damage) {
        hp -= damage;
        if (hp <= 0) {
            setAlive(false);
        }
    }

    public void pickHealingSalveUp() {
        healingSalvesNumber++;
    }

    public void useHealingSalve() {
        if (healingSalvesNumber > 0) {
            if (hp < MAX_HP - HealingSalve.REGENERATABLE_HP) {
                hp += HealingSalve.REGENERATABLE_HP;
            } else {
                hp = MAX_HP;
            }
            healingSalvesNumber--;
        }
    }

    public void powerUpRevolver() {
        revolverBulletDamage = revolverBulletDamage * RevolverBooster.REVOLVER_BULLET_DAMAGE_MULTIPLIER;
        revolverBulletSpeed = revolverBulletSpeed * RevolverBooster.REVOLVER_BULLET_SPEED_MULTIPLIER;
        revolverBulletMaxRange = revolverBulletMaxRange * RevolverBooster.REVOLVER_BULLET_MAX_RANGE_MULTIPLIER;
        revolverLevel++;
    }

    public void score() {
        score++;
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

    public double getVelocity() {
        return velocity;
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

    public int getRevolverLevel() {
        return revolverLevel;
    }

    public int getHealingSalvesNumber() {
        return healingSalvesNumber;
    }

    public int getScore() {
        return score;
    }

}
