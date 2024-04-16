package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.View;

public class ShotModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public ShotModel() {
    }

    public static ShotModel create() {
        ShotModel shotModel;
        ShotView shotView;
        if (ShotModel.removedItems.size() > 0) {
            shotModel = (ShotModel) ShotModel.removedItems.get(0);
            shotView = (ShotView) ShotView.findById(shotModel.getId(),
                    ShotView.removedItems);
            ShotView.removedItems.remove(shotView);
            ShotModel.removedItems.remove(shotModel);

        } else {
            shotModel = new ShotModel();
            shotView = new ShotView(shotModel.getId());
        }

        shotModel.h = Constants.SHOT_DIAMETER;
        shotModel.w = Constants.SHOT_DIAMETER;
        shotModel.max_speed = Constants.SHOT_SPEED;

        shotModel.anchor = EpsilonModel.items.get(0).anchor;
        shotView.addItem(shotView);
        shotModel.addItem(shotModel);
        shotView.setUtil(shotModel);

        return shotModel;
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    @Override
    protected List<Model> getRemovedItems() {
        return removedItems;
    }

    public static Model findById(String Id) {
        return Model.findModel(Id, items);
    }

}
