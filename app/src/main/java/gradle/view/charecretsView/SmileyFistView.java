package gradle.view.charecretsView;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import gradle.controller.Utils;
import gradle.model.SmileyHandsModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.model.ShotType;

public class SmileyFistView extends View {
    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    public SmileyFistView(String Id) {
        super(Id);
    }

    @Override
    public void draw(Graphics g, Component component) {
        Graphics2D g2d = (Graphics2D) g;
        Point2D newAnchor = Utils.getRelatedPoint(anchor, component);
        int radius1 = w / 2;
        int x = (int) newAnchor.getX() - radius1;
        int y = (int) newAnchor.getY() - radius1;
        Image necro = new ImageIcon("app/src/main/java/gradle/assets/images/fist.png").getImage();
        g2d.drawImage(necro, x, y, w, h, null);
    }

    @Override
    public void setUtil(Model bossModel) {
        anchor = bossModel.anchor;
        w = bossModel.w;
        h = bossModel.h;
    }

    @Override
    public List<View> getItems() {
        return items;
    }

    @Override
    public List<View> getRemovedItems() {
        return removedItems;
    }

    public static SmileyFistView getLeft(){
        return (SmileyFistView) items.get(0);
    }
    public static SmileyFistView getRight(){
        return (SmileyFistView) items.get(1);
    }

}
