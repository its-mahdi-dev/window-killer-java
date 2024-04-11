package gradle.controller;

import java.awt.geom.Point2D;

public class Utils {
    public static Point2D getDirection(Point2D point1, Point2D point2) {
        double angle = Math.atan2(point2.getY() - point1.getY(), point2.getX() - point1.getX());
        double xVelocity = Math.cos(angle);
        double yVelocity = Math.sin(angle);
        return new Point2D.Double(xVelocity, yVelocity);
    }
}
