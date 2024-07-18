package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.charecretsView.BossHandsView;
import gradle.view.charecretsView.BossView;

public class BossHandsModel extends Entity {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public GamePanel panel;

    public BossHandsModel() {
        BossHandsView bossView = new BossHandsView(getId());
        w = Constants.BOSS_HAND_DIAMETER;
        h = Constants.BOSS_HAND_DIAMETER;
        max_speed = Constants.BOSS_HAND_SPEED;
        impact_speed = 1.5;
        ableMove = true;

        panel = new GamePanel();
        panel.setSize(w + 40, h + 40);
        int locX;
        int locY;
        if (items.size() == 0) {
            locX = BossModel.getINSTANCE().panel.getX() - w / 2 - 20;
            locY = BossModel.getINSTANCE().panel.getY() + BossModel.getINSTANCE().panel.getHeight() / 2;
        } else {
            locX = BossModel.getINSTANCE().panel.getX() + BossModel.getINSTANCE().panel.getWidth() + w / 2 + 20;
            locY = BossModel.getINSTANCE().panel.getY() + BossModel.getINSTANCE().panel.getHeight() / 2;
        }

        anchor = new Point2D.Double(locX, locY);
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

    public static BossHandsModel getLeft() {
        return (BossHandsModel) items.get(0);
    }

    public static BossHandsModel getRight() {
        return (BossHandsModel) items.get(1);
    }
}
