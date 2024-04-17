package gradle.controller;

import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import gradle.interfaces.Collectible;
import gradle.model.CollectibleModel;
import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.ShotModel;
import gradle.view.GamePanel;
import gradle.view.charecretsView.CollectibleView;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.View;

public class EpsilonController {

    public static final Map<String, Boolean> pressed = new HashMap<>();
    static {
        pressed.put("up", false);
        pressed.put("down", false);
        pressed.put("left", false);
        pressed.put("right", false);
    }

    public static final Map<String, Boolean> ableMove = new HashMap<>();
    static {
        ableMove.put("up", true);
        ableMove.put("down", true);
        ableMove.put("left", true);
        ableMove.put("right", true);
    }

    public EpsilonController() {
    }

    public static void updateMovement() {
        double dx = 0;
        double dy = 0;

        boolean isDoubleClicked = false;

        for (Map.Entry<String, Boolean> entry : ableMove.entrySet()) {
            ableMove.replace(entry.getKey(), true);
        }

        if (pressed.get("up") && !pressed.get("down")) {
            dy = -1;
        } else if (!pressed.get("up") && pressed.get("down")) {
            dy = +1;
        }

        if (pressed.get("left") && !pressed.get("right")) {
            dx = -1;
        } else if (!pressed.get("left") && pressed.get("right")) {
            dx = +1;
        }

        if (pressed.get("up") && pressed.get("right")) {
            dy = -1;
            dx = +1;
            isDoubleClicked = true;
        } else if (pressed.get("up") && pressed.get("left")) {
            dy = -1;
            dx = -1;
            isDoubleClicked = true;
        } else if (pressed.get("down") && pressed.get("right")) {
            dy = +1;
            dx = +1;
            isDoubleClicked = true;
        } else if (pressed.get("down") && pressed.get("left")) {
            dy = +1;
            dx = -1;
            isDoubleClicked = true;
        }

        if (isDoubleClicked) {
            dy /= Math.sqrt(2);
            dx /= Math.sqrt(2);
        }
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        // System.out.println(dx * dx + dy * dy);
        // System.out.println("speed: " + epsilonModel.speed);
        // System.out.println("max speed: " + epsilonModel.max_speed);
        // System.out.println("acceleration: " + epsilonModel.max_speed /
        // Constants.ACCELERATION);
        // System.out.println("acceleration time: " + Constants.ACCELERATION);
        if (dx != 0 || dy != 0)
            epsilonModel.setDirection(new Point2D.Double(dx, dy));
        int c = 0;
        for (Map.Entry<String, Boolean> entry : pressed.entrySet()) {
            if (!entry.getValue())
                c++;
        }
        if (c == pressed.size())
            epsilonModel.isMoving = false;
        else
            epsilonModel.isMoving = true;
    }

    public static void mousePressed(MouseEvent e) {
        ShotModel shot = ShotModel.create();

        shot.anchor = EpsilonModel.items.get(0).anchor;
        Point2D direction = Utils.getDirection(shot.anchor, new Point2D.Double(e.getX(), e.getY()));
        shot.setDirection(direction);

    }

    public static void checkWallImpact() {
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        if (epsilonModel.getPanelAnchor().getX() - epsilonModel.w / 2 < 0) {
            if (epsilonModel.isMoving)
                epsilonModel.setImpact(new Point2D.Double(-1, 1));
            else
                epsilonModel.setImpact(new Point2D.Double(1, 0));
        }
        if (epsilonModel.getPanelAnchor().getX() + epsilonModel.w / 2 > GamePanel.getINSTANCE().getWidth()) {
            if (epsilonModel.isMoving)
                epsilonModel.setImpact(new Point2D.Double(-1, 1));
            else
                epsilonModel.setImpact(new Point2D.Double(-1, 0));
        }
        if (epsilonModel.getPanelAnchor().getY() - epsilonModel.h / 2 < 0) {
            if (epsilonModel.isMoving)
                epsilonModel.setImpact(new Point2D.Double(1, -1));
            else
                epsilonModel.setImpact(new Point2D.Double(0, 1));
        }
        if (epsilonModel.getPanelAnchor().getY() + epsilonModel.h / 2 > GamePanel.getINSTANCE().getHeight()) {
            if (epsilonModel.isMoving)
                epsilonModel.setImpact(new Point2D.Double(1, -1));
            else
                epsilonModel.setImpact(new Point2D.Double(0, -1));
        }
    }

    public static void checkCollectibleCollision(CollectibleModel collectibleModel) {

        if (Utils.getDistance(EpsilonModel.items.get(0).anchor, collectibleModel.anchor) < EpsilonModel.items.get(0).w
                / 2) {
            EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
            epsilonModel.XP += collectibleModel.enemyModel.collectibleXP;
            removeCollectible(collectibleModel.getId());
        }
    }

    public static void removeCollectible(String Id) {
        CollectibleModel collectibleModel = (CollectibleModel) CollectibleModel.findById(Id);
        CollectibleModel.removedItems.add(collectibleModel);
        CollectibleView.removedItems.add(CollectibleView.findById(collectibleModel.getId()));
        CollectibleModel.items.remove(collectibleModel);
        CollectibleView.items.removeIf(collectible -> collectible.getId() == collectibleModel.getId());
    }
}
