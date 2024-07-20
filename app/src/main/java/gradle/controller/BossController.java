package gradle.controller;

import java.awt.Dimension;
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
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.charecretsView.SmileyHandsView;
import gradle.view.charecretsView.SmileyView;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.SmileyFistView;

public class BossController implements UPSController {

    public static boolean isAttcking = false;
    // public static boolean isSqueezing = false;
    public static boolean[] fixSqueeze = new boolean[2];
    public static boolean[] fistAttacks = new boolean[2];
    public static String[] fistAttacksNames = new String[2];
    public static boolean isQuaking = false;
    public static boolean isPunching = false;
    public static boolean isFist = false;
    public static int epsilonHPdecrease;
    public static Map<String, Boolean> attacks = new HashMap<>();
    public static Map<String, Integer> attackTimes = new HashMap<>();
    public static Map<String, Long> bossAttackTimes = new HashMap<>();
    public static Map<String, Long> bossAttackFinishTimes = new HashMap<>();
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
        attackTimes.put("punch", 8000);
        attackTimes.put("quake", 8000);
        attackTimes.put("rapid", 30000);
        attackTimes.put("slap", 20000);

        bossAttackFinishTimes.put("squeeze", System.currentTimeMillis() - 30 * 60 * 1000);
        bossAttackFinishTimes.put("projectile", System.currentTimeMillis() - 30 * 60 * 1000);
        bossAttackFinishTimes.put("vomit", System.currentTimeMillis() - 30 * 60 * 1000);
        bossAttackFinishTimes.put("punch", System.currentTimeMillis() - 30 * 60 * 1000);
        bossAttackFinishTimes.put("quake", System.currentTimeMillis() - 30 * 60 * 1000);
        bossAttackFinishTimes.put("rapid", System.currentTimeMillis() - 30 * 60 * 1000);
        bossAttackFinishTimes.put("slap", System.currentTimeMillis() - 30 * 60 * 1000);
    }

    interface Removable {
        public void remove();
    }

    public static void start() {
        EnemyController.removeAll();
        CollectibleController.removeAllCollectible();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        epsilonPanel.setSize(Constants.PANEL_SIZE);
        epsilonPanel.setLocationToCenter(GameFrame.getINSTANCE());
        EpsilonModel.getINSTANCE().anchor = new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() / 2,
                epsilonPanel.getY() + epsilonPanel.getHeight() / 2);
        EpsilonModel.getINSTANCE().ableMove = true;
        EpsilonModel.getINSTANCE().direction = new Point2D.Double(0, 0);
        for (Timer timer : EpsilonModel.getINSTANCE().timers.values())
            timer.stop();
        System.out.println(SmileyHandsModel.items.size());
        SmileyModel.getINSTANCE();
        new SmileyHandsModel();
        new SmileyHandsModel();
        // SmileyFistModel.getINSTANCE();
        GameSettings.bossRun = true;
    }

    @Override
    public void check() {
        if (!GameSettings.bossRun)
            return;
        SmileyHandsView.getLeft().setUtil(SmileyHandsModel.getLeft());
        SmileyHandsView.getRight().setUtil(SmileyHandsModel.getRight());
        SmileyView.items.get(0).setUtil(SmileyModel.getINSTANCE());

        if (isFist) {
            SmileyFistView.items.get(0).setUtil(SmileyFistModel.getINSTANCE());
            SmileyFistModel.getINSTANCE().move();
            SmileyFistModel.getINSTANCE().setPanelAnchor();
        }
        SmileyHandsModel.getRight().move();
        SmileyHandsModel.getLeft().move();
        SmileyHandsModel.getLeft().setPanelAnchor();
        SmileyHandsModel.getRight().setPanelAnchor();
        SmileyModel.getINSTANCE().move();
        SmileyModel.getINSTANCE().setPanelAnchor();
        if (SmileyModel.getINSTANCE().HP <= 200 && !isFist) {
            isFist = true;
            SmileyFistModel.getINSTANCE();
        }
        checkEpsilonColision();
        checkTimes();
        if (!isFist)
            checkAttck();
        else
            checkFistAttack();
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

    private void checkAttck() {

        if (!attacks.get("squeeze")
                && System.currentTimeMillis() - bossAttackFinishTimes.get("squeeze") > attackTimes.get("squeeze")
                && !attacks.get("projectile") && isInFirstAnchor()) {
            Point2D epsilonAnchor = EpsilonModel.getINSTANCE().anchor;
            SmileyHandsModel left = SmileyHandsModel.getLeft();
            SmileyHandsModel right = SmileyHandsModel.getRight();
            if (epsilonAnchor.getX() > left.anchor.getX() + left.w / 2 &&
                    epsilonAnchor.getX() < right.anchor.getX() - right.w / 2 &&
                    epsilonAnchor.getY() > right.panel.getY() &&
                    epsilonAnchor.getY() < right.panel.getY() + right.panel.getHeight() &&
                    epsilonAnchor.getY() > left.panel.getY() &&
                    epsilonAnchor.getY() < left.panel.getY() + left.panel.getHeight()) {
                squeezeAttack();
            }
        }
        if (!attacks.get("projectile") && !attacks.get("squeeze") && isInFirstAnchor()
                && System.currentTimeMillis() - bossAttackFinishTimes.get("projectile") > attackTimes
                        .get("projectile") * 2) {
            projectileAttack();
        }

    }

    private void checkFistAttack() {
        if (!fistAttacks[0]) {
            if ("rapid".equals(fistAttacksNames[0])
                    && System.currentTimeMillis() - bossAttackFinishTimes.get("slap") > attackTimes.get("slap")
                    && !attacks.get("slap") && !attacks.get("rapid") && !attacks.get("vomit")) {
                slapAttack();
                fistAttacks[0] = true;
                fistAttacksNames[0] = "slap";
            } else if (bossAttackFinishTimes.get("vomit") <= bossAttackFinishTimes.get("rapid")
                    && System.currentTimeMillis() - bossAttackFinishTimes.get("vomit") > attackTimes.get("vomit")
                    && !attacks.get("vomit") && !attacks.get("rapid") && !"vomit".equals(fistAttacksNames[0])
                    && isInFirstAnchor()) {
                fistAttacksNames[0] = "vomit";
                vomitAttack();
                fistAttacks[0] = true;
            } else if (System.currentTimeMillis() - bossAttackFinishTimes.get("rapid") > attackTimes.get("rapid")
                    && !attacks.get("vomit") && !attacks.get("rapid") && !"rapid".equals(fistAttacksNames[0])
                    && isInFirstAnchor()) {
                fistAttacksNames[0] = "rapid";
                rapidAttack();
                fistAttacks[0] = true;
            }
        }
        if (!fistAttacks[1]) {
            if (bossAttackFinishTimes.get("quake") <= bossAttackFinishTimes.get("punch")
                    && System.currentTimeMillis() - bossAttackFinishTimes.get("quake") > 2 * attackTimes.get("quake")
                    && !attacks.get("quake") && !attacks.get("punch") && isInFirstAnchor()
                    && !"quake".equals(fistAttacksNames[0])) {
                fistAttacksNames[1] = "quake";
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                fistAttacks[1] = true;
                executor.schedule(() -> {
                    quakeAttack();
                }, 2, TimeUnit.SECONDS);
            } else if (System.currentTimeMillis() - bossAttackFinishTimes.get("punch") > 2 * attackTimes.get("punch")
                    && !attacks.get("quake") && !attacks.get("punch") && isInFirstAnchor()
                    && !"punch".equals(fistAttacksNames[0])) {
                fistAttacksNames[1] = "punch";
                punchAttack();
                fistAttacks[1] = true;
            }
        }
    }

    private void checkTimes() {
        for (Map.Entry<String, Long> entry : bossAttackTimes.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() >= attackTimes.get(entry.getKey())
                    && attacks.get(entry.getKey())) {
                bossAttackFinishTimes.put(entry.getKey(), System.currentTimeMillis());
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

    private boolean isInFirstAnchor() {
        for (Model model : BossModel.getAllBossEntities()) {
            BossModel bossModel = (BossModel) model;
            if (Utils.getDistance(bossModel.anchor, bossModel.firstAnchor) >= 2)
                return false;
        }
        return true;
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
        setAttack("squeeze", new Removable() {

            @Override
            public void remove() {
                fixSqueeze[0] = false;
                fixSqueeze[1] = false;
            }

        });
        SmileyHandsModel.getRight().panel.rigid = true;
        SmileyHandsModel.getLeft().panel.rigid = true;
        SmileyModel.getINSTANCE().ableDecrease = true;
        fixSqueeze[0] = false;
        fixSqueeze[1] = false;
    }

    private void checkSqueeze() {
        SmileyHandsModel left = SmileyHandsModel.getLeft();
        SmileyHandsModel right = SmileyHandsModel.getRight();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);

        double rightXdistance = right.panel.getX() - (epsilonPanel.getX() + epsilonPanel.getWidth() + 5);
        double rightYdistance = Math.abs(right.panel.getY()
                + right.panel.getHeight() / 2 - (epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
        Point2D rightDirection = new Point2D.Double(0, 0);
        if (rightXdistance <= 3 &&
                rightXdistance >= 0
                && rightYdistance <= 3) {
            fixSqueeze[0] = true;
        } else if (!fixSqueeze[0] || rightYdistance >= 3) {
            rightDirection = Utils.getDirection(right.anchor,
                    new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() + right.panel.getWidth() / 2 + 5,
                            epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
        }
        right.setDirection(rightDirection);

        double leftXdistance = epsilonPanel.getX() - 5 - (left.panel.getX() + left.panel.getWidth());
        double leftYdistance = Math.abs(left.panel.getY() + left.panel.getHeight() / 2
                - (epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
        Point2D leftDirection = new Point2D.Double(0, 0);
        if (leftXdistance <= 3 &&
                leftXdistance >= 0
                && leftYdistance <= 3) {
            fixSqueeze[1] = true;
        } else if (!fixSqueeze[1] || leftYdistance >= 3) {
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
        SmileyModel.getINSTANCE().ableDecrease = false;
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
        left.setAngleMove();
        right.setAngleMove();
        SmileyModel.getINSTANCE().setAngleMove();
        setAttack("projectile", new Removable() {

            @Override
            public void remove() {
                SmileyHandsModel.getLeft().timers.get("projectile").stop();
                SmileyHandsModel.getRight().timers.get("projectile").stop();
            }

        });
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
        setAttack("vomit", new Removable() {

            @Override
            public void remove() {
                fistAttacks[0] = false;
            }

        });

    }

    public static void punchAttack() {
        setAttack("punch", new Removable() {

            @Override
            public void remove() {
                fistAttacks[1] = false;
            }

        });
        isPunching = true;
        SmileyFistModel fist = SmileyFistModel.getINSTANCE();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        fist.panel.rigid = true;
        for (Model model : BossModel.getAllBossEntities())
            ((BossModel) model).ableDecrease = true;
        // for (Model hand : SmileyHandsModel.items)
        // ((SmileyHandsModel) hand).panel.rigid = true;
        // fist.anchor = new Point2D.Double(epsilonPanel.getX() +
        // epsilonPanel.getWidth() + fist.panel.getWidth() + 40,
        // epsilonPanel.getY() + epsilonPanel.getHeight() / 2);

    }

    private void checkPunch() {
        SmileyFistModel fist = SmileyFistModel.getINSTANCE();
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        // epsilonPanel.speed = fist.speed;
        if (isPunching) {
            Point2D newDirection = Utils.getDirection(fist.anchor,
                    new Point2D.Double(epsilonPanel.getX() + epsilonPanel.getWidth() + fist.panel.getWidth() + 40,
                            epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
            fist.setDirection(newDirection);
            double dis = Math.abs(fist.panel.getX()
                    - (epsilonPanel.getX() + epsilonPanel.getWidth() + fist.panel.getWidth() / 2 + 40));
            if (dis <= 3) {
                isPunching = false;
            }

        } else {
            Point2D newDirection = Utils.getDirection(
                    new Point2D.Double(fist.anchor.getX() - fist.panel.getWidth() / 2,
                            fist.anchor.getY()),
                    new Point2D.Double(
                            epsilonPanel.getX() + epsilonPanel.getWidth() + 3,
                            epsilonPanel.getY() + epsilonPanel.getHeight() / 2));

            // epsilonPanel.availableDimension = new Dimension((int)
            // Constants.PANEL_SIZE.getWidth() - 100,
            // (int) Constants.PANEL_SIZE.getHeight());
            double dis = Utils.getDistance(
                    new Point2D.Double(fist.anchor.getX() - fist.panel.getWidth() / 2,
                            fist.anchor.getY()),
                    new Point2D.Double(
                            epsilonPanel.getX() + epsilonPanel.getWidth(),
                            epsilonPanel.getY() + epsilonPanel.getHeight() / 2));
            if (dis <= 10
                    && epsilonPanel.getWidth() > epsilonPanel.availableDimension.getWidth() - 150) {
                epsilonPanel.setLocation(epsilonPanel.getX() - 10, epsilonPanel.getY());
                epsilonPanel.setSize(epsilonPanel.getWidth() - 10,
                        epsilonPanel.getHeight());
                newDirection = new Point2D.Double(0, 0);

            }

            fist.setDirection(newDirection);
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
                SmileyFistModel.getINSTANCE().ableMove = true;
                fistAttacks[1] = false;
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
            fist.setEnemyImpacts(Constants.MAX_DISTANCE_IMPACT * 8, 1);
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
                shotModel.max_speed /= 2;
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
                fistAttacks[0] = false;
                System.out.println("rapid remove");
            }

        });
    }

    private void checkRapid() {
    }

    public static void slapAttack() {
        SmileyHandsModel.getRight().ableMove = true;
        setAttack("slap", new Removable() {

            @Override
            public void remove() {
                fistAttacks[0] = false;
            }

        });
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
            // for (Timer timer : bossModel.timers.values())
            // timer.stop();
        }
        SmileyModel.getINSTANCE().vomitAnchors = new ArrayList<>();
    }
}
