package gradle.movement;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Direction {
    boolean isUpward = false;
    boolean isDownward = false;
    double slope;
    DirectionState state = DirectionState.neutral;

    public Direction(Point2D point) {
        if (point.getX() == 0 && point.getY() > 0)
            isUpward = true;
        else if (point.getX() == 0 && point.getY() < 0)
            isDownward = true;
        else if (point.getX() == 0)
            state = DirectionState.neutral;
        else {
            this.slope = point.getY() / point.getX();
            if (point.getX() > 0)
                this.state = DirectionState.positive;
            else
                this.state = DirectionState.negative;
        }
    }

    public Direction(double angle) {
        angle = angle - Math.floor(angle / 360) * 360;
        if (angle == 90)
            isUpward = true;
        if (angle == 270)
            isDownward = true;
        double x = Math.cos(Math.toRadians(angle));
        double y = Math.sin(Math.toRadians(angle));
        this.slope = y / x;
        if (angle >= 0 && angle < 180)
            state = DirectionState.positive;
        else
            state = DirectionState.negative;
    }

    public Point2D getDirectionVector() {
        if (state == DirectionState.neutral)
            return new Point(0, 0);
        if (isUpward)
            return new Point2D.Double(0, 1);
        if (isDownward)
            return new Point2D.Double(0, -1);
        double magnitude = Math.sqrt(1 + slope * slope);
        Point2D.Double normalVector = new Point2D.Double(1 / magnitude, slope / magnitude);
        if (state == DirectionState.negative)
            normalVector = new Point2D.Double(-normalVector.x, -normalVector.y);
        return normalVector;
    }

    public enum DirectionState {
        negative, positive, neutral
    }
}
