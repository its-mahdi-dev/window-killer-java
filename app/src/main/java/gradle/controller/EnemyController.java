package gradle.controller;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.Model;
import gradle.view.charecretsView.EnemyView;

public class EnemyController {

    static List<EnemyModel> removedEnemies = new ArrayList<>();

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
                System.out.println(newDirection);
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
        // Utils.playMusic("app/src/main/java/gradle/assets/musics/ah2.wav");
        EnemyModel enemyModel = (EnemyModel) EnemyModel.findById(Id);
        if (enemyModel != null) {
            enemyModel.setCollectible();
            EnemyModel.removedItems.add(enemyModel);
            EnemyView.removedItems.add(EnemyView.findById(enemyModel.getId()));
            EnemyModel.items.remove(enemyModel);
            EnemyView.items.removeIf(enemy -> enemy.getId() == enemyModel.getId());
        }
    }
}
