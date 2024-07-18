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

public class BossModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    private static BossModel INSTANCE;

    GamePanel panel;

    private BossModel() {
        BossView bossView = new BossView(getId());
        w = Constants.BOSS_HEAD_DIAMETER;
        h = Constants.BOSS_HEAD_DIAMETER;
        panel = new GamePanel();
        panel.setSize(EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth(), h + 30);
        int locX = EpsilonModel.getINSTANCE().currentPanels.get(0).getX();
        int locY = EpsilonModel.getINSTANCE().currentPanels.get(0).getY() - panel.getHeight();
        panel.setLocation(locX, locY);
        Panels.getINSTANCE().addPanel(panel);

        double x = panel.getX() + panel.getWidth() / 2;
        double y = panel.getY() + panel.getHeight() / 2;
        anchor = new Point2D.Double(x, y);
        addItem(this);
        bossView.addItem(bossView);
        bossView.setUtil(this);
    }

    public static BossModel getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new BossModel();
        return INSTANCE;
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
