package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Utils;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.model.ShotType;

public class ShotView extends View {
    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    public ShotType shotType;

    public ShotView(String Id) {
        super(Id);
    }

    @Override
    public void draw(Graphics g, Component component) {
        Point2D newAnchor = Utils.getRelatedPoint(anchor, component);
        if(shotType == ShotType.epsilon)
            g.setColor(Color.white);
        else if(shotType == ShotType.enemy)
            g.setColor(Color.red);
        int x = (int) newAnchor.getX() - w / 2;
        int y = (int) newAnchor.getY() - h / 2;
        g.fillOval(x, y, w, h);
    }

    @Override
    public void setUtil(Model shotModel) {
        anchor = shotModel.anchor;
        w = shotModel.w;
        h = shotModel.h;
        shotType = ((ShotModel) shotModel).shotType;
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
