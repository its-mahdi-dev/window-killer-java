package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import gradle.controller.Utils;
import gradle.model.Entity;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.model.ShotType;
import gradle.model.SmileyModel;

public class SmileyView extends View {
    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    public List<Point2D> vomitAnchors = new ArrayList<>();
    public int vomitRadius;
    public int HP;

    public SmileyView(String Id) {
        super(Id);
    }

    @Override
    public void draw(Graphics g, Component component) {
        Graphics2D g2d = (Graphics2D) g;
        Point2D newAnchor = Utils.getRelatedPoint(anchor, component);
        int radius1 = w / 2;
        int x = (int) newAnchor.getX() - radius1;
        int y = (int) newAnchor.getY() - radius1;
        Image necro = new ImageIcon("app/src/main/java/gradle/assets/images/smiley.png").getImage();
        g2d.drawImage(necro, x, y, w, h, null);

        // g2d.setColor(new Color(255, 245, 0, 70));
        for (Point2D vomit : vomitAnchors) {
            Point2D vomitAnchor = Utils.getRelatedPoint(vomit, component);
            int vomitX = (int) vomitAnchor.getX() - vomitRadius;
            int vomitY = (int) vomitAnchor.getY() - vomitRadius;
            g2d.setColor(new Color(255, 245, 0, 70));
            g2d.fillOval(vomitX, vomitY, vomitRadius * 2, vomitRadius * 2);
            drawHP(g2d, newAnchor, HP);
        }

    }

    @Override
    public void setUtil(Model bossModel) {
        anchor = bossModel.anchor;
        w = bossModel.w;
        h = bossModel.h;
        vomitAnchors = ((SmileyModel) bossModel).vomitAnchors;
        vomitRadius = ((SmileyModel) bossModel).vomitRadius;

        HP = ((Entity) bossModel).HP;
    }

    @Override
    public List<View> getItems() {
        return items;
    }

    @Override
    public List<View> getRemovedItems() {
        return removedItems;
    }

}
