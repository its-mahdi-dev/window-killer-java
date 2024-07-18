package gradle.view.charecretsView.enemies;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;
import gradle.model.enemies.BlackorbEnemy;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.View;

public class BlackorbEnemyView extends EnemyView {
    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    List<Point2D> orbs = new LinkedList<>();
    List<Polygon> lasers = new LinkedList<>();

    public BlackorbEnemyView(String Id, EnemyType enemyType) {
        super(Id, enemyType);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setUtil(Model enemyModel) {
        BlackorbEnemy enemy = (BlackorbEnemy) enemyModel;
        anchor = enemy.anchor;
        w = enemy.w;
        h = enemy.h;
        xPoints = enemy.getXpointsInt();
        yPoints = enemy.getYpointsInt();
        HP = enemy.HP;
        angle = enemy.angle;
        visible = enemy.visible;
        orbs = enemy.orbs;
        lasers = enemy.getOrbsPolygon();
    }

    @Override
    public void draw(Graphics g, Component component) {

        Point2D newBaseAnchor = Utils.getRelatedPoint(anchor, component);
        Graphics2D g2d = (Graphics2D) g;
        // g2d.setStroke(new BasicStroke((float) Constants.ENEMY_STROKE));

        // g2d.drawPolygon(newXpoints, newYpoints, 4);
        for (int i = 0; i < lasers.size(); i++) {
            g2d.setColor(generateRandomPurpleColor());
            Polygon laser = lasers.get(i);
            Map<String, int[]> points = Utils.getPanelPoints(laser.xpoints, laser.ypoints, component);
            int[] newXpoints = points.get("xPoints");
            int[] newYpoints = points.get("yPoints");

            g2d.fillPolygon(newXpoints, newYpoints, 4);

        }

        g2d.setStroke(new BasicStroke((float) Constants.ENEMY_STROKE / 4));
        g2d.setColor(new Color(116, 15, 109));
        for (int i = 0; i < orbs.size(); i++) {
            Point2D orb = orbs.get(i);

            Point2D newAnchor = Utils.getRelatedPoint(orb, component);
            int radius1 = w / 2;
            int x = (int) newAnchor.getX() - radius1;
            int y = (int) newAnchor.getY() - radius1;
            g2d.fillOval(x, y, w, h);

            Image necro = new ImageIcon("app/src/main/java/gradle/assets/images/blackorb.png").getImage();
            g2d.drawImage(necro, x, y, w, h, null);
        }

        super.drawBase(g2d, newBaseAnchor);
    }

    private static Color generateRandomPurpleColor() {
        Random rand = new Random();
        int red = 128 + rand.nextInt(128); // Red between 128 and 255
        int blue = 128 + rand.nextInt(128); // Blue between 128 and 255
        int green = rand.nextInt(64); // Green between 0 and 63 to maintain purple hue

        return new Color(red, green, blue, 90);
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
