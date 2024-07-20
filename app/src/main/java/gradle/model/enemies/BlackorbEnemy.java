package gradle.model.enemies;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import org.checkerframework.checker.units.qual.radians;

import gradle.controller.Constants;
import gradle.controller.EnemyController;
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
import gradle.view.charecretsView.enemies.BlackorbEnemyView;
import gradle.view.charecretsView.enemies.WyrmEnemyView;

public class BlackorbEnemy extends EnemyModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public List<GamePanel> panels = new LinkedList<>();
    public List<Point2D> orbs = new ArrayList<>();
    public double radius;
    public boolean isCreating = true;
    public List<Map<String, double[]>> lasers = new LinkedList<>();

    public BlackorbEnemy() {
        saveIgnore.add("lasers");

    }

    public static EnemyModel create(Point2D anchor) {
        BlackorbEnemy enemyModel;
        BlackorbEnemyView enemyView;

        if (removedItems.size() > 0) {
            enemyModel = (BlackorbEnemy) removedItems.get(0);
            removedItems.remove(0);
            enemyView = (BlackorbEnemyView) EnemyView.findView(enemyModel.getId(),
                    BlackorbEnemyView.removedItems);
            BlackorbEnemy.removedItems.removeIf(enemy -> enemy.getId() == enemyModel.getId());
        } else {
            enemyModel = new BlackorbEnemy();
            enemyView = new BlackorbEnemyView(enemyModel.getId(), enemyModel.type);
        }

        enemyModel.anchor = anchor;
        enemyModel.type = EnemyType.blackorb;
        enemyModel.max_speed = Constants.ENEMY_SPEED + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = false;

        enemyModel.collectibleCount = 5;
        enemyModel.collectibleXP = 30;
        enemyModel.HP = 30;
        enemyModel.hovering = false;
        enemyModel.ableMove = false;
        enemyModel.attacks.put("laser", Long.valueOf(2));
        enemyModel.xPoints = new double[0];
        enemyModel.yPoints = new double[0];
        List<Point2D> points = Utils.generateSymmetricPoints(enemyModel.anchor, 5, 1);
        enemyModel.orbs = points;

        enemyModel.setRelativePoints();

        for (Point2D orb : enemyModel.orbs) {
            GamePanel gamePanel = new GamePanel();
            gamePanel.setSize(enemyModel.w + 20, enemyModel.h + 20);
            gamePanel.isometric = true;
            gamePanel.rigid = false;
            enemyModel.panels.add(gamePanel);
            Panels.getINSTANCE().addPanel(gamePanel);
        }

        enemyModel.setPanelAnchor();
        Timer initTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enemyModel.radius + enemyModel.velocity <= Constants.MINIBOSS_BLACKORB_SIDE) {
                    // enemyModel.speed += enemyModel.velocity;
                    enemyModel.radius += enemyModel.velocity * 10;
                    List<Point2D> points = Utils.generateSymmetricPoints(enemyModel.anchor, 5, enemyModel.radius);
                    enemyModel.orbs = points;
                    enemyModel.setRelativePoints();
                    enemyModel.setPanelAnchor();
                } else {
                    enemyModel.isCreating = false;
                }

            }
        });
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            initTimer.start();
        }, 3, TimeUnit.SECONDS);

        executor.shutdown();
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
        xPoints = new double[orbs.size()];
        yPoints = new double[orbs.size()];
        // double x = anchor.getX();
        // double y = anchor.getY();
        // double rotationAngle = Math.toRadians(0);
        w = Constants.MINIBOSS_BLACKORB_DIAMETER;
        h = Constants.MINIBOSS_BLACKORB_DIAMETER;

        List<Map<String, double[]>> newLasers = new LinkedList<>();
        for (int i = 0; i < orbs.size() - 1; i++) {
            for (int j = i + 1; j < orbs.size(); j++) {
                Point2D center1 = orbs.get(i);
                Point2D center2 = orbs.get(j);
                double dx = center2.getX() - center1.getX();
                double dy = center2.getY() - center1.getY();
                double length = Math.sqrt(dx * dx + dy * dy);

                // Normalize the direction vector
                double ux = dx / length;
                double uy = dy / length;

                // Calculate the perpendicular vector
                double px = -uy;
                double py = ux;

                double height = 10.0;

                double[] newXpoints = new double[4];
                double[] newYpoints = new double[4];

                newXpoints[0] = (center1.getX() + height * px / 2);
                newYpoints[0] = (center1.getY() + height * py / 2);

                newXpoints[1] = (center1.getX() - height * px / 2);
                newYpoints[1] = (center1.getY() - height * py / 2);

                newXpoints[2] = (center2.getX() - height * px / 2);
                newYpoints[2] = (center2.getY() - height * py / 2);

                newXpoints[3] = (center2.getX() + height * px / 2);
                newYpoints[3] = (center2.getY() + height * py / 2);

                newLasers.add(Map.of("x", newXpoints, "y", newYpoints));
            }
        }
        lasers = newLasers;

        // setPanelAnchor();
    }

    public List<Polygon> getOrbsPolygon() {
        List<Polygon> polygons = new ArrayList<>();
        for (Map<String, double[]> laser : lasers) {
            polygons.add(new Polygon(Utils.getIntPoints(laser.get("x")), Utils.getIntPoints(laser.get("y")), 4));
        }
        return polygons;
    }

    private void setPanelAnchor() {
        for (int i = 0; i < orbs.size(); i++)
            panels.get(i).setLocation((int) orbs.get(i).getX() - w / 2 - 10, (int) orbs.get(i).getY() - h / 2 - 10);
    }

    @Override
    public void removeUtils() {
        for (GamePanel panel : panels)
            Panels.getINSTANCE().removePanel(panel);
    }

}
