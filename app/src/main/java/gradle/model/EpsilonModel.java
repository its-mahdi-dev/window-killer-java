package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Constants;
import gradle.movement.Movable;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.View;

public class EpsilonModel extends Model implements Movable {
    public static final List<Model> items = new ArrayList<>();
    Point2D direction = new Point2D.Double(0, 0);

    public EpsilonModel() {
        anchor = new Point2D.Double(100, 100);
        w = Constants.EPSILON_DIAMETER;
        h = Constants.EPSILON_DIAMETER;
        addItem(this);
        View view = new EpsilonView(getId());
        view.setUtil(this);
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    public void setDirection(Point2D direction) {
        this.direction = direction;
    }

    @Override
    public void move(Point2D direction, double speed) {
        anchor = new Point2D.Double(anchor.getX() + direction.getX() * speed, anchor.getY() + direction.getY() * speed);
        View view = EpsilonView.findById(getId());
        view.setUtil(this);
    }

    @Override
    public void move() {
        move(direction, Constants.MOVE_SPEED);
    }
}
