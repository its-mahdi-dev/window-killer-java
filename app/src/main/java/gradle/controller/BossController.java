package gradle.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import gradle.interfaces.UPSController;
import gradle.model.SmileyHandsModel;
import gradle.model.SmileyModel;
import gradle.model.SmileyModel;
import gradle.model.BossModel;
import gradle.model.EpsilonModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.model.ShotType;
import gradle.view.GamePanel;
import gradle.view.charecretsView.BossHandsView;
import gradle.view.charecretsView.BossView;
import gradle.view.charecretsView.ShotView;

public class BossController implements UPSController {

    public static boolean isAttcking = false;
    // public static boolean isSqueezing = false;
    public Map<String, Boolean> attacks = new HashMap<>();
    {
        attacks.put("squeez", false);
        attacks.put("projectile", true);
    }

    @Override
    public void check() {
        BossHandsView.getLeft().setUtil(SmileyHandsModel.getLeft());
        BossHandsView.getRight().setUtil(SmileyHandsModel.getRight());
        BossView.items.get(0).setUtil(SmileyModel.getINSTANCE());

        SmileyHandsModel.getRight().move();
        SmileyHandsModel.getLeft().move();
        SmileyHandsModel.getLeft().setPanelAnchor();
        SmileyHandsModel.getRight().setPanelAnchor();
        SmileyModel.getINSTANCE().move();
        SmileyModel.getINSTANCE().setPanelAnchor();
        ;

        if (attacks.get("squeez"))
            if (!checkSqueeze())
                squeezeAttack();
        if (attacks.get("projectile"))
            projectileAttack();

    }

    private void squeezeAttack() {
        SmileyHandsModel left = SmileyHandsModel.getLeft();
        SmileyHandsModel right = SmileyHandsModel.getRight();
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
        SmileyHandsModel left = SmileyHandsModel.getLeft();
        SmileyHandsModel right = SmileyHandsModel.getRight();
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
        if (epsilonPanel.getX() - (left.panel.getX() + left.panel.getWidth()) <= 10
                && Math.abs(left.panel.getY() + left.panel.getHeight() / 2
                        - (epsilonPanel.getY() + epsilonPanel.getHeight() / 2)) <= 10) {
            isLeft = true;
            left.ableMove = false;
        } else {
            left.ableMove = true;
        }

        return isRight && isLeft;
    }

    private void projectileAttack() {
        SmileyHandsModel left = SmileyHandsModel.getLeft();
        SmileyHandsModel right = SmileyHandsModel.getRight();
        SmileyModel smiley = SmileyModel.getINSTANCE();
        left.moveRotation();
        right.moveRotation();
        smiley.moveRotation();
        if (left.timers.get("projectile") == null) {
            Timer leftTimer = new Timer(2000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("here");
                    ShotModel shotModel = ShotModel.create(left.anchor, ShotType.enemy, 8);
                    Point2D newDirection = Utils.getDirection(left.anchor, EpsilonModel.getINSTANCE().anchor);
                    shotModel.setDirection(newDirection);
                    shotModel.rigid = false;
                }

            });
            leftTimer.start();
            left.timers.put("projectile", leftTimer);
        }

        if (right.timers.get("projectile") == null) {
            Timer rightTimer = new Timer(2000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("here");
                    ShotModel shotModel = ShotModel.create(right.anchor, ShotType.enemy, 8);
                    Point2D newDirection = Utils.getDirection(right.anchor, EpsilonModel.getINSTANCE().anchor);
                    shotModel.setDirection(newDirection);
                    shotModel.rigid = false;
                }

            });
            rightTimer.start();
            right.timers.put("projectile", rightTimer);
        }

    }

}
