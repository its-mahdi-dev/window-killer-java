package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Constants;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.View;

public class EnemyModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public EnemyType type;

    public EnemyModel(Point2D anchor, EnemyType enemyType) {
        this.anchor = anchor;
        type = enemyType;
        int x = (int) anchor.getX();
        int y = (int) anchor.getY();
        double rotationAngle = Math.toRadians(20);

        if (type == EnemyType.square) {
            w = Constants.ENEMY_SQUARE_DIAMETER;
            h = Constants.ENEMY_SQUARE_DIAMETER;
            xPoints = new int[] {
                    (int) (x - w / 2 * Math.cos(rotationAngle) + h / 2 * Math.sin(rotationAngle)),
                    (int) (x + w / 2 * Math.cos(rotationAngle) + h / 2 * Math.sin(rotationAngle)),
                    (int) (x + w / 2 * Math.cos(rotationAngle) - h / 2 * Math.sin(rotationAngle)),
                    (int) (x - w / 2 * Math.cos(rotationAngle) - h / 2 * Math.sin(rotationAngle))
            };
            yPoints = new int[] {
                    (int) (y - w / 2 * Math.sin(rotationAngle) - h / 2 * Math.cos(rotationAngle)),
                    (int) (y + w / 2 * Math.sin(rotationAngle) - h / 2 * Math.cos(rotationAngle)),
                    (int) (y + w / 2 * Math.sin(rotationAngle) + h / 2 * Math.cos(rotationAngle)),
                    (int) (y - w / 2 * Math.sin(rotationAngle) + h / 2 * Math.cos(rotationAngle))
            };
        } else if (type == EnemyType.triangle) {
            w = Constants.ENEMY_TRIANGLE_DIAMETER;
            h = Constants.ENEMY_TRIANGLE_DIAMETER;
            double d = Math.sqrt(3) / 2 * h;
            xPoints = new int[] {
                    (int) (x + d * Math.cos(rotationAngle)),
                    (int) (x + d * Math.cos(rotationAngle - Math.PI * 2 / 3)),
                    (int) (x + d * Math.cos(rotationAngle + Math.PI * 2 / 3))
            };
            yPoints = new int[] {
                    (int) (y + d * Math.sin(rotationAngle)),
                    (int) (y + d * Math.sin(rotationAngle - Math.PI * 2 / 3)),
                    (int) (y + d * Math.sin(rotationAngle + Math.PI * 2 / 3))
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
