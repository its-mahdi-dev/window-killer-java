package gradle.model.enemies;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.enemies.WyrmEnemyView;

public class NecropickEnemy extends EnemyModel{
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();
    public NecropickEnemy(){

    }

    public static EnemyModel create(Point2D anchor) {
        EnemyModel enemyModel;
        WyrmEnemyView enemyView;

        if (removedItems.size() > 0) {
            enemyModel = (EnemyModel) removedItems.get(0);
            removedItems.remove(0);
            enemyView = (WyrmEnemyView) EnemyView.findView(enemyModel.getId(),
                    WyrmEnemyView.removedItems);
            WyrmEnemy.removedItems.removeIf(enemy -> enemy.getId() == enemyModel.getId());
        } else {
            enemyModel = new WyrmEnemy();
            enemyView = new WyrmEnemyView(enemyModel.getId(), enemyModel.type);
        }

        enemyModel.anchor = anchor;
        enemyModel.type = EnemyType.necropick;
        enemyModel.max_speed = Constants.ENEMY_SPEED + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = true;
        
        enemyModel.collectibleCount = 4;
        enemyModel.collectibleXP = 2;
        enemyModel.HP = 10;
        enemyModel.times.put("hovering", System.currentTimeMillis());
        enemyModel.times.put("gravity", System.currentTimeMillis());
        enemyModel.hovering = false;
        enemyModel.ableMove = false;

        enemyModel.setRelativePoints();
        enemyModel.init(enemyModel, enemyView);
        return enemyModel;
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

    @Override
    public void setRelativePoints() {
        double x = anchor.getX();
        double y = anchor.getY();
        double rotationAngle = Math.toRadians(0);
        w = Constants.ENEMY_NECRIPICK_WIDTH;
        h = Constants.ENEMY_NECRIPICK_HEIGHT;
        xPoints = new double[] {
                (x - w / 2 * Math.cos(rotationAngle) + h / 2 * Math.sin(rotationAngle)),
                (x + w / 2 * Math.cos(rotationAngle) + h / 2 * Math.sin(rotationAngle)),
                (x + w / 2 * Math.cos(rotationAngle) - h / 2 * Math.sin(rotationAngle)),
                (x - w / 2 * Math.cos(rotationAngle) - h / 2 * Math.sin(rotationAngle))
        };
        yPoints = new double[] {
                (y - w / 2 * Math.sin(rotationAngle) - h / 2 * Math.cos(rotationAngle)),
                (y + w / 2 * Math.sin(rotationAngle) - h / 2 * Math.cos(rotationAngle)),
                (y + w / 2 * Math.sin(rotationAngle) + h / 2 * Math.cos(rotationAngle)),
                (y - w / 2 * Math.sin(rotationAngle) + h / 2 * Math.cos(rotationAngle))
        };
    }
}
