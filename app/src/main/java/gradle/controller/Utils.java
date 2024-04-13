package gradle.controller;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.ShotModel;

public class Utils {
    public static Point2D getDirection(Point2D point1, Point2D point2) {
        double angle = Math.atan2(point2.getY() - point1.getY(), point2.getX() - point1.getX());
        double xVelocity = Math.cos(angle);
        double yVelocity = Math.sin(angle);
        return new Point2D.Double(xVelocity, yVelocity);
    }

    public static boolean checkEpsilonShot(EnemyModel enemyModel, ShotModel shotModel) {
        Polygon polygon = new Polygon(enemyModel.getXpointsInt(), enemyModel.getYpointsInt(),
                enemyModel.getEnemyPointsNumber());
        if (polygon.contains(shotModel.anchor))
            return true;
        return false;
    }
}
