package gradle.model;

import java.util.ArrayList;
import java.util.List;

import java.awt.geom.Point2D;
import gradle.controller.Constants;
import gradle.model.enemies.ArchmireEnemy;
import gradle.view.Panels;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.enemies.SquareEnemyView;
import gradle.view.charecretsView.enemies.TriangleEnemyView;

public class ShotModel extends Entity {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public ShotType shotType;
    public int power;

    public ShotModel() {
    }

    public static ShotModel create(Point2D anchor, ShotType type, int power) {
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

        if (type == ShotType.epsilon)
            shotModel.max_speed = Constants.SHOT_SPEED;
        else if (type == ShotType.enemy)
            shotModel.max_speed = Constants.ENEMY_SHOT_SPEED;

        shotModel.anchor = anchor;
        shotModel.shotType = type;
        shotModel.power = power;
        shotModel.currentPanels = EpsilonModel.getINSTANCE().currentPanels;
        shotView.addItem(shotView);
        shotModel.addItem(shotModel);
        shotView.setUtil(shotModel);

        return shotModel;
    }

    @Override
    public List<Model> getItems() {
        return items;
    }

    @Override
    public List<Model> getRemovedItems() {
        return removedItems;
    }

    public static Model findById(String Id) {
        return Model.findModel(Id, items);
    }

}
