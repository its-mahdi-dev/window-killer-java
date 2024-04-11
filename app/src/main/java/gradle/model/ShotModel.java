package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.movement.Movable;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.View;

public class ShotModel extends Model implements Movable {
    public static final List<Model> items = new ArrayList<>();
    Point2D direction;

    public ShotModel() {
        anchor = new Point2D.Double(EpsilonModel.items.get(0).anchor.getX(),
                EpsilonModel.items.get(0).anchor.getY());
        h = Constants.SHOT_DIAMETER;
        w = Constants.SHOT_DIAMETER;
        addItem(this);
        ShotView view = new ShotView(getId());
        view.setUtil(this);
    }

    public void setDirection(Point2D direction) {
        this.direction = direction;
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    @Override
    public void move(Point2D direction, double speed) {
        anchor = new Point2D.Double(anchor.getX() + direction.getX() * speed, anchor.getY() + direction.getY() * speed);
        View view = ShotView.findById(getId());
        view.setUtil(this);
    }

    @Override
    public void move() {
        move(direction, Constants.SHOT_SPEED);
    }

    public static Model findById(String Id) {
        return Model.findModel(Id, items);
    }

}
