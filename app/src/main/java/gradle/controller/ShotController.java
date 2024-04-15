package gradle.controller;

import java.awt.geom.Point2D;

import gradle.model.EpsilonModel;
import gradle.model.ShotModel;
import gradle.view.GamePanel;
import gradle.view.charecretsView.ShotView;

public class ShotController {
    public static void checkShotWithPanel(ShotModel shotModel) {
        int dx = 0;
        int dy = 0;
        int dw = 0;
        int dh = 0;
        if (shotModel.getPanelAnchor().getX() <= 0) {
            dx = -1;
            dw = 1;
        } else if (shotModel.getPanelAnchor().getX() > GamePanel.getINSTANCE().getWidth()) {
            dw = 1;
            dx = 1;
        } else if (shotModel.getPanelAnchor().getY() > GamePanel.getINSTANCE().getHeight()) {
            dh = 1;
            dy = 1;
        } else if (shotModel.getPanelAnchor().getY() < 0) {
            dy = -1;
            dh = 1;
        }
        GamePanel.getINSTANCE().location = new Point2D.Double(dx, dy);
        GamePanel.getINSTANCE().size = new Point2D.Double(dw, dh);
        if (dy != 0 || dx != 0 || dh != 0 || dw != 0) {
            removeShot(shotModel);
            GamePanel.getINSTANCE().setChanging();
        }

    }

    public static void removeShot(ShotModel shotModel) {
        ShotModel.removedItems.add(shotModel);
        ShotView.removedItems.add(ShotView.findById(shotModel.getId()));
        ShotModel.items.remove(shotModel);
        ShotView.items.removeIf(shot -> shot.getId() == shotModel.getId());
    }
}
