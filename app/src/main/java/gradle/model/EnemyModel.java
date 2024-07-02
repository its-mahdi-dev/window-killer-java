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
import gradle.interfaces.Entity;
import gradle.interfaces.Rotation;
import gradle.view.charecretsView.EnemyView;

public class EnemyModel extends Model implements Collectible, Rotation, Entity {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public EnemyType type;
    public int power;
    public int collectibleXP;
    private int collectibleCount;
    Timer shotTimer;
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
        
        System.out.println("aaa");
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
        double rotationAngle = Math.toRadians(20);

        if (enemyModel.type == EnemyType.square) {
            enemyModel.collectibleCount = 1;
            enemyModel.collectibleXP = 5;
            enemyModel.HP = 10;
            enemyModel.power = 6;
            enemyModel.w = Constants.ENEMY_SQUARE_DIAMETER;
            enemyModel.h = Constants.ENEMY_SQUARE_DIAMETER;
            enemyModel.xPoints = new double[] {
                    (x - enemyModel.w / 2 * Math.cos(rotationAngle) + enemyModel.h / 2 * Math.sin(rotationAngle)),
                    (x + enemyModel.w / 2 * Math.cos(rotationAngle) + enemyModel.h / 2 * Math.sin(rotationAngle)),
                    (x + enemyModel.w / 2 * Math.cos(rotationAngle) - enemyModel.h / 2 * Math.sin(rotationAngle)),
                    (x - enemyModel.w / 2 * Math.cos(rotationAngle) - enemyModel.h / 2 * Math.sin(rotationAngle))
            };
            enemyModel.yPoints = new double[] {
                    (y - enemyModel.w / 2 * Math.sin(rotationAngle) - enemyModel.h / 2 * Math.cos(rotationAngle)),
                    (y + enemyModel.w / 2 * Math.sin(rotationAngle) - enemyModel.h / 2 * Math.cos(rotationAngle)),
                    (y + enemyModel.w / 2 * Math.sin(rotationAngle) + enemyModel.h / 2 * Math.cos(rotationAngle)),
                    (y - enemyModel.w / 2 * Math.sin(rotationAngle) + enemyModel.h / 2 * Math.cos(rotationAngle))
            };
            enemyModel.attacks.replace("melee", 6);
        } else if (enemyModel.type == EnemyType.triangle) {
            enemyModel.collectibleCount = 2;
            enemyModel.collectibleXP = 5;
            enemyModel.HP = 15;
            enemyModel.power = 10;
            enemyModel.w = Constants.ENEMY_TRIANGLE_DIAMETER;
            enemyModel.h = Constants.ENEMY_TRIANGLE_DIAMETER;
            double d = Math.sqrt(3) / 2 * enemyModel.h;
            enemyModel.xPoints = new double[] {
                    (x + d * Math.cos(rotationAngle)),
                    (x + d * Math.cos(rotationAngle - Math.PI * 2 / 3)),
                    (x + d * Math.cos(rotationAngle + Math.PI * 2 / 3))
            };
            enemyModel.yPoints = new double[] {
                    (y + d * Math.sin(rotationAngle)),
                    (y + d * Math.sin(rotationAngle - Math.PI * 2 / 3)),
                    (y + d * Math.sin(rotationAngle + Math.PI * 2 / 3))
            };

            enemyModel.attacks.replace("melee", 10);
        } else if (enemyModel.type == EnemyType.omenoct) {
            enemyModel.collectibleCount = 8;
            enemyModel.collectibleXP = 4;
            enemyModel.HP = 20;
            enemyModel.power = 8;
            enemyModel.w = Constants.ENEMY_OMENOCT_DIAMETER;
            enemyModel.h = Constants.ENEMY_OMENOCT_DIAMETER;
            double[] xPointsO = new double[8];
            double[] yPointsO = new double[8];
            for (int i = 0; i < 8; i++) {
                double angle = 2 * Math.PI * i / 8 + rotationAngle;
                xPointsO[i] = x + enemyModel.w * Math.cos(angle);
                yPointsO[i] = y + enemyModel.h * Math.sin(angle);
            }
            enemyModel.xPoints = xPointsO;
            enemyModel.yPoints = yPointsO;

            enemyModel.attacks.replace("melee", 8);
            enemyModel.attacks.replace("ranged", 4);
            enemyModel.shotTimer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(enemyModel.anchor);
                    ShotModel shotModel = ShotModel.create(enemyModel.anchor, ShotType.enemy, 5);
                    shotModel.setDirection(Utils.getDirection(enemyModel.anchor, EpsilonModel.getINSTANCE().anchor));
                }
            });
            // enemyModel.shotTimer.start();
        }

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

}
