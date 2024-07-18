package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Dimension;

import gradle.controller.Constants;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.charecretsView.BossView;

public class SmileyModel extends BossModel {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    private static SmileyModel INSTANCE;

    private SmileyModel() {
        BossView bossView = new BossView(getId());
        w = Constants.BOSS_HEAD_DIAMETER;
        h = Constants.BOSS_HEAD_DIAMETER;
        minRadius = Constants.BOSS_MINRADIUS;
        max_speed = Constants.BOSS_HAND_SPEED;
        
        angleChange = max_speed / minRadius;
        panel = new GamePanel();
        panel.setSize(w + 60, h + 40);
        int locX = EpsilonModel.getINSTANCE().currentPanels.get(0).getX()
                + EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth() / 2;
        int locY = EpsilonModel.getINSTANCE().currentPanels.get(0).getY()
                - panel.getHeight();

        angleMove = Math.toRadians(50);
        anchor = new Point2D.Double(locX, locY);
        setPanelAnchor();
        Panels.getINSTANCE().addPanel(panel);

        addItem(this);
        bossView.addItem(bossView);
        bossView.setUtil(this);
    }

    public static SmileyModel getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new SmileyModel();
        return INSTANCE;
    }

    public void setPanelAnchor() {
        if (panel != null)
            panel.setLocation((int) anchor.getX() - w / 2 - 30, (int) anchor.getY() - h / 2 - 20);
    }

    @Override
    public List<Model> getItems() {
        return items;
    }

    @Override
    public List<Model> getRemovedItems() {
        return removedItems;
    }
}
