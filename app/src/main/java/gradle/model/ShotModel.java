package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.movement.Direction;
import gradle.movement.Movable;
import gradle.view.charecretsView.ShotView;

public class ShotModel extends Model implements Movable{
    public static final List<Model> items = new ArrayList<>();
    Direction direction;
    public ShotModel() {
        anchor = new Point2D.Double(EpsilonModel.items.get(0).anchor.getX() , EpsilonModel.items.get(0).anchor.getY());
        h = EpsilonModel.items.get(0).h;
        w = EpsilonModel.items.get(0).w;
        addItem(this);
        ShotView view = new ShotView(getId());
        view.setUtil(this);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    @Override
    public void move(Direction direction, double speed) {
        Point2D movement=Utils.multiplyVector(direction.getDirectionVector(),speed);
        this.anchor=Utils.addVectors(anchor,movement);
    }

    @Override
    public void move() {
        move(direction,Constants.SPEED);
    }
}
