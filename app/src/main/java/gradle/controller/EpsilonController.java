package gradle.controller;

import java.awt.Panel;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import gradle.controller.StoreController.StoreTypes;
import gradle.interfaces.UPSController;
import gradle.model.CollectibleModel;
import gradle.model.EnemyType;
import gradle.model.EpsilonCerbModel;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.model.ShotType;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.charecretsView.CollectibleView;
import gradle.view.charecretsView.EpsilonCerbView;
import gradle.view.charecretsView.EpsilonVertexView;
import gradle.view.charecretsView.EpsilonView;

public class EpsilonController implements UPSController {

    public static boolean isCreating = true;
    public static int shotsNumber;
    public static int shotsSuccessNumber;
    public static int enemyDeadEnemies;
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

    @Override
    public void check() {
        EpsilonModel.getINSTANCE().move();
        checkWallImpact();
        updateVertextAnchor();
        updateCerbAnchor();
        setCurrentPanel();

        EpsilonView.items.get(0).setUtil(EpsilonModel.getINSTANCE());
        for (int i = 0; i < EpsilonVertexView.items.size(); i++) {
            EpsilonVertexView epsilonView = (EpsilonVertexView) EpsilonVertexView.items.get(i);
            EpsilonVertexModel epsilonModel = (EpsilonVertexModel) EpsilonVertexModel.findById(epsilonView.getId());
            epsilonView.setUtil(epsilonModel);
        }
        for (int i = 0; i < EpsilonCerbView.items.size(); i++) {
            EpsilonCerbView epsilonView = (EpsilonCerbView) EpsilonCerbView.items.get(i);
            EpsilonCerbModel epsilonModel = (EpsilonCerbModel) EpsilonCerbModel.findById(epsilonView.getId());
            epsilonView.setUtil(epsilonModel);
        }
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
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
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
            shotsNumber++;
            int power = Constants.EPSILON_SHOT_POWER;

            if (StoreController.itemsActive.get(StoreTypes.phonoi)) {
                power = 50;
                StoreController.itemsActive.replace(StoreTypes.phonoi, false);
            }
            ShotModel shot = ShotModel.create(EpsilonModel.getINSTANCE().anchor, ShotType.epsilon,
                    power);
            Utils.playMusic("shot", false);
            shot.anchor = EpsilonModel.getINSTANCE().anchor;
            Point2D shotGoal = new Point2D.Double(e.getX() + i * (Math.pow(-1, i) * 50),
                    e.getY() + i * (Math.pow(-1, i) * 50));
            Point2D direction = Utils.getDirection(shot.anchor, shotGoal);
            shot.setDirection(direction);
            shot.rigid = true;
        }
    }

    public static void checkWallImpact() {

        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        GamePanel currentPanel = epsilonModel.currentPanels.get(0);
        if (epsilonModel.currentPanels.size() == 1) {
            if (epsilonModel.anchor.getX() - epsilonModel.w / 2 <= currentPanel.getX()) {
                if (epsilonModel.direction.getX() <= 0) {
                    if (epsilonModel.isMoving)
                        epsilonModel.setImpact(new Point2D.Double(-1, 1), false);
                    else
                        epsilonModel.setImpact(new Point2D.Double(1, 0), false);
                }
            }
            if (epsilonModel.anchor.getX() + epsilonModel.w / 2 >= currentPanel.getX() + currentPanel.getWidth()) {
                if (epsilonModel.direction.getX() >= 0) {
                    if (epsilonModel.isMoving)
                        epsilonModel.setImpact(new Point2D.Double(-1, 1), false);
                    else
                        epsilonModel.setImpact(new Point2D.Double(-1, 0), false);
                }
            }
            if (epsilonModel.anchor.getY() - epsilonModel.h / 2 <= currentPanel.getY()) {
                if (epsilonModel.direction.getY() <= 0) {
                    if (epsilonModel.isMoving)
                        epsilonModel.setImpact(new Point2D.Double(1, -1), false);
                    else
                        epsilonModel.setImpact(new Point2D.Double(0, 1), false);
                }
            }
            if (epsilonModel.anchor.getY() + epsilonModel.h / 2 > currentPanel.getY() + currentPanel.getHeight()) {
                if (epsilonModel.direction.getY() >= 0) {
                    if (epsilonModel.isMoving)
                        epsilonModel.setImpact(new Point2D.Double(1, -1), false);
                    else
                        epsilonModel.setImpact(new Point2D.Double(0, -1), false);
                }
            }
        }
    }

    public static void updateCerbAnchor() {
        for (Model cerbModel : EpsilonCerbModel.items) {
            ((EpsilonCerbModel) cerbModel).setRealAnchor();
        }
    }

    public static void updateVertextAnchor() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        for (Model vertex : EpsilonVertexModel.items) {
            double dy = EpsilonModel.getINSTANCE().anchor.getY() - MouseController.mousePos
                    .getY();
            double dx = EpsilonModel.getINSTANCE().anchor.getX() - MouseController.mousePos
                    .getX();
            double angle = Math.atan2(dy, dx);

            double x = epsilonModel.anchor.getX()
                    + (epsilonModel.w / 2 - vertex.w / 2) * Math.cos(vertex.angle + angle);
            double y = epsilonModel.anchor.getY()
                    + (epsilonModel.w / 2 - vertex.w / 2) * Math.sin(vertex.angle + angle);
            vertex.anchor = new Point2D.Double(x, y);
        }
    }

    public static void setCurrentPanel() {
        HashSet<GamePanel> currentGamePanels = new HashSet<>();
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        for (GamePanel panel : Panels.getINSTANCE().getPanels()) {
            if ((epsilonModel.anchor.getX() - epsilonModel.w / 2 >= panel.getX()) &&
                    (epsilonModel.anchor.getX() + epsilonModel.w / 2 <= panel.getX() + panel.getWidth()) &&
                    (epsilonModel.anchor.getY() - epsilonModel.h / 2 >= panel.getY()) &&
                    (epsilonModel.anchor.getY() + epsilonModel.h / 2 <= panel.getY() + panel.getHeight())) {
                currentGamePanels.add(panel);
            }
        }

        // System.out.println(currentGamePanels);
        if (currentGamePanels.size() == 0)
            currentGamePanels.add(epsilonModel.currentPanels.get(0));
        EpsilonModel.getINSTANCE().currentPanels = new ArrayList<>(currentGamePanels);
    }
}
