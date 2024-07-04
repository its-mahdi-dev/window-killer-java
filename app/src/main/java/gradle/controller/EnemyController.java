package gradle.controller;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Map.Entry;

import javax.swing.Timer;

import gradle.interfaces.UPSController;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.model.ShotType;
import gradle.view.GamePanel;
import gradle.view.charecretsView.EnemyView;

public class EnemyController implements UPSController {

    static List<EnemyModel> removedEnemies = new ArrayList<>();
    public static boolean isCreating = true;

    @Override
    public void check() {
        if (!isCreating) {
            removedEnemies = new ArrayList<>();
            for (Model model : EnemyModel.items) {
                EnemyModel enemyModel = (EnemyModel) model;

                Point2D direction = Utils.getDirection(enemyModel.anchor,
                        EpsilonModel.getINSTANCE().anchor);
                if (enemyModel.type == EnemyType.omenoct)
                    checkOmenoctMove(enemyModel, direction);
                else
                    enemyModel.setDirection(direction);

                enemyModel.move();
                checkEnemyAbilities(enemyModel);
                if (enemyModel.ableMove)
                    setPoints(enemyModel);
                checkEnemyCollision(enemyModel);
                checkEpsilonColision(enemyModel);
            }

            for (EnemyModel enemyModel : removedEnemies) {
                remove(enemyModel.getId());
            }
        }
        if (!isCreating && EnemyModel.items.size() > 0) {
            for (int i = 0; i < EnemyView.items.size(); i++) {
                EnemyView enemyView = (EnemyView) EnemyView.items.get(i);
                EnemyModel enemyModel = (EnemyModel) EnemyModel.findById(enemyView.getId());
                if (enemyModel != null)
                    enemyView.setUtil(enemyModel);
            }
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
        if (enemyModel.hovering)
            return;
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        Point2D[] point2ds = Utils.getNearestPoints(enemyModel.xPoints, enemyModel.yPoints, epsilonModel.anchor);

        Point2D newDirection = Utils.getDirection(epsilonModel.anchor, enemyModel.anchor);
        for (int i = 0; i < enemyModel.xPoints.length; i++) {
            if (Math.abs(epsilonModel.anchor.getX() - enemyModel.xPoints[i]) <= epsilonModel.w / 2
                    && Math.abs(epsilonModel.anchor.getY() - enemyModel.yPoints[i]) <= epsilonModel.w / 2) {
                // if (epsilonModel.isMoving) {
                // epsilonModel.setImpact();
                // } else {
                // epsilonModel.setImpact(enemyModel.direction);
                // }
                enemyModel.setImpact(newDirection, true, true);

                if (System.currentTimeMillis() - epsilonModel.HP_time > 200) {
                    epsilonModel.HP_time = System.currentTimeMillis();
                    epsilonModel.HP -= enemyModel.attacks.get("melee");
                }
            }
        }

        if (Utils.getDistance(point2ds[0], point2ds[1], epsilonModel.anchor) < enemyModel.w
                / 2) {

            if (Utils.isPerpendicular(point2ds[0], point2ds[1], epsilonModel.anchor)) {
                System.out.println("killl");
                // if (epsilonModel.isMoving) {
                // epsilonModel.setImpact();
                // } else {
                // epsilonModel.setImpact(enemyModel.direction);
                // }
                for (Model vertex : EpsilonVertexModel.items) {
                    // System.out.println(Utils.getDistance(point2ds[0], point2ds[1], vertex.anchor)
                    // + " -> " + point2ds[0]
                    // + " -- " + point2ds[1]
                    // + " ->" + vertex.anchor);
                    if (Utils.getDistance(point2ds[0], point2ds[1], vertex.anchor) < vertex.w * 2) {
                        enemyModel.HP -= Constants.EPSILON_POWER;
                        if (enemyModel.HP <= 0) {
                            removedEnemies.add(enemyModel);
                        }
                    }

                }
                enemyModel.setImpact(newDirection, true, true);
            }

        }

    }

    public static void checkEnemyCollision(EnemyModel enemyModel) {
        if (enemyModel.hovering)
            return;
        for (int i = 0; i < EnemyModel.items.size(); i++) {
            EnemyModel enemy = (EnemyModel) EnemyModel.items.get(i);
            if (enemy.hovering)
                continue;
            if (!enemy.equals(enemyModel) && isEnemyCollision(enemyModel, enemy)) {
                Point2D newDirection = Utils.getDirection(enemy.anchor, enemyModel.anchor);
                // enemyModel.anchor = new Point2D.Double(
                // enemyModel.anchor.getX() + (newDirection.getX() * -5),
                // enemyModel.anchor.getY() + (newDirection.getY() * -5));
                enemyModel.setImpact(newDirection, true, true);
                // enemy.setImpact();
            }
        }
    }

    private static boolean isEnemyCollision(EnemyModel enemyModel1, EnemyModel enemyModel2) {
        Polygon polygon = new Polygon(enemyModel1.getXpointsInt(), enemyModel1.getYpointsInt(),
                enemyModel1.getEnemyPointsNumber());
        for (int i = 0; i < enemyModel2.xPoints.length; i++) {
            if (polygon.contains(new Point2D.Double(enemyModel2.xPoints[i], enemyModel2.yPoints[i])))
                return true;
        }

        return false;
    }

    public static void remove(String Id) {

        EnemyModel enemyModel = (EnemyModel) EnemyModel.findById(Id);

        if (enemyModel != null) {
            Utils.playMusic("enemyDeath", false);
            for (Timer timers : enemyModel.timers.values())
                timers.stop();
            enemyModel.setCollectible();
            EnemyModel.removedItems.add(enemyModel);
            EnemyView.removedItems.add(EnemyView.findById(enemyModel.getId()));
            EnemyModel.items.remove(enemyModel);
            EnemyView.items.removeIf(enemy -> enemy.getId() == enemyModel.getId());
        }
    }

    public static void createEnemyWaves(int number) {
        int squareEnemies = number / 2;
        int triangleEnemies = number - squareEnemies;
        Random rand = new Random();

        for (int i = 0; i < squareEnemies; i++) {

            int x1 = rand.nextInt(EpsilonModel.getINSTANCE().currentPanels.get(0).getX())
                    + EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth();
            int y1 = EpsilonModel.getINSTANCE().currentPanels.get(0).getY()
                    + EpsilonModel.getINSTANCE().currentPanels.get(0).getHeight()
                    + Constants.ENEMY_SQUARE_DIAMETER;
            if (i % 2 == 0) {
                y1 = EpsilonModel.getINSTANCE().currentPanels.get(0).getY() - Constants.ENEMY_SQUARE_DIAMETER;
            }
            EnemyModel.create(new Point2D.Double(x1, y1), EnemyType.square);
        }

        for (int i = 0; i < triangleEnemies; i++) {
            int y1 = rand.nextInt(EpsilonModel.getINSTANCE().currentPanels.get(0).getY())
                    + EpsilonModel.getINSTANCE().currentPanels.get(0).getHeight();
            int x1 = EpsilonModel.getINSTANCE().currentPanels.get(0).getX()
                    + EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth()
                    + Constants.ENEMY_TRIANGLE_DIAMETER;
            if (i % 2 == 0)
                x1 = EpsilonModel.getINSTANCE().currentPanels.get(0).getX() - Constants.ENEMY_TRIANGLE_DIAMETER;

            EnemyModel.create(new Point2D.Double(x1, y1), EnemyType.triangle);
        }

        isCreating = false;
    }

    public static void removeAll() {

        for (int i = EnemyModel.items.size() - 1; i >= 0; i--) {
            remove(EnemyModel.items.get(i).getId());
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
        }
    }

    private static void startNecripick(EnemyModel enemyModel) {
        if (enemyModel.hovering) {
            System.out.println("start");
            Point2D epsilonAnchor = EpsilonModel.getINSTANCE().anchor;
            // enemyModel.ableMove = false;
            enemyModel.hovering = false;
            int[] possibleValues = { -1, 1 };
            Random random = new Random();
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
