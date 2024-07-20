package gradle.controller;

import java.awt.Polygon;
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
import java.util.Map.Entry;

import javax.swing.Timer;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

import gradle.controller.SkillTreeController.SkillTypes;
import gradle.controller.StoreController.StoreTypes;
import gradle.interfaces.UPSController;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.model.ShotType;
import gradle.model.enemies.ArchmireEnemy;
import gradle.model.enemies.BarricadosMiniboss;
import gradle.model.enemies.BlackorbEnemy;
import gradle.model.enemies.NecropickEnemy;
import gradle.model.enemies.OmenoctEnemy;
import gradle.model.enemies.SquareEnemy;
import gradle.model.enemies.TriangleEnemy;
import gradle.model.enemies.WyrmEnemy;
import gradle.view.GamePanel;
import gradle.view.charecretsView.EnemyView;

public class EnemyController implements UPSController {

    static List<EnemyModel> removedEnemies = new ArrayList<>();
    public static boolean isCreating = true;
    public static final Map<Integer, CreateWave> waves = new HashMap<>();
    public static int deadEnemies;
    public static Timer creatTimer;
    public static int waveStopNumber;

    interface CreateWave {
        void create();
    }

    private static final Random random = new Random();

    @Override
    public void check() {

        // if (!isCreating) {
        removedEnemies = new ArrayList<>();
        for (Model model : EnemyModel.getAllEnemies()) {
            EnemyModel enemyModel = (EnemyModel) model;

            if (StoreController.itemsActive.get(StoreTypes.hypnos))
                enemyModel.ableMove = false;
            else if (enemyModel.type != EnemyType.barricados && enemyModel.type != EnemyType.blackorb
                    && enemyModel.type != EnemyType.necropick)
                enemyModel.ableMove = true;

            Point2D direction = Utils.getDirection(enemyModel.anchor,
                    EpsilonModel.getINSTANCE().anchor);
            double distance = Utils.getDistance(enemyModel.anchor, EpsilonModel.getINSTANCE().anchor);
            if (StoreController.itemsActive.get(StoreTypes.deimos) && distance <= Constants.DEIMOS_RADUIS
                    && !enemyModel.hovering) {
                if (Math.abs(distance - Constants.DEIMOS_RADUIS) < 3) {
                    enemyModel.setDirection(new Point2D.Double(0, 0));
                    enemyModel.moveWithAngle();
                } else {
                    enemyModel.setDirection(new Point2D.Double(-direction.getX(), -direction.getY()));
                    enemyModel.move();
                }

            } else {
                if (enemyModel.type == EnemyType.wyrm) {
                    // enemyModel.updatePosition();
                    Point2D newDirection = ((WyrmEnemy) enemyModel)
                            .getTangentialDirection(EpsilonModel.getINSTANCE().anchor);
                    enemyModel.setDirection(newDirection);
                    enemyModel.move();
                    enemyModel.setRelativePoints();
                } else {
                    if (enemyModel.type == EnemyType.omenoct)
                        checkOmenoctMove(enemyModel, direction);
                    else
                        enemyModel.setDirection(direction);

                    enemyModel.move();
                }
            }
            if (!StoreController.itemsActive.get(StoreTypes.hypnos))
                checkEnemyAbilities(enemyModel);
            if (enemyModel.ableMove)
                setPoints(enemyModel);
            if (enemyModel.type == EnemyType.archmire)
                ((ArchmireEnemy) enemyModel).updatePathHistory();

            if (enemyModel.type != EnemyType.blackorb) {
                checkEnemyCollision(enemyModel);
                checkEpsilonColision(enemyModel);
            } else {
                checkBlackOrbCollision((BlackorbEnemy) enemyModel);
            }
            if (enemyModel.HP <= 0 && enemyModel.type != EnemyType.barricados)
                removedEnemies.add(enemyModel);
        }

        for (EnemyModel enemyModel : removedEnemies) {
            remove(enemyModel.getId());
            deadEnemies++;
        }
        // }
        if (EnemyModel.getAllEnemies().size() > 0) {
            for (int i = 0; i < EnemyView.getEnemyViews().size(); i++) {
                EnemyView enemyView = (EnemyView) EnemyView.getEnemyViews().get(i);
                EnemyModel enemyModel = (EnemyModel) EnemyModel.findById(enemyView.getId());
                if (enemyModel != null)
                    enemyView.setUtil(enemyModel);
            }
        }
        if (deadEnemies == waveStopNumber && creatTimer != null) {
            creatTimer.stop();
            isCreating = false;
        }

        if (!isCreating && EnemyModel.getAllEnemies().size() == 0) {
            // System.out.println("here");
            GameController.createWave();
        }

    }

    public static void checkOmenoctMove(EnemyModel enemyModel, Point2D direction) {
        GamePanel currentPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        int x1, x2, y1, y2 = 0;
        int enemyPosition = 0;
        if (enemyModel.anchor.getX() >= currentPanel.getX()
                && enemyModel.anchor.getX() <= currentPanel.getX() + currentPanel.getWidth()) {
            x1 = currentPanel.getX();
            x2 = x1 + currentPanel.getWidth();
            if (enemyModel.anchor.getY() < currentPanel.getY()) {
                y1 = currentPanel.getY();
                y2 = currentPanel.getY();
                enemyPosition = 1;
            } else {
                y1 = currentPanel.getY() + currentPanel.getHeight();
                y2 = currentPanel.getY() + currentPanel.getHeight();
                enemyPosition = 3;
            }
        } else {
            y1 = currentPanel.getY();
            x2 = y1 + currentPanel.getHeight();
            if (enemyModel.anchor.getX() < currentPanel.getX()) {
                x1 = currentPanel.getX();
                x2 = currentPanel.getX();
                enemyPosition = 4;
            } else {
                x1 = currentPanel.getX() + currentPanel.getWidth();
                x2 = currentPanel.getX() + currentPanel.getWidth();
                enemyPosition = 2;
            }
        }
        boolean ableMove = true;
        switch (enemyPosition) {
            case 1:
                if (enemyModel.anchor.getY() + enemyModel.h >= currentPanel.getY())
                    ableMove = false;
                break;
            case 2:
                if (enemyModel.anchor.getX() - enemyModel.w <= currentPanel.getX() + currentPanel.getWidth())
                    ableMove = false;
                break;
            case 3:
                if (enemyModel.anchor.getY() - enemyModel.h <= currentPanel.getY() + currentPanel.getHeight())
                    ableMove = false;
                break;
            case 4:
                if (enemyModel.anchor.getX() + enemyModel.h >= currentPanel.getX())
                    ableMove = false;
                break;
            default:
                break;
        }
        Point2D newDirection = direction;
        if (!ableMove)
            newDirection = new Point2D.Double(0, 0);

        if (!ableMove && (enemyModel.anchor.getX() + enemyModel.w > currentPanel.getX() + 5) &&
                (enemyModel.anchor.getX() - enemyModel.w + 5 < currentPanel.getX() + currentPanel.getWidth()) &&
                (enemyModel.anchor.getY() + enemyModel.h > currentPanel.getY() + 5) &&
                (enemyModel.anchor.getY() - enemyModel.h + 5 < currentPanel.getY() + currentPanel.getHeight())) {

            // System.out.println(
            // (enemyModel.anchor.getX() - enemyModel.w) + " " + (currentPanel.getX() +
            // currentPanel.getWidth()));
            if (enemyModel.anchor.getX() + enemyModel.w > currentPanel.getX()
                    && enemyModel.anchor.getX() < currentPanel.getX() + currentPanel.getWidth() / 2)
                newDirection = new Point2D.Double(-1, 0);
            else if (enemyModel.anchor.getX() - enemyModel.w < currentPanel.getX() +
                    currentPanel.getWidth()
                    && enemyModel.anchor.getX() > currentPanel.getX() + currentPanel.getWidth() / 2)
                newDirection = new Point2D.Double(1, 0);
            else if (enemyModel.anchor.getY() + enemyModel.h > currentPanel.getY()
                    && enemyModel.anchor.getY() < currentPanel.getY() + currentPanel.getHeight() / 2)
                newDirection = new Point2D.Double(0, -1);
            else if (enemyModel.anchor.getY() - enemyModel.h < currentPanel.getY() +
                    currentPanel.getHeight()
                    && enemyModel.anchor.getY() > currentPanel.getY() + currentPanel.getHeight() / 2)
                newDirection = new Point2D.Double(0, 1);
        }
        enemyModel.setDirection(newDirection);

    }

    public static void setPoints(EnemyModel enemyModel) {
        for (int i = 0; i < enemyModel.xPoints.length; i++) {
            enemyModel.xPoints[i] = (enemyModel.xPoints[i] + enemyModel.direction.getX() * enemyModel.speed);
        }
        for (int i = 0; i < enemyModel.yPoints.length; i++) {
            enemyModel.yPoints[i] = (enemyModel.yPoints[i] + enemyModel.direction.getY() * enemyModel.speed);
        }
    }

    public static void checkEpsilonColision(EnemyModel enemyModel) {
        if (enemyModel.type == EnemyType.archmire) {
            Polygon polygon = new Polygon(enemyModel.getXpointsInt(), enemyModel.getYpointsInt(),
                    enemyModel.xPoints.length);
            if (polygon.contains(EpsilonModel.getINSTANCE().anchor)) {
                if (EpsilonModel.getINSTANCE().times.get("archmire") == null) {
                    EpsilonModel.getINSTANCE().times.put("archmire", System.currentTimeMillis());
                    EpsilonModel.getINSTANCE().HP -= enemyModel.attacks.get("drown");
                } else {
                    if (System.currentTimeMillis() - EpsilonModel.getINSTANCE().times.get("archmire") > 1000) {
                        EpsilonModel.getINSTANCE().times.put("archmire", System.currentTimeMillis());
                        EpsilonModel.getINSTANCE().HP -= enemyModel.attacks.get("drown");
                    }
                }
            }
            if (isPointInPastArea(enemyModel, EpsilonModel.getINSTANCE().anchor.getX(),
                    EpsilonModel.getINSTANCE().anchor.getY())) {
                if (EpsilonModel.getINSTANCE().times.get("archmire") == null) {
                    EpsilonModel.getINSTANCE().times.put("archmire", System.currentTimeMillis());
                    EpsilonModel.getINSTANCE().HP -= enemyModel.attacks.get("aoe");
                } else {
                    if (System.currentTimeMillis() - EpsilonModel.getINSTANCE().times.get("archmire") > 1000) {
                        EpsilonModel.getINSTANCE().times.put("archmire", System.currentTimeMillis());
                        EpsilonModel.getINSTANCE().HP -= enemyModel.attacks.get("aoe");
                    }
                }
            }
        }
        if (enemyModel.hovering)
            return;
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        Point2D[] point2ds = Utils.getNearestPoints(enemyModel.xPoints, enemyModel.yPoints, epsilonModel.anchor);
        int archiveHP = enemyModel.HP;
        Point2D newDirection = Utils.getDirection(epsilonModel.anchor, enemyModel.anchor);
        for (int i = 0; i < enemyModel.xPoints.length; i++) {
            if (Math.abs(epsilonModel.anchor.getX() - enemyModel.xPoints[i]) <= epsilonModel.w / 2
                    && Math.abs(epsilonModel.anchor.getY() - enemyModel.yPoints[i]) <= epsilonModel.w / 2) {
                enemyModel.decreasHp(SkillTreeController.enemy_hp_collision_decrease);
                enemyModel.setImpact(newDirection, true, true);
                if (SkillTreeController.activeSkills.get(SkillTypes.melampus)) {
                    if (random.nextInt(100) > 5)
                        epsilonModel.decreasHp(enemyModel.attacks.get("melee"));
                } else
                    epsilonModel.decreasHp(enemyModel.attacks.get("melee"));
            }
        }

        if (Utils.getDistance(point2ds[0], point2ds[1], epsilonModel.anchor) < epsilonModel.w
                / 2) {

            if (Utils.isPerpendicular(point2ds[0], point2ds[1], epsilonModel.anchor)) {
                if (enemyModel.type != EnemyType.wyrm) {
                    for (Model vertex : EpsilonVertexModel.items) {
                        if (Utils.getDistance(point2ds[0], point2ds[1], vertex.anchor) < vertex.w * 2) {
                            enemyModel.decreasHp(Constants.EPSILON_POWER);
                        }

                    }
                }

                enemyModel.decreasHp(SkillTreeController.enemy_hp_collision_decrease);
                enemyModel.setImpact(newDirection, true, true);
            }

        }

        if (enemyModel.HP < archiveHP && SkillTreeController.activeSkills.get(SkillTypes.chiron)) {
            epsilonModel.HP += 3;
        }

    }

    public static void checkEnemyCollision(EnemyModel enemyModel) {
        if (enemyModel.type == EnemyType.archmire) {
            for (int i = 0; i < EnemyModel.getAllEnemies().size(); i++) {
                EnemyModel enemy = (EnemyModel) EnemyModel.getAllEnemies().get(i);
                if (enemy.type != EnemyType.blackorb) {
                    if (enemy.equals(enemyModel))
                        continue;
                    Polygon polygon = new Polygon(enemyModel.getXpointsInt(), enemyModel.getYpointsInt(),
                            enemyModel.xPoints.length);
                    if (polygon.contains(enemy.anchor)) {
                        if (enemy.times.get("archmire") == null) {
                            enemy.times.put("archmire", System.currentTimeMillis());
                            enemy.HP -= enemyModel.attacks.get("drown");
                        } else {
                            if (System.currentTimeMillis() - enemy.times.get("archmire") > 1000) {
                                enemy.times.put("archmire", System.currentTimeMillis());
                                enemy.HP -= enemyModel.attacks.get("drown");
                            }
                        }
                    }
                    if (isPointInPastArea(enemyModel, enemy.anchor.getX(),
                            enemy.anchor.getY())) {
                        if (enemy.times.get("archmire") == null) {
                            enemy.times.put("archmire", System.currentTimeMillis());
                            enemy.HP -= enemyModel.attacks.get("aoe");
                        } else {
                            if (System.currentTimeMillis() - enemy.times.get("archmire") > 1000) {
                                enemy.times.put("archmire", System.currentTimeMillis());
                                enemy.HP -= enemyModel.attacks.get("aoe");
                            }
                        }
                    }
                }
            }
        }
        if (enemyModel.hovering)
            return;
        for (int i = 0; i < EnemyModel.getAllEnemies().size(); i++) {
            EnemyModel enemy = (EnemyModel) EnemyModel.getAllEnemies().get(i);
            if (enemy.hovering)
                continue;
            if (!enemy.equals(enemyModel) && isEnemyCollision(enemyModel, enemy)) {
                Point2D newDirection = Utils.getDirection(enemy.anchor, enemyModel.anchor);
                enemyModel.setImpact(newDirection, true, true);
            }
        }
    }

    private static boolean isEnemyCollision(EnemyModel enemyModel1, EnemyModel enemyModel2) {
        Polygon polygon = new Polygon(enemyModel1.getXpointsInt(), enemyModel1.getYpointsInt(),
                enemyModel1.xPoints.length);
        for (int i = 0; i < enemyModel2.xPoints.length; i++) {
            if (polygon.contains(new Point2D.Double(enemyModel2.xPoints[i], enemyModel2.yPoints[i])))
                return true;
        }

        return false;
    }

    private static void checkBlackOrbCollision(BlackorbEnemy enemyModel) {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();

        // ENEMIES
        for (Model model : EnemyModel.getAllEnemies()) {
            EnemyModel enemy = (EnemyModel) model;

            if (enemy.type != EnemyType.blackorb) {
                for (Point2D orb : enemyModel.orbs) {
                    Point2D newDirection = Utils.getDirection(orb, enemy.anchor);
                    Point2D[] point2ds = Utils.getNearestPoints(enemy.xPoints, enemy.yPoints, orb);
                    // System.out.println(point2ds + " -> " +enemy.type);
                    if (Utils.getDistance(point2ds[0], point2ds[1], orb) < enemyModel.w
                            / 2) {

                        if (Utils.isPerpendicular(point2ds[0], point2ds[1], orb)) {

                            enemy.setImpact(newDirection, true, true);
                            // System.out.println("hereee");
                        }

                    }

                    // POINTS
                    for (int i = 0; i < enemy.xPoints.length; i++) {
                        if (Math.abs(orb.getX() - enemy.xPoints[i]) <= enemyModel.w / 2
                                && Math.abs(orb.getY() - enemy.yPoints[i]) <= enemyModel.w / 2) {
                            enemy.setImpact(newDirection, true, true);

                        }
                    }
                }

                // LASER
                for (Polygon polygon : enemyModel.getOrbsPolygon()) {
                    for (int i = 0; i < enemy.xPoints.length; i++) {
                        if (polygon.contains(new Point2D.Double(enemy.xPoints[i], enemy.yPoints[i]))) {
                            if (enemy.times.get("blackorb") == null) {
                                enemy.times.put("blackorb", System.currentTimeMillis());
                                enemy.HP -= enemyModel.attacks.get("laser");
                            } else {
                                if (System.currentTimeMillis() - enemy.times.get("blackorb") > 1000) {
                                    enemy.times.put("blackorb", System.currentTimeMillis());
                                    enemy.HP -= enemyModel.attacks.get("laser");
                                }
                            }
                        }
                    }
                }

            }
        }

        // EPSILON
        for (Point2D orb : enemyModel.orbs) {
            if (Utils.getDistance(orb, epsilonModel.anchor) <= epsilonModel.w / 2 + enemyModel.w / 2) {
                for (Model vertex : EpsilonVertexModel.items) {
                    if (Utils.getDistance(orb, vertex.anchor) <= vertex.w + enemyModel.w / 2) {
                        enemyModel.HP -= Constants.EPSILON_POWER;
                    }
                }
                Point2D newDirection = Utils.getDirection(orb, epsilonModel.anchor);
                epsilonModel.setImpact(newDirection, true, true);
            }
        }

        if (!StoreController.itemsActive.get(StoreTypes.hypnos)) {
            for (Polygon polygon : enemyModel.getOrbsPolygon()) {
                if (polygon.contains(epsilonModel.anchor)) {
                    if (epsilonModel.times.get("blackorb") == null) {
                        epsilonModel.times.put("blackorb", System.currentTimeMillis());
                        epsilonModel.HP -= enemyModel.attacks.get("laser");
                    } else {
                        if (System.currentTimeMillis() - epsilonModel.times.get("blackorb") > 1000) {
                            epsilonModel.times.put("blackorb", System.currentTimeMillis());
                            epsilonModel.HP -= enemyModel.attacks.get("laser");
                        }
                    }
                }
            }
        }
    }

    public static void remove(String Id) {

        EnemyModel enemyModel = (EnemyModel) EnemyModel.findById(Id);

        if (enemyModel != null) {
            Utils.playMusic("enemyDeath", false);
            for (Timer timers : enemyModel.timers.values())
                timers.stop();
            enemyModel.removeUtils();
            enemyModel.timers.clear();
            enemyModel.setCollectible();
            enemyModel.getRemovedItems().add(enemyModel);
            EnemyView enemyView = (EnemyView) EnemyView.findById(enemyModel.getId());
            enemyView.getRemovedItems().add(enemyView);
            enemyModel.getItems().remove(enemyModel);
            enemyView.getItems().removeIf(enemy -> enemy.getId() == enemyModel.getId());
        }
    }

    public static void setWaveMethods() {
        waves.put(1, new CreateWave() {

            @Override
            public void create() {
                waveStopNumber = GameController.waveNumbers.get(1) + ((int) GameSettings.level * 2);
                System.out.println("stop" + waveStopNumber);
                creatTimer = new Timer(1500, new ActionListener() {

                    int count = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (GameSettings.isGameRun && !GameSettings.isPause) {
                            count++;
                            Point2D randomPosition = randomEnemyPosition();
                            if (count % 2 == 0)
                                SquareEnemy.create(randomPosition);
                            else
                                TriangleEnemy.create(randomPosition);
                        }
                    }

                });

                creatTimer.start();
            }

        });
        waves.put(2, new CreateWave() {

            @Override
            public void create() {
                waveStopNumber = GameController.waveNumbers.get(2) + ((int) GameSettings.level * 2);
                deadEnemies = 0;
                System.out.println("stoppp" + waveStopNumber);
                creatTimer = new Timer(1500, new ActionListener() {
                    int count = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (GameSettings.isGameRun && !GameSettings.isPause) {
                            count++;
                            System.out.println(deadEnemies);
                            Point2D randomPosition = randomEnemyPosition();
                            if (count % 3 == 0)
                                OmenoctEnemy.create(randomPosition);
                            if (count % 3 == 1)
                                SquareEnemy.create(randomPosition);
                            else if (count % 3 == 2)
                                TriangleEnemy.create(randomPosition);
                        }
                    }

                });

                creatTimer.start();
            }

        });
        waves.put(3, new CreateWave() {

            @Override
            public void create() {
                waveStopNumber = GameController.waveNumbers.get(3) + ((int) GameSettings.level * 2);
                deadEnemies = 0;
                System.out.println("stoppp" + waveStopNumber);
                BarricadosMiniboss.create(randomEnemyPosition());
                NecropickEnemy.create(randomEnemyPosition());
                creatTimer = new Timer(1700, new ActionListener() {
                    int count = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (GameSettings.isGameRun && !GameSettings.isPause) {
                            count++;
                            System.out.println(deadEnemies);
                            Point2D randomPosition = randomEnemyPosition();
                            if (count == 6)
                                NecropickEnemy.create(randomPosition);
                            else if (count % 3 == 0)
                                SquareEnemy.create(randomPosition);
                            else if (count % 3 == 1)
                                ArchmireEnemy.create(randomPosition);
                            else if (count % 3 == 2)
                                OmenoctEnemy.create(randomPosition);
                        }
                    }

                });

                creatTimer.start();
            }

        });
        waves.put(4, new CreateWave() {

            @Override
            public void create() {
                waveStopNumber = GameController.waveNumbers.get(4) + ((int) GameSettings.level * 2);
                deadEnemies = 0;
                System.out.println("stoppp" + waveStopNumber);
                BarricadosMiniboss.create(randomEnemyPosition());
                BlackorbEnemy.create(randomEnemyPosition());
                NecropickEnemy.create(randomEnemyPosition());
                OmenoctEnemy.create(randomEnemyPosition());
                creatTimer = new Timer(1700, new ActionListener() {
                    int count = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (GameSettings.isGameRun && !GameSettings.isPause) {
                            count++;
                            System.out.println(deadEnemies);
                            Point2D randomPosition = randomEnemyPosition();
                            if (count % 3 == 0)
                                TriangleEnemy.create(randomPosition);
                            else if (count % 3 == 1)
                                WyrmEnemy.create(randomPosition);
                            else if (count % 3 == 2)
                                ArchmireEnemy.create(randomPosition);
                        }
                    }

                });

                creatTimer.start();
            }

        });
        waves.put(5, new CreateWave() {

            @Override
            public void create() {
                waveStopNumber = GameController.waveNumbers.get(5) + ((int) GameSettings.level * 2);
                deadEnemies = 0;
                System.out.println("stoppp" + waveStopNumber);
                BarricadosMiniboss.create(randomEnemyPosition());
                BlackorbEnemy.create(randomEnemyPosition());
                BlackorbEnemy.create(randomEnemyPosition());
                NecropickEnemy.create(randomEnemyPosition());
                OmenoctEnemy.create(randomEnemyPosition());
                creatTimer = new Timer(1700, new ActionListener() {
                    int count = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (GameSettings.isGameRun && !GameSettings.isPause) {
                            count++;
                            System.out.println(deadEnemies);
                            Point2D randomPosition = randomEnemyPosition();
                            if (count % 3 == 0)
                                SquareEnemy.create(randomPosition);
                            else if (count % 3 == 1)
                                ArchmireEnemy.create(randomPosition);
                            else if (count % 3 == 2)
                                WyrmEnemy.create(randomPosition);
                        }
                    }

                });

                creatTimer.start();
            }

        });
    }

    public static Point2D randomEnemyPosition() {
        GamePanel epsilonPanel = EpsilonModel.getINSTANCE().currentPanels.get(0);
        int x1 = random.nextInt(epsilonPanel.getWidth() + 400) + epsilonPanel.getX() - 200;
        int y1 = random.nextInt(epsilonPanel.getHeight() + 200) + epsilonPanel.getY() - 100;
        return new Point2D.Double(x1, y1);
    }

    public static void createEnemyWave1(int number) {
        int squareEnemies = number / 2;
        int triangleEnemies = number - squareEnemies;

        for (int i = 0; i < squareEnemies; i++) {

            int x1 = random.nextInt(EpsilonModel.getINSTANCE().currentPanels.get(0).getX())
                    + EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth();
            int y1 = EpsilonModel.getINSTANCE().currentPanels.get(0).getY()
                    + EpsilonModel.getINSTANCE().currentPanels.get(0).getHeight()
                    + Constants.ENEMY_SQUARE_DIAMETER;
            if (i % 2 == 0) {
                y1 = EpsilonModel.getINSTANCE().currentPanels.get(0).getY() - Constants.ENEMY_SQUARE_DIAMETER;
            }
            SquareEnemy.create(new Point2D.Double(x1, y1));
        }

        for (int i = 0; i < triangleEnemies; i++) {
            int y1 = random.nextInt(EpsilonModel.getINSTANCE().currentPanels.get(0).getY())
                    + EpsilonModel.getINSTANCE().currentPanels.get(0).getHeight();
            int x1 = EpsilonModel.getINSTANCE().currentPanels.get(0).getX()
                    + EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth()
                    + Constants.ENEMY_TRIANGLE_DIAMETER;
            if (i % 2 == 0)
                x1 = EpsilonModel.getINSTANCE().currentPanels.get(0).getX() - Constants.ENEMY_TRIANGLE_DIAMETER;

            TriangleEnemy.create(new Point2D.Double(x1, y1));
        }
    }

    public static boolean isPointInPastArea(EnemyModel enemyModel, double x, double y) {
        ArchmireEnemy archmireEnemy = (ArchmireEnemy) enemyModel;
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate pointCoordinate = new Coordinate(x, y);
        org.locationtech.jts.geom.Point point = geometryFactory.createPoint(pointCoordinate);

        for (org.locationtech.jts.geom.Polygon polygon : archmireEnemy.pathHistory) {
            if (polygon.contains(point)) {
                return true;
            }
        }
        return false;
    }

    public static void removeAll() {

        for (int i = EnemyModel.getAllEnemies().size() - 1; i >= 0; i--) {
            remove(EnemyModel.getAllEnemies().get(i).getId());
        }
    }

    private static void checkEnemyAbilities(EnemyModel enemyModel) {
        if (enemyModel.type == EnemyType.necropick) {
            if (System.currentTimeMillis() - enemyModel.times.get("hovering") >= 4000) {
                enemyModel.times.replace("hovering", System.currentTimeMillis());
                startNecripick(enemyModel);
            }
            // if (System.currentTimeMillis() - enemyModel.times.get("gravity") >= 4000 &&
            // enemyModel.hovering) {
            // enemyModel.times.replace("hovering", System.currentTimeMillis());
            // enemyModel.hovering = false;
            // enemyModel.gravity = true;
            // System.out.println("finish hovering");
            // }
            if (enemyModel.hovering)
                enemyModel.visible = false;
            else
                enemyModel.visible = true;
        } else if (enemyModel.type == EnemyType.barricados) {
            if (System.currentTimeMillis() - enemyModel.created_time >= 2 * 60 * 1000)
                removedEnemies.add(enemyModel);
        }
    }

    private static void startNecripick(EnemyModel enemyModel) {
        if (enemyModel.hovering) {
            System.out.println("start");
            Point2D epsilonAnchor = EpsilonModel.getINSTANCE().anchor;
            // enemyModel.ableMove = false;
            enemyModel.hovering = false;
            int[] possibleValues = { -1, 1 };
            int randomX = random.nextInt(possibleValues.length);
            int randomY = random.nextInt(possibleValues.length);
            enemyModel.anchor = new Point2D.Double(
                    epsilonAnchor.getX() + possibleValues[randomX] * Constants.NECROPICK_EPSILON_RADIUS,
                    epsilonAnchor.getY() + possibleValues[randomY] * Constants.NECROPICK_EPSILON_RADIUS);
            enemyModel.setRelativePoints();

            List<Point2D> aroundPoints = Utils.generateSymmetricPoints(enemyModel.anchor, 8);

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                for (int i = 0; i < aroundPoints.size(); i++) {
                    Point2D direction = Utils.getDirection(enemyModel.anchor, aroundPoints.get(i));
                    ShotModel shotModel = ShotModel.create(enemyModel.anchor, ShotType.enemy, 5);
                    shotModel.setDirection(direction);
                }
            }, 3, TimeUnit.SECONDS);

        } else {
            // enemyModel.ableMove = true;
            enemyModel.hovering = true;
        }
    }
}
