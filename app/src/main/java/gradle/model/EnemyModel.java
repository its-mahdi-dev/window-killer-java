package gradle.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.*;
import javax.swing.Timer;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
import gradle.controller.Utils;
import gradle.interfaces.Collectible;
import gradle.interfaces.PolyganPoints;
import gradle.interfaces.Rotation;
import gradle.model.enemies.ArchmireEnemy;
import gradle.model.enemies.BarricadosMiniboss;
import gradle.model.enemies.BlackorbEnemy;
import gradle.model.enemies.NecropickEnemy;
import gradle.model.enemies.OmenoctEnemy;
import gradle.model.enemies.SquareEnemy;
import gradle.model.enemies.TriangleEnemy;
import gradle.model.enemies.WyrmEnemy;
import gradle.view.charecretsView.EnemyView;

public abstract class EnemyModel extends Entity implements Collectible, Rotation, PolyganPoints {
    public double[] xPoints;
    public double[] yPoints;
    public EnemyType type;
    public int collectibleXP;
    public int collectibleCount;
    public int HP;
    public double HP_time;

    public Map<String, Long> times = new HashMap<>();
    public Map<String, Integer> attacks = new HashMap<>();
    {
        attacks.put("melee", 0);
        attacks.put("ranged", 0);
        attacks.put("aoe", 0);
        attacks.put("drown", 0);
    }

    public EnemyModel() {
    }

    public void init(EnemyModel enemyModel, EnemyView enemyView) {
        enemyModel.addItem(enemyModel);
        enemyView.addItem(enemyView);
        enemyView.setUtil(enemyModel);
    }

    @Override
    public void setCollectible() {
        for (int i = 0; i < collectibleCount; i++) {
            CollectibleModel.create(type, collectibleXP,
                    new Point2D.Double(anchor.getX() + generateRandomDouble(),
                            anchor.getY() + generateRandomDouble()));
        }
    }

    public static double generateRandomDouble() {

        Random random = new Random();
        // Generate a random number to decide the range
        boolean isNegative = random.nextBoolean();

        if (isNegative) {
            // Generate a number between -100 and -20
            return -20 - (random.nextDouble() * (100 - 20));
        } else {
            // Generate a number between 20 and 100
            return 20 + (random.nextDouble() * (100 - 20));
        }
    }

    public abstract void setRelativePoints();

    @Override
    public void moveRotaion(double deg) {
        double rotationAngle = Math.toRadians(deg);
        angle += rotationAngle * speed;
        double x = anchor.getX();
        double y = anchor.getY();
        if (xPoints.length == 4) {
            xPoints = new double[] {
                    (x - w / 2 * Math.cos(angle) + h / 2 * Math.sin(angle)),
                    (x + w / 2 * Math.cos(angle) + h / 2 * Math.sin(angle)),
                    (x + w / 2 * Math.cos(angle) - h / 2 * Math.sin(angle)),
                    (x - w / 2 * Math.cos(angle) - h / 2 * Math.sin(angle))
            };
            yPoints = new double[] {
                    (y - w / 2 * Math.sin(angle) - h / 2 * Math.cos(angle)),
                    (y + w / 2 * Math.sin(angle) - h / 2 * Math.cos(angle)),
                    (y + w / 2 * Math.sin(angle) + h / 2 * Math.cos(angle)),
                    (y - w / 2 * Math.sin(angle) + h / 2 * Math.cos(angle))
            };
        } else if (xPoints.length == 3) {
            double d = Math.sqrt(3) / 2 * h;
            xPoints = new double[] {
                    (x + d * Math.cos(angle)),
                    (x + d * Math.cos(angle - Math.PI * 2 / 3)),
                    (x + d * Math.cos(angle + Math.PI * 2 / 3))
            };
            yPoints = new double[] {
                    (y + d * Math.sin(angle)),
                    (y + d * Math.sin(angle - Math.PI * 2 / 3)),
                    (y + d * Math.sin(angle + Math.PI * 2 / 3))
            };
        } else if (xPoints.length == 8) {
            double[] xPointsO = new double[8];
            double[] yPointsO = new double[8];
            for (int i = 0; i < 8; i++) {
                double newAngle = 2 * Math.PI * i / 8 + angle;
                xPointsO[i] = x + w * Math.cos(newAngle);
                yPointsO[i] = y + h * Math.sin(newAngle);
            }
            xPoints = xPointsO;
            yPoints = yPointsO;
        }
    }

    @Override
    public int[] getXpointsInt() {
        return Utils.getIntPoints(xPoints);
    }

    @Override
    public int[] getYpointsInt() {
        return Utils.getIntPoints(yPoints);
    }

    @Override
    public Map<String, int[]> getPanelPoints() {
        Point2D[] points = new Point2D[xPoints.length];
        int[] newXpoints = new int[xPoints.length];
        int[] newYpoints = new int[xPoints.length];
        for (int i = 0; i < xPoints.length; i++) {
            points[i] = Utils.getRelatedPoint(new Point2D.Double(xPoints[i], yPoints[i]),
                    currentPanels.get(0));
            newXpoints[i] = (int) points[i].getX();
            newYpoints[i] = (int) points[i].getY();
        }
        return Map.of("xPoints", newXpoints, "yPoints", newYpoints);
    }

    public static List<Model> getAllEnemies() {
        List<Model> enemies = new ArrayList<>();
        enemies.addAll(ArchmireEnemy.items);
        enemies.addAll(WyrmEnemy.items);
        enemies.addAll(NecropickEnemy.items);
        enemies.addAll(SquareEnemy.items);
        enemies.addAll(OmenoctEnemy.items);
        enemies.addAll(TriangleEnemy.items);
        enemies.addAll(BarricadosMiniboss.items);
        enemies.addAll(BlackorbEnemy.items);
        return enemies;
    }

    public static Model findById(String Id) {
        return Model.findModel(Id, getAllEnemies());
    }

    // public List<Model> getAllRemovedItems() {
    // return getRemovedItems();
    // }

    public abstract void removeUtils();
}
