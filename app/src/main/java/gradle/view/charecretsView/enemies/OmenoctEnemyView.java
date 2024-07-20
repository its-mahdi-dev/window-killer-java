package gradle.view.charecretsView.enemies;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.View;

public class OmenoctEnemyView extends EnemyView{

    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    public OmenoctEnemyView(String Id, EnemyType enemyType) {
        super(Id, enemyType);
        //TODO Auto-generated constructor stub
    }
    @Override
    public void setUtil(Model enemyModel) {
        EnemyModel enemy = (EnemyModel) enemyModel;
        anchor = enemy.anchor;
        w = enemy.w;
        h = enemy.h;
        xPoints = enemy.getXpointsInt();
        yPoints = enemy.getYpointsInt();
        HP = enemy.HP;
        angle = enemy.angle;
        visible = enemy.visible;
    }

    @Override
    public void draw(Graphics g, Component component) {
        
        Map<String, int[]> points = Utils.getPanelPoints(xPoints, yPoints, component);
        Point2D newAnchor = Utils.getRelatedPoint(anchor, component);
        int[] newXpoints = points.get("xPoints");
        int[] newYpoints = points.get("yPoints");
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke((float) Constants.ENEMY_STROKE));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < 8; i++) {
            g2d.setColor(i % 2 == 0 ? Color.RED : Color.WHITE);
            fillTriangle(g2d, (int) newAnchor.getX(), (int) newAnchor.getY(), newXpoints[i], newYpoints[i],
                    newXpoints[(i + 1) % 8], newYpoints[(i + 1) % 8]);
        }
        super.drawBase(g2d, newAnchor);

    }
    private void fillTriangle(Graphics2D g2d, int x1, int y1, int x2, int y2, int x3, int y3) {
        Path2D triangle = new Path2D.Double();
        triangle.moveTo(x1, y1);
        triangle.lineTo(x2, y2);
        triangle.lineTo(x3, y3);
        triangle.closePath();
        g2d.fill(triangle);
    }

    

    @Override
    public List<View> getItems() {
        return items;
    }

    @Override
    public List<View> getRemovedItems() {
        return removedItems;
    }

    public static View findById(String Id) {
        return View.findView(Id, items);
    }

    
}
