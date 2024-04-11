package gradle.movement;

public interface Movable {
    void setDirection(Direction direction);
    void move(Direction direction,double speed);
    void move();
}
