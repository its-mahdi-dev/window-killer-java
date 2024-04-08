package gradle.view.charecretsView;

import java.awt.Graphics;

import gradle.controller.Constants;
import gradle.model.EpsilonModel;
import gradle.model.Model;

public class EpsilonView extends View {

    public EpsilonView() {
        items.add(this);
    }

    @Override
    public void draw(Graphics g) {
        g.fillOval(x, y, w, h);
    }

    @Override
    public void setUtil(Model epsilonModel) {
        x = epsilonModel.x;
        y = epsilonModel.y;
        w = epsilonModel.w;
        h = epsilonModel.h;
    }

}
