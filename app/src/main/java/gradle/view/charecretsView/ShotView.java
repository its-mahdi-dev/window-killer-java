package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import gradle.model.Model;

public class ShotView extends View {
    public static final List<View> items = new ArrayList<>();

    public ShotView(String Id) {
        super(Id);
        items.add(this);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        int x = (int) anchor.getX() - w / 2;
        int y = (int) anchor.getY() - h / 2;
        g.fillOval(x, y, w, h);
    }

    @Override
    public void setUtil(Model shotModel) {
        anchor = shotModel.anchor;
        w = shotModel.w;
        h = shotModel.h;
    }

    @Override
    public List<View> getItems() {
        return items;
    }

    public static View findById(String Id) {
        return View.findView(Id, items);
    }
}
