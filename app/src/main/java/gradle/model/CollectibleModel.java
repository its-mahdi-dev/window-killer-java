package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.interfaces.Collectible;
import gradle.view.charecretsView.CollectibleView;

public class CollectibleModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();
    public EnemyType enemyType;
    public int xp;
    public double time;

    public CollectibleModel(Point2D anchor) {
    }

    public static CollectibleModel create(EnemyType type, int xp, Point2D anchor) {
        CollectibleModel collectibleModel;
        CollectibleView collectibleView = null;
        if (CollectibleModel.removedItems.size() > 0) {
            collectibleModel = (CollectibleModel) CollectibleModel.removedItems.get(0);
            CollectibleModel.removedItems.remove(collectibleModel);
            collectibleView = (CollectibleView) CollectibleView.findView(collectibleModel.getId(),
                    CollectibleView.removedItems);
            CollectibleView.removedItems.removeIf(collectible -> collectible.getId() == collectibleModel.getId());

        } else {
            collectibleModel = new CollectibleModel(anchor);
            collectibleView = new CollectibleView(collectibleModel.getId(), type);
        }

        collectibleModel.xp = xp;
        collectibleModel.enemyType = type;
        collectibleModel.w = Constants.COLLECTIBLE_DIAMETER;
        collectibleModel.h = Constants.COLLECTIBLE_DIAMETER;
        collectibleModel.anchor = anchor;
        collectibleModel.time = System.currentTimeMillis();
        collectibleView.setUtil(collectibleModel);
        collectibleView.addItem(collectibleView);
        collectibleModel.addItem(collectibleModel);

        return collectibleModel;
    }

    // public static void removeEnemyCollectibles(EnemyModel enemyModel) {
    // for (int i = 0; i < CollectibleModel.items.size(); i++) {
    // CollectibleModel collectibleModel = (CollectibleModel)
    // CollectibleModel.items.get(i);
    // if (collectibleModel.enemyModel.equals(enemyModel)) {
    // CollectibleModel.removedItems.add(collectibleModel);
    // CollectibleModel.items.remove(collectibleModel);
    // }
    // }
    // }

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
