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
    public EnemyModel enemyModel;

    public CollectibleModel(String EnemyId, Point2D anchor) {
    }

    public static CollectibleModel create(String EnemyId, Point2D anchor) {
        CollectibleModel collectibleModel;
        CollectibleView collectibleView = null;
        if (CollectibleModel.removedItems.size() > 0) {
            collectibleModel = (CollectibleModel) CollectibleModel.removedItems.get(0);
            CollectibleModel.removedItems.remove(collectibleModel);
            collectibleView = (CollectibleView) CollectibleView.findView(collectibleModel.getId(),
                    CollectibleView.removedItems);
            CollectibleView.removedItems.removeIf(collectible -> collectible.getId() == collectibleModel.getId());

        } else {
            collectibleModel = new CollectibleModel(EnemyId, anchor);
            collectibleView = new CollectibleView(collectibleModel.getId(), collectibleModel.enemyModel.type);
        }
        collectibleModel.w = Constants.COLLECTIBLE_DIAMETER;
        collectibleModel.h = Constants.COLLECTIBLE_DIAMETER;
        collectibleModel.anchor = anchor;
        collectibleModel.enemyModel = (EnemyModel) EnemyModel.findById(EnemyId);
        collectibleView.addItem(collectibleView);
        collectibleView.setUtil(collectibleModel);
        collectibleModel.addItem(collectibleModel);

        return collectibleModel;
    }

    public static void removeEnemyCollectibles(EnemyModel enemyModel) {
        for (int i = 0; i < CollectibleModel.items.size(); i++) {
            CollectibleModel collectibleModel = (CollectibleModel) CollectibleModel.items.get(i);
            if (collectibleModel.enemyModel.equals(enemyModel)) {
                CollectibleModel.removedItems.add(collectibleModel);
                CollectibleModel.items.remove(collectibleModel);
            }
        }
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
