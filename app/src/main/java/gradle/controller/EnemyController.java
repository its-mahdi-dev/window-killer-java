package gradle.controller;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;

public class EnemyController {
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
        for (int i = 0; i < enemyModel.xPoints.length; i++) {
            if (Math.abs(epsilonModel.anchor.getX() - enemyModel.xPoints[i]) < epsilonModel.w / 2
                    && Math.abs(epsilonModel.anchor.getY() - enemyModel.yPoints[i]) < epsilonModel.w / 2) {
                epsilonModel.setImpact();
                enemyModel.setImpact();

            }
        }

        if (Utils.getDistance(point2ds[0], point2ds[1], epsilonModel.anchor) < enemyModel.w / 2) {
            epsilonModel.setImpact();
            enemyModel.setImpact();
        }

    }
}
