package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Utils;
import gradle.model.Model;
import gradle.view.GamePanel;

public abstract class View {

    public Point2D anchor = new Point2D.Double(0, 0);
    public int w;
    public int h;
    public ArrayList<GamePanel> currentPanels = new ArrayList<>();
    public boolean visible;

    private String Id;

    public View(String Id) {
        this.Id = Id;
        visible = true;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void addItem(View item) {
        List<View> items = getItems();
        if (items != null) {
            items.add(item);
        }
    }

    public static View findView(String Id, List<? extends View> items) {
        for (View item : items) {
            if (item.getId().equals(Id)) {
                return item;
            }
        }
        return null;
    }

    protected void drawHP(Graphics2D g2d , Point2D position, int HP) {
        int centerX = (int) position.getX();
        int centerY = (int) position.getY();
        // for (int i = 0; i < newXpoints.length; i++) {
        // centerX += newXpoints[i];
        // centerY += newYpoints[i];
        // }
        // centerX /= newXpoints.length;
        // centerY /= newYpoints.length;

        // Set the font size to 18
        g2d.setFont(new Font("Arial", Font.BOLD, 15));

        // Draw the string at the center of the enemy shape
        g2d.setColor(Color.RED);
        String text = String.valueOf(HP);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = centerX - textWidth / 2;
        int textY = centerY + textHeight / 2;
        g2d.drawString(text, textX, textY);
    }

    public abstract void draw(Graphics g, Component component);

    public abstract void setUtil(Model model);

    public abstract List<View> getItems();

    public abstract List<View> getRemovedItems();
}
