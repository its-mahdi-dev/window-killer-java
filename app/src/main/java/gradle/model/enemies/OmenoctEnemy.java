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

public class OmenoctEnemy extends EnemyModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public OmenoctEnemy() {

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
        enemyModel.type = EnemyType.omenoct;
        enemyModel.max_speed = Constants.ENEMY_SPEED + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = true;

        enemyModel.collectibleCount = 8;
        enemyModel.collectibleXP = 4;
        enemyModel.HP = 20;
        enemyModel.attacks.replace("melee", 8);
        enemyModel.attacks.replace("ranged", 4);
        Timer shoTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShotModel shotModel = ShotModel.create(enemyModel.anchor, ShotType.enemy,
                        enemyModel.attacks.get("ranged"));
                shotModel.setDirection(Utils.getDirection(enemyModel.anchor, EpsilonModel.getINSTANCE().anchor));
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
        w = Constants.ENEMY_OMENOCT_DIAMETER;
        h = Constants.ENEMY_OMENOCT_DIAMETER;
        double[] xPointsO = new double[8];
        double[] yPointsO = new double[8];
        for (int i = 0; i < 8; i++) {
            double angle = 2 * Math.PI * i / 8 + rotationAngle;
            xPointsO[i] = x + w * Math.cos(angle);
            yPointsO[i] = y + h * Math.sin(angle);
        }
        xPoints = xPointsO;
        yPoints = yPointsO;
    }
}
