package gradle.controller;

import java.awt.geom.Point2D;

import java.awt.Polygon;

import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.ShotModel;
import gradle.view.GamePanel;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.ShotView;

public class ShotController {
    public static void checkShotWithPanel(ShotModel shotModel) {
        int dx = 0;
        int dy = 0;
        int dw = 0;
        int dh = 0;
        if (shotModel.getPanelAnchor().getX() <= 0) {
            dx = -1;
            dw = 1;
        } else if (shotModel.getPanelAnchor().getX() > GamePanel.getINSTANCE().getWidth()) {
            dw = 1;
            dx = 1;
        } else if (shotModel.getPanelAnchor().getY() > GamePanel.getINSTANCE().getHeight()) {
            dh = 1;
            dy = 1;
        } else if (shotModel.getPanelAnchor().getY() < 0) {
            dy = -1;
            dh = 1;
        }
        if (dx != 0 || dy != 0)
            GamePanel.getINSTANCE().location = new Point2D.Double(dx, dy);

        if (dh != 0 || dw != 0)
            GamePanel.getINSTANCE().size = new Point2D.Double(dw, dh);
        if (dy != 0 || dx != 0 || dh != 0 || dw != 0) {
            remove(shotModel.getId());
            GamePanel.getINSTANCE().setChanging();
        }

    }

    public static void remove(String Id) {
        ShotModel shotModel = (ShotModel) ShotModel.findById(Id);
        if (shotModel != null) {
            ShotModel.removedItems.add(shotModel);
            ShotView.removedItems.add(ShotView.findById(shotModel.getId()));
            ShotModel.items.remove(shotModel);
            ShotView.items.removeIf(shot -> shot.getId() == shotModel.getId());
        }

    }

    public static void checkCollision() {
        for (int i = 0; i < ShotModel.items.size(); i++) {
            ShotModel shotModel = (ShotModel) ShotModel.items.get(i);
            shotModel.move();

            checkShotWithPanel(shotModel);
            for (int j = 0; j < EnemyModel.items.size(); j++) {
                EnemyModel enemyModel = (EnemyModel) EnemyModel.items.get(j);
                if (checkEpsilonShot(enemyModel, shotModel)) {
                    enemyModel.HP -= 5 + (SkillTreeController.enemy_hp_decrease);
                    if (enemyModel.HP >= 0)
                        // Utils.playMusic("app/src/main/java/gradle/assets/musics/ah.wav");
                    if (ShotModel.items.contains(shotModel)) {
                        remove(shotModel.getId());
                    }
                    if (enemyModel.isImpacting) {
                        enemyModel.impact_speed *= 1.05;
                        enemyModel.setImpact(new Point2D.Double(1, 1), false);
                    } else
                        enemyModel.setImpact(false);
                }
                if (enemyModel.HP <= 0) {
                    EnemyController.remove(enemyModel.getId());
                }

            }
        }
    }

    private static boolean checkEpsilonShot(EnemyModel enemyModel, ShotModel shotModel) {
        Polygon polygon = new Polygon(enemyModel.getXpointsInt(), enemyModel.getYpointsInt(),
                enemyModel.getEnemyPointsNumber());
        if (polygon.contains(shotModel.anchor))
            return true;
        return false;
    }
}
