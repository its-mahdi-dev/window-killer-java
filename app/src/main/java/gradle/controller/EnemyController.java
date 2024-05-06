package gradle.controller;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.Model;
import gradle.view.GamePanel;
import gradle.view.charecretsView.EnemyView;

public class EnemyController {

    static List<EnemyModel> removedEnemies = new ArrayList<>();
    public static boolean isCreating = true;

    public static void checkCollision() {
        removedEnemies = new ArrayList<>();
        for (Model model : EnemyModel.items) {
            EnemyModel enemyModel = (EnemyModel) model;
            enemyModel.setDirection(Utils.getDirection(enemyModel.anchor,
                    EpsilonModel.items.get(0).anchor));

            enemyModel.move();

            setPoints(enemyModel);
            checkEnemyCollision(enemyModel);
            checkEpsilonColision(enemyModel);

        }

        for (EnemyModel enemyModel : removedEnemies) {
            remove(enemyModel.getId());
        }

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
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
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
                    epsilonModel.HP -= enemyModel.power;
                }
            }
        }

        if (Utils.getDistance(point2ds[0], point2ds[1], epsilonModel.anchor) < enemyModel.w
                / 2) {

            if (Utils.isPerpendicular(point2ds[0], point2ds[1], epsilonModel.anchor)) {
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
        for (int i = 0; i < EnemyModel.items.size(); i++) {
            EnemyModel enemy = (EnemyModel) EnemyModel.items.get(i);
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

            int x1 = rand.nextInt(GamePanel.getINSTANCE().getX()) + GamePanel.getINSTANCE().getWidth();
            int y1 = GamePanel.getINSTANCE().getY() + GamePanel.getINSTANCE().getHeight()
                    + Constants.ENEMY_SQUARE_DIAMETER;
            if (i % 2 == 0) {
                y1 = GamePanel.getINSTANCE().getY() - Constants.ENEMY_SQUARE_DIAMETER;
            }
            EnemyModel.create(new Point2D.Double(x1, y1), EnemyType.square);
        }

        for (int i = 0; i < triangleEnemies; i++) {
            int y1 = rand.nextInt(GamePanel.getINSTANCE().getY()) + GamePanel.getINSTANCE().getHeight();
            int x1 = GamePanel.getINSTANCE().getX() + GamePanel.getINSTANCE().getWidth()
                    + Constants.ENEMY_TRIANGLE_DIAMETER;
            if (i % 2 == 0)
                x1 = GamePanel.getINSTANCE().getX() - Constants.ENEMY_TRIANGLE_DIAMETER;

            EnemyModel.create(new Point2D.Double(x1, y1), EnemyType.triangle);
        }

        isCreating = false;
    }
}
