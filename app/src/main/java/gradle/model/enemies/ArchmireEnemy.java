package gradle.model.enemies;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.enemies.ArchmireEnemyView;
import gradle.view.charecretsView.enemies.WyrmEnemyView;

public class ArchmireEnemy extends EnemyModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public Queue<Polygon> pathHistory = new LinkedList<>();
    int maxSteps;
    GeometryFactory geometryFactory = new GeometryFactory();

    long lastPathUpdate = System.currentTimeMillis();

    public ArchmireEnemy() {

    }

    public static EnemyModel create(Point2D anchor) {
        ArchmireEnemy enemyModel;
        ArchmireEnemyView enemyView;

        if (removedItems.size() > 0) {
            enemyModel = (ArchmireEnemy) removedItems.get(0);
            removedItems.remove(0);
            enemyView = (ArchmireEnemyView) EnemyView.findView(enemyModel.getId(),
                    ArchmireEnemyView.removedItems);
            ArchmireEnemy.removedItems.removeIf(enemy -> enemy.getId() == enemyModel.getId());
        } else {
            enemyModel = new ArchmireEnemy();
            enemyView = new ArchmireEnemyView(enemyModel.getId(), enemyModel.type);
        }
        enemyModel.pathHistory = new LinkedList<>();
        enemyModel.anchor = anchor;
        enemyModel.type = EnemyType.archmire;
        enemyModel.max_speed = Constants.ENEMY_SPEED / 2 + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = true;

        enemyModel.collectibleCount = 5;
        enemyModel.collectibleXP = 6;
        enemyModel.HP = 30;
        // enemyModel.attacks.replace("ranged", 8);
        enemyModel.hovering = true;
        enemyModel.ableMove = true;
        ((ArchmireEnemy) enemyModel).maxSteps = 5 * 20;
        enemyModel.attacks.replace("aoe", 2);
        enemyModel.attacks.replace("drown", 10);
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
        w = Constants.ENEMY_ARCHMIRE_WIDTH;
        h = Constants.ENEMY_ARCHMIRE_HEIGHT;
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

    public Polygon getCurrentPolygon() {
        Coordinate[] coordinates = new Coordinate[xPoints.length + 1];
        for (int i = 0; i < xPoints.length; i++) {
            coordinates[i] = new Coordinate(xPoints[i], yPoints[i]);
        }
        // Close the polygon by repeating the first coordinate
        coordinates[xPoints.length] = new Coordinate(xPoints[0], yPoints[0]);
        return geometryFactory.createPolygon(coordinates);
    }

    public void updatePathHistory() {
        if (System.currentTimeMillis() - lastPathUpdate < 50)
            return;
        lastPathUpdate = System.currentTimeMillis();
        Polygon currentPolygon = getCurrentPolygon();
        synchronized (pathHistory) {
            pathHistory.add(currentPolygon);
            // Ensure the path history contains only the last 5 seconds of movement
            if (pathHistory.size() > maxSteps) {
                pathHistory.poll();
            }
        }
    }

    @Override
    public void removeUtils() {

    }
}
