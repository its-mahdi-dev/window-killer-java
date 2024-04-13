package gradle.view.charecretsView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.*;

import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;

public class EnemyView extends View {

    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems= new ArrayList<>();

    public EnemyType type;
    public int HP;

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
        int centerX = 0;
        int centerY = 0;
        for (int i = 0; i < xPoints.length; i++) {
            centerX += xPoints[i];
            centerY += yPoints[i];
        }
        centerX /= xPoints.length;
        centerY /= yPoints.length;

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

    }

    @Override
    public void setUtil(Model enemyModel) {
        EnemyModel enemy = (EnemyModel) enemyModel;
        anchor = enemy.anchor;
        w = enemy.w;
        h = enemy.h;
        xPoints = enemy.xPoints;
        yPoints = enemy.yPoints;
        HP = enemy.HP;
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
