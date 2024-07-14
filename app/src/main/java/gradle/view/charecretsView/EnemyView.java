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
import gradle.model.enemies.ArchmireEnemy;
import gradle.view.charecretsView.enemies.ArchmireEnemyView;
import gradle.view.charecretsView.enemies.NecropickEnemyView;
import gradle.view.charecretsView.enemies.OmenoctEnemyView;
import gradle.view.charecretsView.enemies.SquareEnemyView;
import gradle.view.charecretsView.enemies.TriangleEnemyView;
import gradle.view.charecretsView.enemies.WyrmEnemyView;

public abstract class EnemyView extends View {

    public double angle;
    public int HP;
    public int[] xPoints;
    public int[] yPoints;

    public EnemyView(String Id, EnemyType enemyType) {
        super(Id);
    }

    public void drawBase(Graphics2D g2d, Point2D newAnchor) {

        int centerX = (int) newAnchor.getX();
        int centerY = (int) newAnchor.getY();
        // for (int i = 0; i < newXpoints.length; i++) {
        // centerX += newXpoints[i];
        // centerY += newYpoints[i];
        // }
        // centerX /= newXpoints.length;
        // centerY /= newYpoints.length;

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

        // g2d.setColor(Color.white);
        // for (int i = 0; i < newXpoints.length; i++) {
        // int cenX = newXpoints[i] - 2;
        // int cenY = newYpoints[i] - 2;

        // g2d.fillOval(cenX, cenY, 4, 4);
        // }

    }

    public void setBaseUtil(Model enemyModel) {
        EnemyModel enemy = (EnemyModel) enemyModel;
        anchor = enemy.anchor;
        w = enemy.w;
        h = enemy.h;
        HP = enemy.HP;
        visible = enemy.visible;
        xPoints = enemy.getXpointsInt();
        yPoints = enemy.getYpointsInt();
    }

    public static List<View> getEnemyViews() {
        List<View> enemyViews = new ArrayList<>();
        enemyViews.addAll(ArchmireEnemyView.items);
        enemyViews.addAll(WyrmEnemyView.items);
        enemyViews.addAll(NecropickEnemyView.items);
        enemyViews.addAll(SquareEnemyView.items);
        enemyViews.addAll(TriangleEnemyView.items);
        enemyViews.addAll(OmenoctEnemyView.items);
        return enemyViews;
    }

    public abstract List<View> getRemovedItems();

    public static View findById(String Id) {
        return View.findView(Id, getEnemyViews());
    }


}
