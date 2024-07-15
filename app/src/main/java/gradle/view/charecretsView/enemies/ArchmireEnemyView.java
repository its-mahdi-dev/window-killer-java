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

import org.locationtech.jts.geom.Polygon;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;
import gradle.model.enemies.ArchmireEnemy;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.View;

public class ArchmireEnemyView extends EnemyView {

    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    List<Polygon> pathHistory;
    public ArchmireEnemyView(String Id, EnemyType enemyType) {
        super(Id, enemyType);
    }

    @Override
    public void draw(Graphics g, Component component) {
        Map<String, int[]> points = Utils.getPanelPoints(xPoints, yPoints, component);
        Point2D newAnchor = Utils.getRelatedPoint(anchor, component);
        int[] newXpoints = points.get("xPoints");
        int[] newYpoints = points.get("yPoints");

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke((float) Constants.ENEMY_STROKE / 2));

        g2d.setColor(new Color(255, 69, 0, 100));
        

        // Draw the path history

        for (Polygon polygon : pathHistory) {
            int percent =(int) (pathHistory.indexOf(polygon) * 100/ pathHistory.size());
            if(percent <= 1) percent = 2;
            g2d.setColor(new Color(255 , 69 , 0 , percent));
            drawPolygon(g2d, polygon, component);
        }

        g2d.setColor(new Color(255, 69, 0));

        // g2d.drawPolygon(newXpoints, newYpoints, 4);
        Image necro = new ImageIcon("app/src/main/java/gradle/assets/images/archmire.png").getImage();
        g2d.translate((int) newAnchor.getX(), (int) newAnchor.getY());

        g2d.rotate(angle);
        g2d.drawImage(necro, -w / 2, -h / 2, w, h, null);
        g2d.rotate(-angle);
        g2d.translate(-(int) newAnchor.getX(), -(int) newAnchor.getY());
        super.drawBase(g2d, newAnchor);

    }

    private void drawPolygon(Graphics2D g2d, Polygon polygon, Component component) {
        int nPoints = polygon.getNumPoints() - 1; // Exclude the closing point
        int[] newxPoints = new int[nPoints];
        int[] newyPoints = new int[nPoints];

        for (int i = 0; i < nPoints; i++) {
            newxPoints[i] = (int) polygon.getCoordinates()[i].x;
            newyPoints[i] = (int) polygon.getCoordinates()[i].y;
        }

        Map<String, int[]> points = Utils.getPanelPoints(newxPoints, newyPoints, component);
        int[] newXpoints = points.get("xPoints");
        int[] newYpoints = points.get("yPoints");
        g2d.drawPolygon(newXpoints, newYpoints, nPoints);
    }

    @Override
    public void setUtil(Model enemyModel) {
        ArchmireEnemy enemy = (ArchmireEnemy) enemyModel;
        anchor = enemy.anchor;
        w = enemy.w;
        h = enemy.h;
        xPoints = enemy.getXpointsInt();
        yPoints = enemy.getYpointsInt();
        HP = enemy.HP;
        angle = enemy.angle;
        visible = enemy.visible;
        synchronized (enemy.pathHistory) {
            pathHistory = new ArrayList<>(enemy.pathHistory);
        }
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
