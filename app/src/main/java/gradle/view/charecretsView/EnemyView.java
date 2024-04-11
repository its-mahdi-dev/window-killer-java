package gradle.view.charecretsView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;

import gradle.model.EnemyType;
import gradle.model.Model;

public class EnemyView extends View {

    public static final List<View> items = new ArrayList<>();

    public EnemyType type;

    public EnemyView(String Id, EnemyType enemyType) {
        super(Id);
        this.type = enemyType;
        items.add(this);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3.0f));
        if (type == EnemyType.square) {
            g2d.setColor(Color.GREEN);
            g2d.drawPolygon(xPoints, yPoints, 4);
        } else if (type == EnemyType.triangle) {
            g2d.setColor(Color.YELLOW);
            g2d.drawPolygon(xPoints, yPoints, 3);
        }
    }

    @Override
    public void setUtil(Model enemyModel) {
        anchor = enemyModel.anchor;
        w = enemyModel.w;
        h = enemyModel.h;
        xPoints = enemyModel.xPoints;
        yPoints = enemyModel.yPoints;
    }

    @Override
    public List<View> getItems() {
        return items;
    }

    public static View findById(String Id) {
        return View.findView(Id, items);
    }

}
