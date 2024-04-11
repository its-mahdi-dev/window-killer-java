package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import gradle.model.Model;

public class ShotView extends View {
    public static final List<View> items = new ArrayList<>();

    public ShotView() {
        items.add(this);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x, y, w, h);
    }

    @Override
    public void setUtil(Model shotModel) {
        x = shotModel.x;
        y = shotModel.y;
        w = shotModel.w;
        h = shotModel.h;
    }

    @Override
    public List<View> getItems() {
        return items;
    }
}
