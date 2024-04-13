package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.movement.Movable;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.View;

public class ShotModel extends Model {
    public static final List<Model> items = new ArrayList<>();

    public ShotModel() {
        anchor = new Point2D.Double(EpsilonModel.items.get(0).anchor.getX(),
                EpsilonModel.items.get(0).anchor.getY());
        h = Constants.SHOT_DIAMETER;
        w = Constants.SHOT_DIAMETER;
        max_speed = Constants.SHOT_SPEED;
        isMoving = true;
        addItem(this);
        ShotView view = new ShotView(getId());
        view.setUtil(this);
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    public static Model findById(String Id) {
        return Model.findModel(Id, items);
    }

}
