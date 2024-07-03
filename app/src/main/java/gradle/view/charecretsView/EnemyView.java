package gradle.view.charecretsView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;

import javax.swing.ImageIcon;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;

public class EnemyView extends View {

    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    public EnemyType type;
    public double angle;

    public EnemyView(String Id, EnemyType enemyType) {
        super(Id);
        this.type = enemyType;
    }

    @Override
    public void draw(Graphics g, Component component) {
        Map<String, int[]> points = Utils.getPanelPoints(xPoints, yPoints, component);
        Point2D newAnchor = Utils.getRelatedPoint(anchor, component);
        int[] newXpoints = points.get("xPoints");
        int[] newYpoints = points.get("yPoints");
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke((float) Constants.ENEMY_STROKE));
        if (type == EnemyType.square) {
            g2d.setColor(Color.GREEN);
            g2d.drawPolygon(newXpoints, newYpoints, 4);
        } else if (type == EnemyType.triangle) {
            g2d.setColor(Color.YELLOW);
            g2d.drawPolygon(newXpoints, newYpoints, 3);
        } else if (type == EnemyType.omenoct) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = 0; i < 8; i++) {
                g2d.setColor(i % 2 == 0 ? Color.RED : Color.WHITE);
                fillTriangle(g2d, (int) newAnchor.getX(), (int) newAnchor.getY(), newXpoints[i], newYpoints[i],
                        newXpoints[(i + 1) % 8], newYpoints[(i + 1) % 8]);
            }
            g2d.setColor(Color.green);
            g2d.drawLine((int) newAnchor.getX(), (int) newAnchor.getY(), (int) newAnchor.getX() + w,
                    (int) newAnchor.getY() + h);
        } else if (type == EnemyType.necropick) {
            g2d.setColor(Color.GRAY);

            g2d.drawPolygon(newXpoints, newYpoints, 4);
            Polygon polygon = new Polygon(newXpoints, newYpoints, 4);
            Image necro = new ImageIcon("app/src/main/java/gradle/assets/images/necropick.png").getImage();
            g2d.translate((int) newAnchor.getX(), (int) newAnchor.getY());

            // Rotate the graphics context
            g2d.rotate(angle);

            // Draw the image (adjusting for the negative width and height to handle rotation correctly)
            g2d.drawImage(necro, -w / 2, -h / 2, w, h, null);

            // Reset transformations (optional)
            g2d.rotate(-angle);
            g2d.translate(-(int) newAnchor.getX(), -(int) newAnchor.getY());
        }

        int centerX = 0;
        int centerY = 0;
        for (int i = 0; i < newXpoints.length; i++) {
            centerX += newXpoints[i];
            centerY += newYpoints[i];
        }
        centerX /= newXpoints.length;
        centerY /= newYpoints.length;

        // Set the font size to 18
        g2d.setFont(new Font("Arial", Font.BOLD, 15));

        // Draw the string at the center of the enemy shape
        g2d.setColor(Color.WHITE);
        String text = String.valueOf(HP);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = centerX - textWidth / 2;
        int textY = centerY + textHeight / 2;
        g2d.drawString(text, textX, textY);

        g2d.setColor(Color.white);
        for (int i = 0; i < newXpoints.length; i++) {
            int cenX = newXpoints[i] - 2;
            int cenY = newYpoints[i] - 2;

            g2d.fillOval(cenX, cenY, 4, 4);
        }

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
    public void setUtil(Model enemyModel) {
        EnemyModel enemy = (EnemyModel) enemyModel;
        anchor = enemy.anchor;
        type = enemy.type;
        w = enemy.w;
        h = enemy.h;
        xPoints = enemy.getXpointsInt();
        yPoints = enemy.getYpointsInt();
        HP = enemy.HP;
        angle = enemy.angle;
    }

    @Override
    public List<View> getItems() {
        return items;
    }

    @Override
    protected List<View> getRemovedItems() {
        return removedItems;
    }

    public static View findById(String Id) {
        return View.findView(Id, items);
    }

}
