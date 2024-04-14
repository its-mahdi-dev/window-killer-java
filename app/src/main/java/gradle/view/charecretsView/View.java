package gradle.view.charecretsView;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Utils;
import gradle.model.Model;
import gradle.view.GamePanel;

public abstract class View {

    public Point2D anchor = new Point2D.Double(0, 0);
    public int w;
    public int h;
    public int[] xPoints;
    public int[] yPoints;

    public int HP;

    private String Id;

    public View(String Id) {
        this.Id = Id;
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

    public void serRelativeAnchor(Point2D point2d) {
        anchor = Utils.getRelatedPoint(anchor, GamePanel.getINSTANCE());
    }

    public abstract void draw(Graphics g);

    public abstract void setUtil(Model model);

    public abstract List<View> getItems();

    protected abstract List<View> getRemovedItems();
}
