package gradle.view.charecretsView;

import java.awt.Graphics;

import gradle.controller.Constants;

public class EpsilonView extends View {
    public int x = 100;
    public int y = 100;
    public int diameter = Constants.EPSILON_DIAMETER;

    public EpsilonView() {
        items.add(this);
    }

    @Override
    public void draw(Graphics g) {
        g.fillOval(x, y, diameter / 2, diameter / 2);
    }

}
