package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
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
            EnemyView.removedItems.removeIf(collectible -> collectible.getId() == enemyModel.getId());
        } else {
            enemyModel = new EnemyModel(anchor, enemyType);
            enemyView = new EnemyView(enemyModel.getId(), enemyModel.type);

        }

        enemyModel.anchor = anchor;
        enemyModel.type = enemyType;
        double x = anchor.getX();
        double y = anchor.getY();
        enemyModel.max_speed = Constants.ENEMY_SPEED - 0.4 + (GameSettings.level / 5);
        enemyModel.impact_speed = 2.8;
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
        }

        CollectibleModel.removeEnemyCollectibles(enemyModel);
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
        return num;
    }

    @Override
    public void setCollectible() {
        for (int i = 0; i < collectibleCount; i++) {
            CollectibleModel.create(getId(), new Point2D.Double(anchor.getX() + (Math.pow(-1, i) * i * 20),
                    anchor.getY() + (Math.pow(-1, i) * i * 20)));
        }
    }

}
