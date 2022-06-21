package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

public class ActiveObject extends GameObject {
    private final double velocity;
    private boolean alive;

    public ActiveObject(double x, double y, double width, double height, double collidableRectPaddingX, double collidableRectPaddingY, double velocity) {
        super(x, y, width, height, collidableRectPaddingX, collidableRectPaddingY);
        this.velocity = velocity;
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

    public double getVelocity() {
        return velocity;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isColliding(GameObject other) {
        if (this == other) {
            return false;
        }
        if (getX() < other.getX() + other.getWidth() &&
                getX() + getWidth() > other.getX() &&
                getY() < other.getY() + other.getHeight() &&
                getY() + getHeight() > other.getY()) {
            return true;
        }
        return false;
    }
}
