package gradle.movement;

import java.awt.geom.Point2D;

public interface Movable {
    void setDirection(Point2D direction);

    void move(Point2D direction, double speed);

    void move();
}
