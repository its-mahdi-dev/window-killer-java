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
import gradle.view.charecretsView.EnemyView;

public class EnemyModel extends Entity implements Collectible, Rotation, PolyganPoints {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();
    public double[] xPoints;
    public double[] yPoints;
    public EnemyType type;
    public int power;
    public int collectibleXP;
    private int collectibleCount;
    public int HP;
    public double HP_time;
    public Map<String, Timer> timers = new HashMap<>();
    public Map<String, Long> times = new HashMap<>();
    public Map<String, Integer> attacks = new HashMap<>();
    {
        attacks.put("melee", 0);
        attacks.put("ranged", 0);
        attacks.put("aoe", 0);
    }

    public EnemyModel(Point2D anchor, EnemyType enemyType) {

    }

    public static EnemyModel create(Point2D anchor, EnemyType enemyType) {
        EnemyModel enemyModel;
        EnemyView enemyView;

        if (EnemyModel.removedItems.size() > 0) {
            enemyModel = (EnemyModel) EnemyModel.removedItems.get(0);
            EnemyModel.removedItems.remove(0);
            enemyView = (EnemyView) EnemyView.findView(enemyModel.getId(),
                    EnemyView.removedItems);
            EnemyView.removedItems.removeIf(enemy -> enemy.getId() == enemyModel.getId());
        } else {
            enemyModel = new EnemyModel(anchor, enemyType);
            enemyView = new EnemyView(enemyModel.getId(), enemyModel.type);
        }

        enemyModel.anchor = anchor;
        enemyModel.type = enemyType;
        double x = anchor.getX();
        double y = anchor.getY();
        enemyModel.max_speed = Constants.ENEMY_SPEED + (GameSettings.level / 5);
        enemyModel.impact_speed = 1.5;
        enemyModel.isMoving = true;

        if (enemyModel.type == EnemyType.square) {
            enemyModel.collectibleCount = 1;
            enemyModel.collectibleXP = 5;
            enemyModel.HP = 10;
            enemyModel.power = 6;
            enemyModel.attacks.replace("melee", 6);
        } else if (enemyModel.type == EnemyType.triangle) {
            enemyModel.collectibleCount = 2;
            enemyModel.collectibleXP = 5;
            enemyModel.HP = 15;
            enemyModel.power = 10;
            enemyModel.attacks.replace("melee", 10);
        } else if (enemyModel.type == EnemyType.omenoct) {
            enemyModel.collectibleCount = 8;
            enemyModel.collectibleXP = 4;
            enemyModel.HP = 20;
            enemyModel.power = 8;
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
        } else if (enemyModel.type == EnemyType.necropick) {
            enemyModel.collectibleCount = 4;
            enemyModel.collectibleXP = 2;
            enemyModel.HP = 10;
            enemyModel.power = 6;
            enemyModel.times.put("hovering", System.currentTimeMillis());
            enemyModel.times.put("gravity", System.currentTimeMillis());
            enemyModel.hovering = false;
            enemyModel.ableMove = false;
        }
        enemyModel.setRelativePoints();

        enemyModel.addItem(enemyModel);
        enemyView.addItem(enemyView);
        enemyView.setUtil(enemyModel);

        return enemyModel;
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    @Override
    protected List<Model> getRemovedItems() {
        return removedItems;
    }

    public static Model findById(String Id) {
        return Model.findModel(Id, items);
    }

    public int getEnemyPointsNumber() {
        int num = 3;
        if (type == EnemyType.square)
            num = 4;
        else if (type == EnemyType.triangle)
            num = 3;
        else if (type == EnemyType.omenoct)
            num = 8;
        return num;
    }

    @Override
    public void setCollectible() {
        int max = 200;
        int min = 50;
        int range = max - min + 1;
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

    public void setRelativePoints() {
        double x = anchor.getX();
        double y = anchor.getY();
        double rotationAngle = Math.toRadians(20);

        if (type == EnemyType.square) {
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
        } else if (type == EnemyType.triangle) {
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
        } else if (type == EnemyType.omenoct) {
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
        } else if (type == EnemyType.necropick) {
            w = Constants.ENEMY_NECRIPICN_WIDTH;
            h = Constants.ENEMY_NECRIPICN_HEIGHT;
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
        int[] points = new int[xPoints.length];
        for (int i = 0; i < xPoints.length; i++) {
            points[i] = (int) xPoints[i];
        }
        return points;
    }

    @Override
    public int[] getYpointsInt() {
        int[] points = new int[yPoints.length];
        for (int i = 0; i < yPoints.length; i++) {
            points[i] = (int) yPoints[i];
        }
        return points;
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

    

}
