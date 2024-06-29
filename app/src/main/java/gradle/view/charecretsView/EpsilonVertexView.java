package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Utils;
import gradle.model.Model;

public class EpsilonVertexView extends View {
    public EpsilonVertexView(String Id) {
        super(Id);
    }

    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    @Override
    public void setUtil(Model epsilonModel) {
        anchor = epsilonModel.anchor;
        w = epsilonModel.w;
        h = epsilonModel.h;
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

    @Override
    public void draw(Graphics g , Component component) {
        Point2D newAnchor = Utils.getRelatedPoint(anchor, component);
        g.setColor(Color.RED);
        int x = (int) newAnchor.getX() - w / 2;
        int y = (int) newAnchor.getY() - w / 2;
        g.fillOval(x, y, w,
                w);
    }
}
