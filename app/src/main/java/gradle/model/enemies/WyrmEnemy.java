package gradle.model.enemies;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class WyrmEnemy extends EnemyModel {

    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public WyrmEnemy() {

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
        enemyModel.type = EnemyType.wyrm;
        enemyModel.max_speed = Constants.ENEMY_SPEED + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = true;

        enemyModel.collectibleCount = 2;
        enemyModel.collectibleXP = 8;
        enemyModel.HP = 12;
        enemyModel.attacks.replace("ranged", 8);
        enemyModel.hovering = false;
        enemyModel.ableMove = true;
        enemyModel.clockwise = true;
        enemyModel.minRadius = Constants.WYRM_MIN_RADIUS;
        enemyModel.angleMove = 0;
        Timer shoTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShotModel shotModel = ShotModel.create(enemyModel.anchor, ShotType.enemy,
                        enemyModel.attacks.get("ranged"));
                shotModel.setDirection(Utils.getDirection(enemyModel.anchor, EpsilonModel.getINSTANCE().anchor));
                shotModel.rigid = false;
            }
        });
        enemyModel.timers.put("shotTimer", shoTimer);
        enemyModel.timers.get("shotTimer").start();

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
        w = Constants.ENEMY_WYRM_WIDTH;
        h = Constants.ENEMY_WYRM_HEIGHT;
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

    public void updatePosition() {
        if (ableMove) {
            // Update the angle based on the direction of rotation
            if (clockwise) {
                angleMove += max_speed / 2; // Clockwise rotation
            } else {
                angleMove -= max_speed / 2; // Counterclockwise rotation
            }

            // Calculate the new position using the angleMove and radius
            double x = EpsilonModel.getINSTANCE().anchor.getX() + minRadius * Math.cos(angleMove);
            double y = EpsilonModel.getINSTANCE().anchor.getY() + minRadius * Math.sin(angleMove);

            // Update the anchor position
            anchor = new Point2D.Double(x, y);
            // System.out.println(anchor);
        }
    }

}
