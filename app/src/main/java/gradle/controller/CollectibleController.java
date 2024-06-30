package gradle.controller;

import gradle.interfaces.UPSController;
import gradle.model.CollectibleModel;
import gradle.model.EnemyType;
import gradle.model.EpsilonModel;
import gradle.view.charecretsView.CollectibleView;

public class CollectibleController implements UPSController {

    @Override
    public void check() {
        
        for (int i = 0; i < CollectibleModel.items.size(); i++) {
            checkCollectibleCollision((CollectibleModel) CollectibleModel.items.get(i));
        }
        for (int i = 0; i < CollectibleView.items.size(); i++) {
            CollectibleView collectibleView = (CollectibleView) CollectibleView.items.get(i);
            CollectibleModel collectibleModel = (CollectibleModel) CollectibleModel.findById(collectibleView.getId());
            if (collectibleModel != null)
                collectibleView.setUtil(collectibleModel);
        }
    }

     public static void checkCollectibleCollision(CollectibleModel collectibleModel) {

        if (Utils.getDistance(EpsilonModel.getINSTANCE().anchor, collectibleModel.anchor) < EpsilonModel.getINSTANCE().w
                / 2) {
            EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
            epsilonModel.XP += collectibleModel.xp;
            removeCollectible(collectibleModel.getId());
        }

        if (System.currentTimeMillis() - collectibleModel.time > 10000)
            removeCollectible(collectibleModel.getId());
    }

    public static void removeCollectible(String Id) {

        CollectibleModel collectibleModel = (CollectibleModel) CollectibleModel.findById(Id);
        if (collectibleModel.enemyType == EnemyType.square)
            Utils.playMusic("squareCollectible", false);
        else if (collectibleModel.enemyType == EnemyType.triangle)
            Utils.playMusic("triangleCollectible", false);
        CollectibleModel.removedItems.add(collectibleModel);
        CollectibleView.removedItems.add(CollectibleView.findById(collectibleModel.getId()));
        CollectibleModel.items.remove(collectibleModel);
        CollectibleView.items.removeIf(collectible -> collectible.getId() == collectibleModel.getId());
    }
    public static void removeAllCollectible() {
        for (int i = CollectibleModel.items.size() - 1; i >= 0; i--) {
            removeCollectible(CollectibleModel.items.get(i).getId());
        }
    }

}
