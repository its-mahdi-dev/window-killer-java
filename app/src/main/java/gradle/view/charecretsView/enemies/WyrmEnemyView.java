package gradle.view.charecretsView.enemies;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.View;

public class WyrmEnemyView extends EnemyView {
    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    public WyrmEnemyView(String Id, EnemyType enemyType) {
        super(Id, enemyType);
        // TODO Auto-generated constructor stub
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
        g2d.setStroke(new BasicStroke((float) Constants.ENEMY_STROKE / 2));
        g2d.setColor(Color.PINK);

        g2d.drawPolygon(newXpoints, newYpoints, 4);
        Image necro = new ImageIcon("app/src/main/java/gradle/assets/images/wyrm.png").getImage();
        g2d.translate((int) newAnchor.getX(), (int) newAnchor.getY());

        g2d.rotate(angle);
        g2d.drawImage(necro, -w / 2, -h / 2, w, h, null);
        g2d.rotate(-angle);
        g2d.translate(-(int) newAnchor.getX(), -(int) newAnchor.getY());
        super.drawBase(g2d, newAnchor);
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
