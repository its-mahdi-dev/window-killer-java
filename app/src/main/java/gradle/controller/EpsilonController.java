package gradle.controller;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import gradle.model.EpsilonModel;
import gradle.model.ShotModel;
import gradle.view.charecretsView.EpsilonView;

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
        int dx = 0;
        int dy = 0;

        for (Map.Entry<String, Boolean> entry : ableMove.entrySet()) {
            ableMove.replace(entry.getKey(), true);
        }

        setAbleMoves();

        if (pressed.get("up") && !pressed.get("down") && ableMove.get("up")) {
            dy -= Constants.MOVE_SPEED;
        } else if (!pressed.get("up") && pressed.get("down") && ableMove.get("down")) {
            dy += Constants.MOVE_SPEED;
        }

        if (pressed.get("left") && !pressed.get("right") && ableMove.get("left")) {
            dx -= Constants.MOVE_SPEED;
        } else if (!pressed.get("left") && pressed.get("right") && ableMove.get("right")) {
            dx += Constants.MOVE_SPEED;
        }

        if (pressed.get("up") && pressed.get("right") && ableMove.get("up") && ableMove.get("right")) {
            dy -= Constants.MOVE_SPEED;
            dx += Constants.MOVE_SPEED;
        } else if (pressed.get("up") && pressed.get("left") && ableMove.get("up") && ableMove.get("left")) {
            dy -= Constants.MOVE_SPEED;
            dx -= Constants.MOVE_SPEED;
        } else if (pressed.get("down") && pressed.get("right") && ableMove.get("down") && ableMove.get("right")) {
            dy += Constants.MOVE_SPEED;
            dx += Constants.MOVE_SPEED;
        } else if (pressed.get("down") && pressed.get("left") && ableMove.get("down") && ableMove.get("left")) {
            dy += Constants.MOVE_SPEED;
            dx -= Constants.MOVE_SPEED;
        }

        EpsilonModel.items.get(0).x += dx;
        EpsilonModel.items.get(0).y += dy;

        EpsilonView.items.get(0).setUtil(EpsilonModel.items.get(0));
    }

    private static void setAbleMoves() {
        if (EpsilonModel.items.get(0).x < 0) {
            ableMove.replace("left", false);
        }
        if (EpsilonModel.items.get(0).x > Constants.PANEL_SIZE.getWidth() - EpsilonModel.items.get(0).w) {
            ableMove.replace("right", false);
        }
        if (EpsilonModel.items.get(0).y < 0) {
            ableMove.replace("up", false);
        }
        if (EpsilonModel.items.get(0).y > Constants.PANEL_SIZE.getHeight() - EpsilonModel.items.get(0).h) {
            ableMove.replace("down", false);
        }
    }

    public static void mousePressed(MouseEvent e) {
        new ShotModel(e.getX(), e.getY(), 10, 10);
        System.out.println(ShotModel.items.size());
        System.out.println("epsilon: " + EpsilonModel.items.size());
    }
}
