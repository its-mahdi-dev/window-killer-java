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


    public static double getDistance(Point2D point1, Point2D point2, Point2D point3) {
        double m = (point2.getY() - point1.getY()) / (point2.getX() - point1.getX());
        double c = point1.getY() - m * point1.getX();
        double distance = Math.abs(m * point3.getX() - point3.getY() + c) / Math.sqrt(m * m + 1);
        return distance;

    }

    public static Point2D[] getNearestPoints(double[] xPoints, double[] yPoints, Point2D point) {
        Point2D[] nearestPoints = new Point2D[2];
        double minDistance = 999999999;
        int index1 = 0;
        for (int i = 0; i < xPoints.length; i++) {
            double distance = Math
                    .sqrt(Math.pow(point.getX() - xPoints[i], 2) + Math.pow(point.getY() - yPoints[i], 2));
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoints[0] = new Point2D.Double(xPoints[i], yPoints[i]);
                index1 = i;
            }
        }
        minDistance = 999999999;
        for (int i = 0; i < xPoints.length; i++) {
            double distance = Math
                    .sqrt(Math.pow(point.getX() - xPoints[i], 2) + Math.pow(point.getY() - yPoints[i], 2));
            if (distance < minDistance && i != index1) {
                minDistance = distance;
                nearestPoints[1] = new Point2D.Double(xPoints[i], yPoints[i]);
            }
        }
        return nearestPoints;

    }
}
