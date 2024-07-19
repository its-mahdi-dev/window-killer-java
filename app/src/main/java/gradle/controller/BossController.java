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
    public static boolean isPunching = false;
    public static int epsilonHPdecrease;
    public static Map<String, Boolean> attacks = new HashMap<>();
    public static Map<String, Integer> attackTimes = new HashMap<>();
    public static Map<String, Long> bossAttackTimes = new HashMap<>();
    public static Map<String, Removable> removes = new HashMap<>();
    static {
        attacks.put("squeeze", false);
        attacks.put("projectile", false);
        attacks.put("vomit", false);
        attacks.put("punch", false);
        attacks.put("quake", false);
        attacks.put("rapid", false);
        attacks.put("slap", false);
    }
    static {
        attackTimes.put("squeeze", 20000);
        attackTimes.put("projectile", 20000);
        attackTimes.put("vomit", 20000);
        attackTimes.put("punch", 20000);
        attackTimes.put("quake", 8000);
        attackTimes.put("rapid", 30000);
        attackTimes.put("slap", 20000);
    }

    interface Removable {
        public void remove();
    }

    @Override
    public void check() {
        if (!GameSettings.bossRun)
            return;
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

        checkEpsilonColision();
        checkTimes();
        if (attacks.get("squeeze"))
            checkSqueeze();
        if (attacks.get("projectile"))
            checkProjectile();
        if (attacks.get("vomit"))
            checkVomit();
        if (attacks.get("punch"))
            checkPunch();
        if (attacks.get("quake"))
            checkQuake();
        if (attacks.get("rapid"))
            checkRapid();
        if (attacks.get("slap"))
            checkSlap();

    }

    private void checkTimes() {
        for (Map.Entry<String, Long> entry : bossAttackTimes.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() >= attackTimes.get(entry.getKey())) {
                attacks.replace(entry.getKey(), false);
                reset();
                if (removes.get(entry.getKey()) != null) {
                    removes.get(entry.getKey()).remove();
                    removes.remove(entry.getKey());
                }
            }
        }
        if (!isAttcking) {
            for (Model model : BossModel.getAllBossEntities()) {
                BossModel bossModel = (BossModel) model;
                bossModel.goToFirstAnchor();
            }
        }

    }

    private static void setAttack(String attack) {
        setAttack(attack, null);
    }

    private static void setAttack(String attack, Removable removeable) {
        isAttcking = true;
        removes.put(attack, removeable);
        bossAttackTimes.put(attack, System.currentTimeMillis());
        attacks.replace(attack, true);
    }

    private void checkEpsilonColision() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        for (Model model : BossModel.getAllBossEntities()) {
            if (Utils.getDistance(epsilonModel.anchor, model.anchor) <= epsilonModel.w / 2 + model.w / 2) {
                epsilonModel.setImpact(Utils.getDirection(model.anchor, epsilonModel.anchor),
                        epsilonModel.max_speed * epsilonModel.impact_speed * 1.5, true);
                if (attacks.get("slap") && model.getId().equals(SmileyHandsModel.getRight().getId())) {
                    epsilonModel.HP -= 4;
                }
            }
        }
    }

    public static void squeezeAttack() {
        setAttack("squeeze");
        SmileyHandsModel.getRight().panel.rigid = true;
        SmileyHandsModel.getLeft().panel.rigid = true;
        SmileyModel.getINSTANCE().ableDecrease = true;
    }

    private void checkSqueeze() {
        SmileyHandsModel left = SmileyHandsModel.getLeft();
        SmileyHandsModel right = SmileyHandsModel.getRight();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);

        Point2D rightDirection;
        if (right.panel.getX() - (epsilonPanel.getX() + epsilonPanel.getWidth()) <= 3 && Math.abs(right.panel.getY()
                + right.panel.getHeight() / 2 - (epsilonPanel.getY() + epsilonPanel.getHeight() / 2)) <= 3) {
            rightDirection = new Point2D.Double(0, 0);
        } else {
            rightDirection = Utils.getDirection(right.anchor,
                    new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() + right.panel.getWidth() / 2 + 5,
                            epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
        }
        right.setDirection(rightDirection);

        Point2D leftDirection;
        if (epsilonPanel.getX() - (left.panel.getX() + left.panel.getWidth()) <= 3
                && Math.abs(left.panel.getY() + left.panel.getHeight() / 2
                        - (epsilonPanel.getY() + epsilonPanel.getHeight() / 2)) <= 3) {
            leftDirection = new Point2D.Double(0, 0);
        } else {
            leftDirection = Utils.getDirection(left.anchor,
                    new Point2D.Double(epsilonPanel.getX() - left.panel.getWidth() / 2 - 5,
                            epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
        }
        left.setDirection(leftDirection);

    }

    public static void projectileAttack() {
        SmileyHandsModel left = SmileyHandsModel.getLeft();
        SmileyHandsModel right = SmileyHandsModel.getRight();
        left.ableDecrease = true;
        right.ableDecrease = true;
        Timer leftTimer = new Timer(2000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ShotModel shotModel = ShotModel.create(left.anchor, ShotType.enemy, 8);
                Point2D newDirection = Utils.getDirection(left.anchor, EpsilonModel.getINSTANCE().anchor);
                shotModel.setDirection(newDirection);
                shotModel.rigid = false;
            }

        });
        leftTimer.start();
        left.timers.put("projectile", leftTimer);

        Timer rightTimer = new Timer(2000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ShotModel shotModel = ShotModel.create(right.anchor, ShotType.enemy, 8);
                Point2D newDirection = Utils.getDirection(right.anchor, EpsilonModel.getINSTANCE().anchor);
                shotModel.setDirection(newDirection);
                shotModel.rigid = false;
            }

        });
        rightTimer.start();
        right.timers.put("projectile", rightTimer);
        setAttack("projectile");
    }

    private void checkProjectile() {
        SmileyHandsModel left = SmileyHandsModel.getLeft();
        SmileyHandsModel right = SmileyHandsModel.getRight();
        SmileyModel smiley = SmileyModel.getINSTANCE();
        left.moveRotation();
        right.moveRotation();
        smiley.moveRotation();
    }

    public static void vomitAttack() {
        Random random = new Random();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        int partWidth = epsilonPanel.getWidth() / 3;
        SmileyModel.getINSTANCE().ableDecrease = true;
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
        setAttack("vomit");

    }

    public static void punchAttack() {

        setAttack("punch");
        isPunching = false;
        SmileyFistModel fist = SmileyFistModel.getINSTANCE();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        fist.panel.rigid = true;
        fist.anchor = new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() + fist.panel.getWidth() + 40,
                epsilonPanel.getY() + epsilonPanel.getHeight() / 2);

    }

    private void checkPunch() {
        SmileyFistModel fist = SmileyFistModel.getINSTANCE();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        // epsilonPanel.speed = fist.speed;
        if (isPunching) {
            // Point2D newDirection = Utils.getDirection(fist.anchor,new
            // Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() + ));
        } else {
            Point2D newDirection = Utils.getDirection(fist.anchor, new Point2D.Double(
                    epsilonPanel.getX() + epsilonPanel.getWidth(), epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
            fist.setDirection(newDirection);
            if (Utils.getDistance(fist.anchor, new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth(),
                    epsilonPanel.getY() + epsilonPanel.getHeight() / 2)) <= 2) {
                epsilonPanel.setSize(epsilonPanel.getWidth() - (int) (newDirection.getX() * fist.speed),
                        epsilonPanel.getHeight());
            }
        }
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

    public static void quakeAttack() {
        setAttack("quake", new Removable() {
            @Override
            public void remove() {
                GameSettings.massedUp = false;
                for (String key : EpsilonController.pressed.keySet()) {
                    EpsilonController.pressed.replace(key, false);
                }
            }
        });
        SmileyFistModel fist = SmileyFistModel.getINSTANCE();
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        GamePanel epsilonPanel = epsilonModel.currentPanels.get(0);
        Point2D newDirection = Utils.getDirection(fist.anchor, new Point2D.Double(
                epsilonPanel.getX() + epsilonPanel.getWidth() / 2, epsilonPanel.getY() + epsilonPanel.getHeight()));
        fist.setDirection(newDirection);
        fist.ableMove = true;
        GameSettings.massedUp = true;

    }

    private void checkQuake() {
        SmileyFistModel fist = SmileyFistModel.getINSTANCE();
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        GamePanel epsilonPanel = epsilonModel.currentPanels.get(0);
        if (Utils.getDistance(fist.anchor, new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() / 2,
                epsilonPanel.getY() + epsilonPanel.getHeight())) <= fist.w / 2 && !isQuaking) {
            fist.setImpact(Utils.getDirection(epsilonModel.anchor, fist.anchor), fist.max_speed * fist.impact_speed,
                    true);
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
    }

    public static void rapidAttack() {
        SmileyModel smileyModel = SmileyModel.getINSTANCE();
        Random random = new Random();
        smileyModel.ableDecrease = true;
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
        setAttack("rapid", new Removable() {

            @Override
            public void remove() {
                SmileyModel.getINSTANCE().timers.get("rapid").stop();
                SmileyModel.getINSTANCE().times.remove("rapid");
            }

        });
    }

    private void checkRapid() {
    }

    public static void slapAttack() {
        SmileyHandsModel.getRight().ableMove = true;
        setAttack("slap");
        SmileyModel.getINSTANCE().ableDecrease = true;
    }

    private void checkSlap() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        SmileyHandsModel right = SmileyHandsModel.getRight();
        Point2D newDirection;
        newDirection = Utils.getDirection(right.anchor, epsilonModel.anchor);
        right.setDirection(newDirection);
    }

    public static void reset() {
        isAttcking = false;
        for (Model model : BossModel.getAllBossEntities()) {
            BossModel bossModel = (BossModel) model;
            bossModel.panel.rigid = false;
            bossModel.panel.isometric = true;
            bossModel.ableDecrease = false;
        }
        SmileyModel.getINSTANCE().vomitAnchors = new ArrayList<>();
    }
}
