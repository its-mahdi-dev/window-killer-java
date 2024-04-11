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
        g.fillOval((int) anchor.getX(), (int) anchor.getY(), w, h);
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

}
