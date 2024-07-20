package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.min;

import gradle.controller.Constants;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.charecretsView.SmileyHandsView;
import gradle.view.charecretsView.SmileyView;

public class SmileyHandsModel extends BossModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public SmileyHandsModel() {
        SmileyHandsView bossView = new SmileyHandsView(getId());
        w = Constants.BOSS_HAND_DIAMETER;
        h = Constants.BOSS_HAND_DIAMETER;
        max_speed = Constants.BOSS_HAND_SPEED;
        minRadius = Constants.BOSS_MINRADIUS;
        impact_speed = 1.5;
        ableMove = true;

        panel = new GamePanel();
        panel.setSize(w + 40, h + 40);
        int locX;
        int locY;
        if (items.size() == 0) {
            locX = SmileyModel.getINSTANCE().panel.getX() - w / 2 - 20;
            locY = SmileyModel.getINSTANCE().panel.getY() + SmileyModel.getINSTANCE().panel.getHeight() + h / 2 + 20;
            angleMove = Math.toRadians(0);
        } else {
            locX = SmileyModel.getINSTANCE().panel.getX() + SmileyModel.getINSTANCE().panel.getWidth() + w / 2 + 20;
            locY = SmileyModel.getINSTANCE().panel.getY() + SmileyModel.getINSTANCE().panel.getHeight() + h / 2 + 20;
            angleMove = Math.toRadians(100);
        }
        HP = 100;
        angleChange = max_speed / minRadius;
        anchor = new Point2D.Double(locX, locY);
        firstAnchor = anchor;
        panel.isometric = true;
        setPanelAnchor();
        Panels.getINSTANCE().addPanel(panel);

        double x = panel.getX() + panel.getWidth() / 2;
        double y = panel.getY() + panel.getHeight() / 2;
        anchor = new Point2D.Double(x, y);
        addItem(this);
        bossView.addItem(bossView);
        bossView.setUtil(this);
    }

    public void setPanelAnchor() {
        if (panel != null)
            panel.setLocation((int) anchor.getX() - w / 2 - 20, (int) anchor.getY() - h / 2 - 20);
    }

    @Override
    public List<Model> getItems() {
        return items;
    }

    @Override
    public List<Model> getRemovedItems() {
        return removedItems;
    }

    public static SmileyHandsModel getLeft() {
        return (SmileyHandsModel) items.get(0);
    }

    public static SmileyHandsModel getRight() {
        return (SmileyHandsModel) items.get(1);
    }
}
