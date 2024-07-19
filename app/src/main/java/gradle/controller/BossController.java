package gradle.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import gradle.model.SmileyFistModel;
import gradle.view.GamePanel;
import gradle.view.charecretsView.SmileyHandsView;
import gradle.view.charecretsView.SmileyView;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.SmileyFistView;

public class BossController implements UPSController {

    public static boolean isAttcking = false;
    // public static boolean isSqueezing = false;
    public static boolean isQuaking = false;
    public static Map<String, Boolean> attacks = new HashMap<>();
    static {
        attacks.put("squeez", false);
        attacks.put("projectile", false);
        attacks.put("vomit", false);
        attacks.put("quake", false);
        attacks.put("rapid", false);
    }

    @Override
    public void check() {
        SmileyHandsView.getLeft().setUtil(SmileyHandsModel.getLeft());
        SmileyHandsView.getRight().setUtil(SmileyHandsModel.getRight());
        SmileyView.items.get(0).setUtil(SmileyModel.getINSTANCE());
        SmileyFistView.items.get(0).setUtil(SmileyFistModel.getINSTANCE());

        SmileyHandsModel.getRight().move();
        SmileyHandsModel.getLeft().move();
        SmileyHandsModel.getLeft().setPanelAnchor();
        SmileyHandsModel.getRight().setPanelAnchor();
        SmileyModel.getINSTANCE().move();
        SmileyModel.getINSTANCE().setPanelAnchor();
        SmileyFistModel.getINSTANCE().move();
        SmileyFistModel.getINSTANCE().setPanelAnchor();
        ;

        if (attacks.get("squeez"))
            if (!checkSqueeze())
                squeezeAttack();
        if (attacks.get("projectile"))
            projectileAttack();
        if (attacks.get("vomit"))
            checkVomit();
        if (attacks.get("quake"))
            checkQuake();
        if (attacks.get("rapid"))
            checkRapid();

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

    public static void vomitAttack() {
        Random random = new Random();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        int partWidth = epsilonPanel.getWidth() / 3;
        int[] yRange = new int[] {
                epsilonPanel.getY(),
                epsilonPanel.getY() + epsilonPanel.getHeight()
        };
        List<int[]> randomAvailabel = List.of(
                new int[] { epsilonPanel.getX(), epsilonPanel.getX() + partWidth },
                new int[] { epsilonPanel.getX() + partWidth, epsilonPanel.getX() + 2 * partWidth },
                new int[] { epsilonPanel.getX() + 2 * partWidth, epsilonPanel.getX() + 3 * partWidth });

        List<Point2D> randomAnchors = new ArrayList<>();
        for (int i = 0; i < randomAvailabel.size(); i++) {
            int randomX = random.nextInt((randomAvailabel.get(i)[1] - randomAvailabel.get(i)[0]) + 1)
                    + randomAvailabel.get(i)[0];
            int randomY = random.nextInt((yRange[1] - yRange[0]) + 1) + yRange[0];
            randomAnchors.add(new Point2D.Double(randomX, randomY));
        }

        SmileyModel.getINSTANCE().vomitAnchors = randomAnchors;

    }

    private void checkVomit() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        for (Point2D vomit : SmileyModel.getINSTANCE().vomitAnchors) {
            if (Utils.getDistance(epsilonModel.anchor, vomit) <= SmileyModel.getINSTANCE().vomitRadius
                    - epsilonModel.w / 2) {
                if (epsilonModel.times.get("boss_vomit") == null) {
                    epsilonModel.times.put("boss_vomit", System.currentTimeMillis());
                    epsilonModel.HP -= 8;
                } else if (System.currentTimeMillis() - epsilonModel.times.get("boss_vomit") >= 1000) {
                    epsilonModel.times.put("boss_vomit", System.currentTimeMillis());
                    epsilonModel.HP -= 8;
                }
            }
        }
    }

    public static void quackeAttack() {
        SmileyFistModel fist = SmileyFistModel.getINSTANCE();
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        GamePanel epsilonPanel = epsilonModel.currentPanels.get(0);
        Point2D newDirection = Utils.getDirection(fist.anchor, new Point2D.Double(
                epsilonPanel.getX() + epsilonPanel.getWidth() / 2, epsilonPanel.getY() + epsilonPanel.getHeight()));
        fist.setDirection(newDirection);
        fist.ableMove = true;
        fist.times.put("quake", System.currentTimeMillis());
        GameSettings.massedUp = true;
        attacks.replace("quake", true);
    }

    private void checkQuake() {
        SmileyFistModel fist = SmileyFistModel.getINSTANCE();
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        GamePanel epsilonPanel = epsilonModel.currentPanels.get(0);
        if (Utils.getDistance(fist.anchor, new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() / 2,
                epsilonPanel.getY() + epsilonPanel.getHeight())) <= fist.w / 2 && !isQuaking) {
            fist.setImpact(new Point2D.Double(-1, -1), fist.max_speed * fist.impact_speed, false);
            fist.setEnemyImpacts(Constants.MAX_DISTANCE_IMPACT * 4, 1);
            isQuaking = true;
        }
        if (isQuaking) {
            Point2D newGoal = new Point2D.Double(
                    epsilonPanel.getX() + epsilonPanel.getWidth() / 2,
                    epsilonPanel.getY() + epsilonPanel.getHeight() + 20);
            Point2D newDirection = Utils.getDirection(fist.anchor, newGoal);
            if (Utils.getDistance(fist.anchor, newGoal) <= fist.w / 2 &&
                    !fist.isImpacting) {
                fist.setDirection(new Point2D.Double(0, 0));
                fist.setFirstAnchor();
                isQuaking = false;
                fist.ableMove = false;
            } else
                fist.setDirection(newDirection);
        }
        if (attacks.get("quake") && System.currentTimeMillis() - fist.times.get("quake") >= 8000) {
            attacks.put("quake", false);
            GameSettings.massedUp = false;
            for (String key : EpsilonController.pressed.keySet()) {
                EpsilonController.pressed.replace(key, false);
            }

        }
    }

    public static void rapidAttack() {
        SmileyModel smileyModel = SmileyModel.getINSTANCE();
        Random random = new Random();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        Timer timer = new Timer(300, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int randomX = random.nextInt(600) + (int) EpsilonModel.getINSTANCE().anchor.getX() - 300;
                Point2D newDirection = Utils.getDirection(smileyModel.anchor,
                        new Point2D.Double(randomX, EpsilonModel.getINSTANCE().anchor.getY()));
                ShotModel shotModel = ShotModel.create(smileyModel.anchor, ShotType.enemy, 8);
                shotModel.setDirection(newDirection);
                shotModel.rigid = false;
            }

        });
        timer.start();
        smileyModel.timers.put("rapid", timer);
        smileyModel.times.put("rapid", System.currentTimeMillis());
        attacks.replace("rapid", true);
    }

    private void checkRapid() {
        if (attacks.get("rapid")
                && System.currentTimeMillis() - SmileyModel.getINSTANCE().times.get("rapid") >= 30000) {
            attacks.put("rapid", false);
            SmileyModel.getINSTANCE().timers.get("rapid").stop();
            SmileyModel.getINSTANCE().times.remove("rapid");
        }
    }
}
