package gradle.model.enemies;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
import gradle.controller.Utils;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.EpsilonModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.model.ShotType;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.enemies.WyrmEnemyView;

public class TriangleEnemy extends EnemyModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public TriangleEnemy() {

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
        enemyModel.type = EnemyType.triangle;
        enemyModel.max_speed = Constants.ENEMY_SPEED + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = true;

        enemyModel.collectibleCount = 2;
        enemyModel.collectibleXP = 5;
        enemyModel.HP = 15;
        enemyModel.attacks.replace("melee", 10);

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
        w = Constants.ENEMY_TRIANGLE_DIAMETER;
        h = Constants.ENEMY_TRIANGLE_DIAMETER;
        double d = Math.sqrt(3) / 2 * h;
        xPoints = new double[] {
                (x + d * Math.cos(rotationAngle)),
                (x + d * Math.cos(rotationAngle - Math.PI * 2 / 3)),
                (x + d * Math.cos(rotationAngle + Math.PI * 2 / 3))
        };
        yPoints = new double[] {
                (y + d * Math.sin(rotationAngle)),
                (y + d * Math.sin(rotationAngle - Math.PI * 2 / 3)),
                (y + d * Math.sin(rotationAngle + Math.PI * 2 / 3))
        };
    }
}
