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
        w = Constants.ENEMY_SQUARE_DIAMETER;
        h = Constants.ENEMY_SQUARE_DIAMETER;
        type = enemyType;
        if (type == EnemyType.square) {
            xPoints = new int[] { (int) anchor.getX(), (int) anchor.getX() + w, (int) anchor.getX() + w,
                    (int) anchor.getX() };
            yPoints = new int[] { (int) anchor.getY(), (int) anchor.getY(), (int) anchor.getY() + h,
                    (int) anchor.getY() + h };
        } else if (type == EnemyType.triangle) {
            xPoints = new int[] { (int) anchor.getX() + w / 2, (int) anchor.getX(), (int) anchor.getX() + w };
            yPoints = new int[] { (int) anchor.getY(), (int) anchor.getY() + h, (int) anchor.getY() + h };
        }
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
}
