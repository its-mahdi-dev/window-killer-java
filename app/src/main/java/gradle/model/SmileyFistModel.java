package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.min;

import gradle.controller.Constants;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.charecretsView.SmileyFistView;
import gradle.view.charecretsView.SmileyHandsView;
import gradle.view.charecretsView.SmileyView;

public class SmileyFistModel extends BossModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public static SmileyFistModel INSTANCE;

    private SmileyFistModel() {
        SmileyFistView bossView = new SmileyFistView(getId());
        w = Constants.BOSS_FIST_DIAMETER;
        h = Constants.BOSS_FIST_DIAMETER;
        max_speed = Constants.BOSS_HAND_SPEED * 5;
        minRadius = Constants.BOSS_MINRADIUS;
        impact_speed = 1.5;
        ableMove = true;

        panel = new GamePanel();
        panel.setSize(w + 40, h + 40);
        setFirstAnchor();
        angleMove = Math.toRadians(100);

        angleChange = max_speed / minRadius;
        panel.isometric = true;
        setPanelAnchor();
        Panels.getINSTANCE().addPanel(panel);
        firstAnchor = anchor;
        addItem(this);
        bossView.addItem(bossView);
        bossView.setUtil(this);
    }

    public static SmileyFistModel getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new SmileyFistModel();
        return INSTANCE;
    }

    public void setFirstAnchor() {

        int locX = EpsilonModel.getINSTANCE().currentPanels.get(0).getX()
                + EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth() / 2;
        int locY = EpsilonModel.getINSTANCE().currentPanels.get(0).getY()
                + EpsilonModel.getINSTANCE().currentPanels.get(0).getHeight() + h / 2 + 40;

        anchor = new Point2D.Double(locX, locY);
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

    public static SmileyFistModel getLeft() {
        return (SmileyFistModel) items.get(0);
    }

    public static SmileyFistModel getRight() {
        return (SmileyFistModel) items.get(1);
    }
}
