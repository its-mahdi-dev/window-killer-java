package gradle.model.enemies;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import gradle.threads.GamePanelThread;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.enemies.BarricadosMinibossView;
import gradle.view.charecretsView.enemies.WyrmEnemyView;

public class BarricadosMiniboss extends EnemyModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public GamePanel panel;

    public BarricadosMiniboss() {

    }

    public static EnemyModel create(Point2D anchor) {
        BarricadosMiniboss enemyModel;
        BarricadosMinibossView enemyView;

        if (removedItems.size() > 0) {
            enemyModel = (BarricadosMiniboss) removedItems.get(0);
            removedItems.remove(0);
            enemyView = (BarricadosMinibossView) EnemyView.findView(enemyModel.getId(),
                    BarricadosMinibossView.removedItems);
            BarricadosMiniboss.removedItems.removeIf(enemy -> enemy.getId() == enemyModel.getId());
        } else {
            enemyModel = new BarricadosMiniboss();
            enemyView = new BarricadosMinibossView(enemyModel.getId(), enemyModel.type);
        }

        enemyModel.anchor = anchor;
        enemyModel.type = EnemyType.barricados;
        enemyModel.max_speed = Constants.ENEMY_SPEED + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = false;

        enemyModel.collectibleCount = 0;
        enemyModel.collectibleXP = 0;
        enemyModel.HP = 12;
        enemyModel.hovering = false;
        enemyModel.ableMove = false;
        enemyModel.setRelativePoints();

        GamePanel gamePanel = new GamePanel();
        enemyModel.panel = gamePanel;
        enemyModel.setPanelAnchor();
        enemyModel.panel.setSize(enemyModel.w + 20, enemyModel.h + 20);
        enemyModel.panel.isometric = true;
        Random random = new Random();
        int rand = random.nextInt(2);
        if (rand == 0)
            enemyModel.panel.rigid = true;
        Panels.getINSTANCE().addPanel(enemyModel.panel);
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
        w = Constants.MINIBOSS_BARRICADOS_WIDTH;
        h = Constants.MINIBOSS_BARRICADOS_HEIGHT;
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
        // setPanelAnchor();
    }

    private void setPanelAnchor() {
        if (panel != null)
            panel.setLocation((int) anchor.getX() - w / 2 - 10, (int) anchor.getY() - h / 2 - 10);
    }

    @Override
    public void removeUtils() {
        Panels.getINSTANCE().removePanel(panel);
    }

}
