package gradle.controller;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import gradle.model.EpsilonModel;
import gradle.view.charecretsView.EpsilonView;

public class EpsilonController {

    public static final Map<String, Boolean> pressed = new HashMap<>();
    static {
        pressed.put("up", false);
        pressed.put("down", false);
        pressed.put("left", false);
        pressed.put("right", false);
    }

    public EpsilonController() {
    }

    public static void updateMovement() {
        int dx = 0;
        int dy = 0;

        if (pressed.get("up") && !pressed.get("down")) {
            dy -= Constants.MOVE_SPEED;
        } else if (!pressed.get("up") && pressed.get("down")) {
            dy += Constants.MOVE_SPEED;
        }

        if (pressed.get("left") && !pressed.get("right")) {
            dx -= Constants.MOVE_SPEED;
        } else if (!pressed.get("left") && pressed.get("right")) {
            dx += Constants.MOVE_SPEED;
        }

        if (pressed.get("up") && pressed.get("right")) {
            dy -= Constants.MOVE_SPEED;
            dx += Constants.MOVE_SPEED;
        } else if (pressed.get("up") && pressed.get("left")) {
            dy -= Constants.MOVE_SPEED;
            dx -= Constants.MOVE_SPEED;
        } else if (pressed.get("down") && pressed.get("right")) {
            dy += Constants.MOVE_SPEED;
            dx += Constants.MOVE_SPEED;
        } else if (pressed.get("down") && pressed.get("left")) {
            dy += Constants.MOVE_SPEED;
            dx -= Constants.MOVE_SPEED;
        }

        EpsilonModel.items.get(0).x += dx;
        EpsilonModel.items.get(0).y += dy;

        EpsilonView.items.get(0).setUtil(EpsilonModel.items.get(0));
    }
}
