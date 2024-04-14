package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import java.awt.FontMetrics;
import gradle.controller.Constants;
import gradle.model.EpsilonModel;
import gradle.model.Model;

public class EpsilonView extends View {
    public static final List<View> items = new ArrayList<>();
    public static final List<View> removedItems = new ArrayList<>();

    public EpsilonView(String Id) {
        super(Id);
        items.add(this);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        int x = (int) anchor.getX() - w / 2;
        int y = (int) anchor.getY() - h / 2;
        g.fillOval(x, y, w, h);

        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        String text = String.valueOf(HP);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int textX = (int) anchor.getX() - textWidth / 2;
        int textY = (int) anchor.getY() + textHeight / 2;
        g.drawString(text, textX, textY);
    }

    @Override
    public void setUtil(Model epsilonModel) {
        anchor = epsilonModel.getPanelAnchor();
        w = epsilonModel.w;
        h = epsilonModel.h;
        HP = epsilonModel.HP;
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

}
