package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Constants;
import gradle.movement.Movable;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.View;

public class EpsilonModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public EpsilonModel() {
        anchor = new Point2D.Double(100, 100);
        w = Constants.EPSILON_DIAMETER;
        h = Constants.EPSILON_DIAMETER;
        max_speed = Constants.MOVE_SPEED;
        isMoving = false;
        addItem(this);
        View view = new EpsilonView(getId());
        view.setUtil(this);
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    @Override
    protected List<Model> getRemovedItems() {
        return removedItems;
    }

}
