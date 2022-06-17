package ru.nsu.fit.kolesnik.notrealroyale;

import javafx.geometry.Point2D;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.ActiveObject;
import ru.nsu.fit.kolesnik.notrealroyale.model.gameobject.Player;

import java.util.List;

public class Enemy extends ActiveObject {
    public final static double DEFAULT_VELOCITY = 0.05;
    public final static int DEFAULT_WIDTH = 1;
    public final static int DEFAULT_HEIGHT = 1;

    public Enemy(double x, double y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_VELOCITY);
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
}
