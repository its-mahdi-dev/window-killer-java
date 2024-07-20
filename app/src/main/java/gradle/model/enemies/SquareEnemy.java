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
import gradle.view.charecretsView.enemies.SquareEnemyView;
import gradle.view.charecretsView.enemies.WyrmEnemyView;

public class SquareEnemy extends EnemyModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public SquareEnemy() {

    }

    public static EnemyModel create(Point2D anchor) {
        EnemyModel enemyModel;
        SquareEnemyView enemyView;

        if (removedItems.size() > 0) {
            enemyModel = (EnemyModel) removedItems.get(0);
            removedItems.remove(0);
            enemyView = (SquareEnemyView) EnemyView.findView(enemyModel.getId(),
                    SquareEnemyView.removedItems);
            SquareEnemy.removedItems.removeIf(enemy -> enemy.getId() == enemyModel.getId());
        } else {
            enemyModel = new SquareEnemy();
            enemyView = new SquareEnemyView(enemyModel.getId(), enemyModel.type);
        }

        enemyModel.anchor = anchor;
        enemyModel.type = EnemyType.square;
        enemyModel.max_speed = Constants.ENEMY_SPEED + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = true;
        enemyModel.collectibleCount = 1;
        enemyModel.collectibleXP = 5;
        enemyModel.HP = 10;
        enemyModel.attacks.replace("melee", Long.valueOf(6));

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
        double rotationAngle = angle;
        w = Constants.ENEMY_SQUARE_DIAMETER;
        h = Constants.ENEMY_SQUARE_DIAMETER;
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

    @Override
    public void removeUtils() {
        
    }
}
