package ru.nsu.fit.kolesnik.notrealroyale.model.gameobject;

import javafx.geometry.Point2D;

import java.util.List;

public class Bot extends GameObject {
    private final static int WIDTH = 1;
    private final static int HEIGHT = 1;
    private final static double COLLIDABLE_RECT_PADDING_X = 0.1;
    private final static double COLLIDABLE_RECT_PADDING_Y = 0.1;
    private final static double VELOCITY = 0.05;

    private final double velocity;
    private boolean alive;

    public Bot(double x, double y) {
        super(x, y, WIDTH, HEIGHT, COLLIDABLE_RECT_PADDING_X, COLLIDABLE_RECT_PADDING_Y);
        velocity = VELOCITY;
        alive = true;
    }

    public void moveTowardsNearestPlayer(List<Player> playerList) {
        Player nearestPlayer = getNearestPlayerFromList(playerList);
        Point2D initialVectorToTarget = new Point2D(nearestPlayer.getX() - this.getX(), nearestPlayer.getY() - this.getY());
        Point2D velocityVector = initialVectorToTarget.normalize().multiply(this.getVelocity());
        moveX(velocityVector.getX());
        moveY(velocityVector.getY());
    }

    private Player getNearestPlayerFromList(List<Player> playerList) {
        Player nearestPlayer = null;
        double minDistanceToPlayer = Double.MAX_VALUE;
        for (Player player : playerList) {
            Point2D currentPosition = new Point2D(getX(), getY());
            Point2D playerPosition = new Point2D(player.getX(), player.getY());
            double distanceToPlayer = currentPosition.distance(playerPosition);
            if (distanceToPlayer < minDistanceToPlayer) {
                nearestPlayer = player;
                minDistanceToPlayer = distanceToPlayer;
            }
        }
        return nearestPlayer;
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
}
