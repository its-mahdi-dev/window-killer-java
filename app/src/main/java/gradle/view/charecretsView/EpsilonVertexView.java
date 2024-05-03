package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import gradle.model.Model;

public class EpsilonVertexView extends View {
    public EpsilonVertexView(String Id) {
        super(Id);
    }

    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    @Override
    public void setUtil(Model epsilonModel) {
        anchor = epsilonModel.getPanelAnchor();
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
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        int x = (int) anchor.getX() - w / 2;
        int y = (int) anchor.getY() - w / 2;
        g.fillOval(x, y, w,
                w);
    }
}
