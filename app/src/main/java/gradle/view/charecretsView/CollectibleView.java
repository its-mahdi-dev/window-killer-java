package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Utils;
import gradle.model.CollectibleModel;
import gradle.model.EnemyType;
import gradle.model.Model;

public class CollectibleView extends View {
    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();
    public EnemyType enemyType;

    public CollectibleView(String Id, EnemyType enemyType) {
        super(Id);
        this.enemyType = enemyType;
    }

    @Override
    public void draw(Graphics g , Component component) {
        Point2D newAnchor = Utils.getRelatedPoint(anchor, component);
        if (enemyType == EnemyType.square)
            g.setColor(Color.green);
        else if (enemyType == EnemyType.triangle)
            g.setColor(Color.yellow);
        else if (enemyType == EnemyType.omenoct)
            g.setColor(Color.red);
        int x = (int) newAnchor.getX() - w / 2;
        int y = (int) newAnchor.getY() - h / 2;
        g.fillOval(x, y, w, h);
    }

    @Override
    public void setUtil(Model collectibleModel) {
        CollectibleModel collectible = (CollectibleModel) collectibleModel;
        anchor = collectible.anchor;
        enemyType = collectible.enemyType;
        w = collectible.w;
        h = collectible.h;
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

    public static View findById(String Id, List<View> searchItems) {
        return View.findView(Id, searchItems);
    }
}
