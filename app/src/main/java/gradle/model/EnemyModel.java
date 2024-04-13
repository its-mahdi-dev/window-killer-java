package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Constants;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.View;

public class EnemyModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();
    public EnemyType type;
    public int HP;
    public int power;

    public EnemyModel(Point2D anchor, EnemyType enemyType) {

        this.anchor = anchor;
        type = enemyType;
        double x = anchor.getX();
        double y = anchor.getY();
        max_speed = Constants.ENEMY_SPEED;
        impact_speed = 1.5;
        double rotationAngle = Math.toRadians(20);

        if (type == EnemyType.square) {
            HP = 10;
            power = 6;
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
            HP = 15;
            power = 10;
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
        }

        addItem(this);
        addView();
    }

    private void addView() {
        View view = new EnemyView(getId(), type);
        view.setUtil(this);
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

}
