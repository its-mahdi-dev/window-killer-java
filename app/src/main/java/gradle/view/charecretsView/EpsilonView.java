package gradle.view.charecretsView;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.model.EpsilonModel;
import gradle.model.Model;

public class EpsilonView extends View {
    public static final List<View> items = new ArrayList<>();

    public EpsilonView(String Id) {
        super(Id);
        items.add(this);
    }

    @Override
    public void draw(Graphics g) {
        int x = (int) anchor.getX() - w / 2;
        int y = (int) anchor.getY() - h / 2;
        g.fillOval(x, y, w, h);
    }

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

    public static View findById(String Id) {
        return View.findView(Id, items);
    }

}
