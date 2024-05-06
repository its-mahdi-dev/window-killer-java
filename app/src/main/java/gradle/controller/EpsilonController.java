package gradle.controller;

import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import gradle.interfaces.Collectible;
import gradle.model.CollectibleModel;
import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.view.GameFrame;
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
        for (int i = 0; i < StoreController.shotsNumber; i++) {
            ShotModel shot = ShotModel.create();
            Utils.playMusic("shot", false);
            shot.anchor = EpsilonModel.items.get(0).anchor;
            Point2D shotGoal = new Point2D.Double(e.getX() + i * (Math.pow(-1, i) * 50),
                    e.getY() + i * (Math.pow(-1, i) * 50));
            Point2D direction = Utils.getDirection(shot.anchor, shotGoal);
            shot.setDirection(direction);
        }

    }

    public static void checkWallImpact() {
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        if (epsilonModel.getPanelAnchor().getX() - epsilonModel.w / 2 < 0) {
            if (epsilonModel.direction.getX() <= 0) {
                if (epsilonModel.isMoving)
                    epsilonModel.setImpact(new Point2D.Double(-1, 1), false);
                else
                    epsilonModel.setImpact(new Point2D.Double(1, 0), false);
            }
        }
        if (epsilonModel.getPanelAnchor().getX() + epsilonModel.w / 2 > GamePanel.getINSTANCE().getWidth()) {
            if (epsilonModel.direction.getX() >= 0) {
                if (epsilonModel.isMoving)
                    epsilonModel.setImpact(new Point2D.Double(-1, 1), false);
                else
                    epsilonModel.setImpact(new Point2D.Double(-1, 0), false);
            }
        }
        if (epsilonModel.getPanelAnchor().getY() - epsilonModel.h / 2 < 0) {
            if (epsilonModel.direction.getY() <= 0) {
                if (epsilonModel.isMoving)
                    epsilonModel.setImpact(new Point2D.Double(1, -1), false);
                else
                    epsilonModel.setImpact(new Point2D.Double(0, 1), false);
            }
        }
        if (epsilonModel.getPanelAnchor().getY() + epsilonModel.h / 2 > GamePanel.getINSTANCE().getHeight()) {
            if (epsilonModel.direction.getY() >= 0) {
                if (epsilonModel.isMoving)
                    epsilonModel.setImpact(new Point2D.Double(1, -1), false);
                else
                    epsilonModel.setImpact(new Point2D.Double(0, -1), false);
            }
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
        // Utils.playMusic("app/src/main/java/gradle/assets/musics/ham.wav");
        CollectibleModel collectibleModel = (CollectibleModel) CollectibleModel.findById(Id);
        CollectibleModel.removedItems.add(collectibleModel);
        CollectibleView.removedItems.add(CollectibleView.findById(collectibleModel.getId()));
        CollectibleModel.items.remove(collectibleModel);
        CollectibleView.items.removeIf(collectible -> collectible.getId() == collectibleModel.getId());
    }

    public static void updateVertextAnchor() {
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        for (Model vertex : EpsilonVertexModel.items) {
            double dy = EpsilonModel.items.get(0).anchor.getY() - MouseController.mousePos
                    .getY();
            double dx = EpsilonModel.items.get(0).anchor.getX() - MouseController.mousePos
                    .getX();
            double angle = Math.atan2(dy, dx);

            double x = epsilonModel.anchor.getX()
                    + (epsilonModel.w / 2 - vertex.w / 2) * Math.cos(vertex.angle + angle);
            double y = epsilonModel.anchor.getY()
                    + (epsilonModel.w / 2 - vertex.w / 2) * Math.sin(vertex.angle + angle);
            vertex.anchor = new Point2D.Double(x, y);
        }
    }
}
