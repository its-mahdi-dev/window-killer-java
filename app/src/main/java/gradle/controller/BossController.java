package gradle.controller;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import gradle.interfaces.UPSController;
import gradle.model.BossHandsModel;
import gradle.model.BossModel;
import gradle.model.EpsilonModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.view.GamePanel;
import gradle.view.charecretsView.BossHandsView;
import gradle.view.charecretsView.BossView;
import gradle.view.charecretsView.ShotView;

public class BossController implements UPSController {

    public static boolean isAttcking = false;
    // public static boolean isSqueezing = false;
    public Map<String, Boolean> attacks = new HashMap<>();
    {
        attacks.put("squeez", true);
    }

    @Override
    public void check() {
        BossHandsView.getLeft().setUtil(BossHandsModel.getLeft());
        BossHandsView.getRight().setUtil(BossHandsModel.getRight());
        BossView.items.get(0).setUtil(BossModel.getINSTANCE());

        BossHandsModel.getRight().move();
        BossHandsModel.getLeft().move();
        BossHandsModel.getLeft().setPanelAnchor();
        BossHandsModel.getRight().setPanelAnchor();

        if (attacks.get("squeez"))
            if (!checkSqueeze())
                squeezeAttack();

    }

    private void squeezeAttack() {
        BossHandsModel left = BossHandsModel.getLeft();
        BossHandsModel right = BossHandsModel.getRight();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        left.panel.isometric = true;
        left.panel.rigid = true;
        right.panel.isometric = true;
        right.panel.rigid = true;

        Point2D rightDirection = Utils.getDirection(right.anchor,
                new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() + right.panel.getWidth() / 2,
                        epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
        right.setDirection(rightDirection);

        Point2D leftDirection = Utils.getDirection(left.anchor,
                new Point2D.Double(epsilonPanel.getX() - left.panel.getWidth() / 2,
                        epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
        left.setDirection(leftDirection);

    }

    private boolean checkSqueeze() {
        BossHandsModel left = BossHandsModel.getLeft();
        BossHandsModel right = BossHandsModel.getRight();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        boolean isRight = false;
        boolean isLeft = false;
        if (right.panel.getX() - (epsilonPanel.getX() + epsilonPanel.getWidth()) <= 10 && Math.abs(right.panel.getY()
                + right.panel.getHeight() / 2 - (epsilonPanel.getY() + epsilonPanel.getHeight() / 2)) <= 10) {
            isRight = true;
            right.ableMove = false;
        } else {
            right.ableMove = true;
        }
        if (epsilonPanel.getX() - (left.panel.getX() + left.panel.getWidth()) <= 10 && Math.abs(left.panel.getY() + left.panel.getHeight() / 2
                - (epsilonPanel.getY() + epsilonPanel.getHeight() / 2)) <= 10) {
            isLeft = true;
            left.ableMove = false;
        } else {
            left.ableMove = true;
        }

        return isRight && isLeft;
    }

}
