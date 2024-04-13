package gradle.controller;

import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.ShotModel;
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

        setAbleMoves();

        if (pressed.get("up") && !pressed.get("down") && ableMove.get("up")) {
            dy = -1;
        } else if (!pressed.get("up") && pressed.get("down") && ableMove.get("down")) {
            dy = +1;
        }

        if (pressed.get("left") && !pressed.get("right") && ableMove.get("left")) {
            dx = -1;
        } else if (!pressed.get("left") && pressed.get("right") && ableMove.get("right")) {
            dx = +1;
        }

        if (pressed.get("up") && pressed.get("right") && ableMove.get("up") && ableMove.get("right")) {
            dy = -1;
            dx = +1;
            isDoubleClicked = true;
        } else if (pressed.get("up") && pressed.get("left") && ableMove.get("up") && ableMove.get("left")) {
            dy = -1;
            dx = -1;
            isDoubleClicked = true;
        } else if (pressed.get("down") && pressed.get("right") && ableMove.get("down") && ableMove.get("right")) {
            dy = +1;
            dx = +1;
            isDoubleClicked = true;
        } else if (pressed.get("down") && pressed.get("left") && ableMove.get("down") && ableMove.get("left")) {
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

        // System.out.println("speed: " + epsilonModel.speed);
        // System.out.println("velocity: " + epsilonModel.velocity);
        // System.out.println(epsilonModel.isMoving);
    }

    private static void setAbleMoves() {
        if (EpsilonModel.items.get(0).anchor.getX() < 0) {
            ableMove.replace("left", false);
        }
        if (EpsilonModel.items.get(0).anchor.getX() > Constants.PANEL_SIZE.getWidth() - EpsilonModel.items.get(0).w) {
            ableMove.replace("right", false);
        }
        if (EpsilonModel.items.get(0).anchor.getY() < 0) {
            ableMove.replace("up", false);
        }
        if (EpsilonModel.items.get(0).anchor.getY() > Constants.PANEL_SIZE.getHeight() - EpsilonModel.items.get(0).h) {
            ableMove.replace("down", false);
        }
    }

    public static void mousePressed(MouseEvent e) {
        ShotModel shot;
        if (ShotModel.removedItems.size() > 0) {
            shot = (ShotModel) ShotModel.removedItems.get(0);
            ShotView shotView = (ShotView) ShotView.findById(shot.getId(), ShotView.removedItems);
            ShotModel.items.add(shot);
            ShotView.items.add(shotView);
            ShotView.removedItems.remove(shotView);
            ShotModel.removedItems.remove(shot);
            System.out.println("is removed");
        } else
            shot = new ShotModel();
        Point2D direction = Utils.getDirection(shot.anchor, new Point2D.Double(e.getX(), e.getY()));
        shot.setDirection(direction);
        System.out.println("items: " + ShotView.items.size());
        System.out.println("removed items: " + ShotView.removedItems.size());

    }
}
